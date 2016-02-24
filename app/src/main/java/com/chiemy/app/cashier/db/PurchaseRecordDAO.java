package com.chiemy.app.cashier.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import com.chiemy.app.cashier.bean.Goods;
import com.chiemy.app.cashier.bean.PurchaseRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by chiemy on 16/2/21.
 */
public class PurchaseRecordDAO {
    private static final String TABLE_NAME = "purchaseRecord";

    private static PurchaseRecordDAO instance;

    private Context context;

    private Handler handler;

    private Map<String, SQLiteDatabase> databaseMap = new HashMap<>(1);

    private GoodsDAO goodsDAO;

    private PurchaseRecordDAO(Context context){
        this.context = context;
        handler = new Handler(Looper.getMainLooper());
        goodsDAO = GoodsDAO.getInstance(context);
    }

    public static PurchaseRecordDAO getInstance(Context context) {
        if (instance == null){
            synchronized (PurchaseRecordDAO.class){
                instance = new PurchaseRecordDAO(context.getApplicationContext());
            }
        }
        return instance;
    }

    public void insert(final PurchaseRecord record, final DBUpdateCallback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Goods goods = record.getGoods();
                    int quantity = goodsDAO.queryQuantity(goods) + record.quantity;
                    Log.d("-", ">>>quantity = " + quantity);
                    goods.quantity = quantity;
                    boolean success = goodsDAO.insertOrUpdate(goods);
                    Log.d("PurchaseRecordDAO", "insert = " + success);
                    SQLiteDatabase db = getDB(record.user_id);
                    long result = db.insert(TABLE_NAME, null, PurchaseRecordBuilder.deconstruct(record));

                    if (callback != null){
                        callback.onDBUpdate(result >= 0 && success);
                    }
                }catch (SQLiteException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 根据进货日期查询,进货记录
     * @param date
     * @return
     */
    public List<PurchaseRecord> queryByDate(String userId, long date){
        SQLiteDatabase db = getDB(userId);
        Cursor cursor = db.query(TABLE_NAME, null, PurchaseRecordBuilder.DATE + "=?", new String[]{date + ""}, null, null, null);
        List<PurchaseRecord> records = new ArrayList<>(2);
        while(cursor.moveToNext()) {
            PurchaseRecord record = build(userId, cursor);
            records.add(record);
        }
        cursor.close();
        return records;
    }

    /**
     * 根据进货日期查询,进货记录
     * @param date
     * @return
     */
    public void queryByDate(final String userId, final long date, final DBCallback<PurchaseRecord> callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                final List<PurchaseRecord> records = queryByDate(userId, date);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDBCallbck(records);
                    }
                });
            }
        }).start();
    }

    /**
     * 查询所有记录
     */
    public void queryAll(final String userId, final DBCallback<PurchaseRecord> callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                final List<PurchaseRecord> records = queryAll(userId);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDBCallbck(records);
                    }
                });
            }
        }).start();
    }

    public List<PurchaseRecord> queryAll(String userId){
        SQLiteDatabase db = getDB(userId);
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        List<PurchaseRecord> records = new ArrayList<PurchaseRecord>();
        while (cursor.moveToFirst()) {
            PurchaseRecord record = build(userId, cursor);
            records.add(record);
        }
        cursor.close();
        return records;
    }

    private PurchaseRecord build(String userId, Cursor cursor){
        PurchaseRecord record = PurchaseRecordBuilder.build(cursor);
        Goods goods = goodsDAO.query(userId, record.getGoods_id());
        record.setGoods(goods);
        return record;
    }

    public void close(){
        Set<Map.Entry<String, SQLiteDatabase>> set = databaseMap.entrySet();
        Iterator<Map.Entry<String, SQLiteDatabase>> iterator = set.iterator();
        while(iterator.hasNext()){
            Map.Entry<String, SQLiteDatabase> entry = iterator.next();
            entry.getValue().close();
        }
        databaseMap.clear();
        context = null;
        instance = null;
    }

    private SQLiteDatabase getDB(String name){
        SQLiteDatabase db = databaseMap.get(name);
        if (db == null || !db.isOpen()){
            db = context.openOrCreateDatabase(name, Context.MODE_PRIVATE, null);
            db.execSQL(PurchaseRecordBuilder.getCreateSQL());
            databaseMap.put(name, db);
        }
        return db;
    }

    public static class PurchaseRecordBuilder{
        private static final String ID = "_id";
        private static final String USER_ID = "user_id";
        private static final String GOODS_ID = "goods_id";
        private static final String COUNT = "quantity";
        private static final String SUPPLIER = "supplier";
        private static final String PURCHASE_PRICE = "purchase_price";
        private static final String DATE = "date";
        private static final String REMARKS = "remarks";

        public static String getCreateSQL(){
            return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                    + ID + " INTEGER, "
                    + USER_ID + " TEXT, "
                    + GOODS_ID + " TEXT NOT NULL, "
                    + REMARKS + " TEXT, "
                    + SUPPLIER + " TEXT, "
                    + COUNT + " INTEGER NOT NULL,"
                    + PURCHASE_PRICE + " REAL NOT NULL, "
                    + DATE + " TEXT NOT NULL)";
        }

        public static PurchaseRecord build(Cursor cursor){
            PurchaseRecord record = new PurchaseRecord();
            record.setGoods_id(cursor.getString(cursor.getColumnIndex(GOODS_ID)));
            record.user_id = cursor.getString(cursor.getColumnIndex(USER_ID));
            record.quantity = cursor.getInt(cursor.getColumnIndex(COUNT));
            record.supplier = cursor.getString(cursor.getColumnIndex(SUPPLIER));
            record.purchase_price = cursor.getFloat(cursor.getColumnIndex(PURCHASE_PRICE));
            record.date = cursor.getString(cursor.getColumnIndex(DATE));
            record.remarks = cursor.getString(cursor.getColumnIndex(REMARKS));
            return record;
        }

        public static ContentValues deconstruct(PurchaseRecord record){
            ContentValues contentValues = new ContentValues(7);
            contentValues.put(GOODS_ID, record.getGoods_id());
            contentValues.put(USER_ID, record.user_id);
            contentValues.put(COUNT, record.quantity);
            contentValues.put(SUPPLIER, record.supplier);
            contentValues.put(PURCHASE_PRICE, record.purchase_price);
            contentValues.put(DATE, record.date);
            contentValues.put(REMARKS, record.remarks);
            return contentValues;
        }
    }
}
