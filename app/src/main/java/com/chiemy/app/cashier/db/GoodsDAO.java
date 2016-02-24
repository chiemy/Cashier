package com.chiemy.app.cashier.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import com.chiemy.app.cashier.bean.Goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by chiemy on 16/2/21.
 */
public class GoodsDAO {
    public static final String TABLE_NAME = "goods";

    private static GoodsDAO dao;
    private Context context;
    private Handler handler;


    private Map<String, SQLiteDatabase> databaseMap = new HashMap<>(1);

    private GoodsDAO(Context context) {
        this.context = context;
        handler = new Handler(Looper.getMainLooper());
    }

    public static GoodsDAO getInstance(Context context) {
        if (dao == null) {
            synchronized (GoodsDAO.class) {
                dao = new GoodsDAO(context.getApplicationContext());
            }
        }
        return dao;
    }

    public void insertIfNotExist(final Goods goods) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SQLiteDatabase db = getDB(goods.user_id);
                    Goods temp = query(goods.user_id, goods.barcode);
                    if (temp == null) {
                        db.insert(TABLE_NAME, null, GoodsBuilder.deconstruct(goods));
                    }
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void insertOrUpdate(final Goods goods, final DBUpdateCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean result = insertOrUpdate(goods);
                    if (callback != null) {
                        callback.onDBUpdate(result);
                    }
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean insertOrUpdate(final Goods goods) {
        SQLiteDatabase db = getDB(goods.user_id);
        Goods temp = query(goods.user_id, goods.barcode);
        long result = -1;
        if (temp == null) {
            result = db.insert(TABLE_NAME, null, GoodsBuilder.deconstruct(goods));
            return result >= 0;
        } else {
            ContentValues contentValues = GoodsBuilder.deconstruct(goods);
            result = db.update(TABLE_NAME, contentValues, GoodsBuilder.BARCODE + "=?", new String[]{goods.barcode});
            return result > 0;
        }
    }

    /**
     * @param userid  用户id
     * @param barcode 商品条码
     * @return
     */
    public Goods query(String userid, String barcode) {
        SQLiteDatabase db = getDB(userid);
        Cursor cursor = db.query(TABLE_NAME, null, GoodsBuilder.BARCODE + "=?", new String[]{barcode}, null, null, null);
        Goods goods = null;
        if (cursor.moveToFirst()) {
            goods = GoodsBuilder.build(cursor);
        }
        cursor.close();
        return goods;
    }

    /**
     * @param userid
     * @param barcode
     * @param callback
     */
    public void query(final String userid, final String barcode, final DBCallback<Goods> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                Goods goods = query(userid, barcode);
                final List<Goods> goodses = new ArrayList<Goods>(1);
                goodses.add(goods);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDBCallbck(goodses);
                    }
                });
            }
        }).start();
    }

    /**
     * @param userid
     * @param callback
     */
    public void queryAll(final String userid, final DBCallback<Goods> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                final List<Goods> goodses = queryAll(userid);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDBCallbck(goodses);
                    }
                });
            }
        }).start();
    }

    public List<Goods> queryAll(final String userid) {
        SQLiteDatabase db = getDB(userid);
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        List<Goods> goodses = new ArrayList<>(10);
        while (cursor.moveToNext()) {
            Goods goods = GoodsBuilder.build(cursor);
            goodses.add(goods);
        }
        cursor.close();
        return goodses;
    }

    public int queryQuantity(Goods goods) {
        SQLiteDatabase db = getDB(goods.user_id);
        Cursor cursor = db.query(TABLE_NAME, new String[]{GoodsBuilder.QUANTITY},
                GoodsBuilder.BARCODE + "=?", new String[]{goods.barcode}, null, null, null);
        int quantity = 0;
        if (cursor.moveToNext()) {
            quantity = cursor.getInt(cursor.getColumnIndex(GoodsBuilder.QUANTITY));
        }
        cursor.close();
        return quantity;
    }

    public void close() {
        Set<Map.Entry<String, SQLiteDatabase>> set = databaseMap.entrySet();
        Iterator<Map.Entry<String, SQLiteDatabase>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, SQLiteDatabase> entry = iterator.next();
            entry.getValue().close();
        }
        databaseMap.clear();
        context = null;
        dao = null;
    }

    private SQLiteDatabase getDB(String name) {
        SQLiteDatabase db = databaseMap.get(name);
        if (db == null || !db.isOpen()) {
            db = context.openOrCreateDatabase(name, Context.MODE_PRIVATE, null);
            db.execSQL(GoodsBuilder.getCreateSQL());
            databaseMap.put(name, db);
        }
        return db;
    }


    public static class GoodsBuilder {
        private static final String ID = "_id";
        private static final String USER_ID = "user_id";
        private static final String BARCODE = "barcode";
        private static final String NAME = "name";
        private static final String SUPPLIER = "supplier";
        private static final String PURCHASE_PRICE = "purchase_price";
        private static final String SELLING_PRICE = "selling_price";
        private static final String UPDATE_STATE = "update_state";
        private static final String QUANTITY = "quantity";

        public static String getCreateSQL() {
            return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                    + ID + " INTEGER, "
                    + USER_ID + " TEXT, "
                    + BARCODE + " TEXT primary key, "
                    + QUANTITY + " INTEGER NOT NULL,"
                    + NAME + " TEXT NOT NULL, "
                    + SUPPLIER + " TEXT, "
                    + UPDATE_STATE + " INTEGER NOT NULL,"
                    + PURCHASE_PRICE + " REAL NOT NULL, "
                    + SELLING_PRICE + " REAL NOT NULL)";
        }

        public static Goods build(Cursor cursor) {
            Goods goods = new Goods();
            goods.user_id = cursor.getString(cursor.getColumnIndex(USER_ID));
            goods.barcode = cursor.getString(cursor.getColumnIndex(BARCODE));
            goods.name = cursor.getString(cursor.getColumnIndex(NAME));
            goods.purchase_price = cursor.getFloat(cursor.getColumnIndex(PURCHASE_PRICE));
            goods.selling_price = cursor.getFloat(cursor.getColumnIndex(SELLING_PRICE));
            goods.supplier = cursor.getString(cursor.getColumnIndex(SUPPLIER));
            goods.update_state = cursor.getInt(cursor.getColumnIndex(UPDATE_STATE));
            goods.quantity = cursor.getInt(cursor.getColumnIndex(QUANTITY));
            return goods;
        }

        public static ContentValues deconstruct(Goods goods) {
            ContentValues contentValues = new ContentValues(8);
            contentValues.put(USER_ID, goods.user_id);
            contentValues.put(BARCODE, goods.barcode);
            contentValues.put(NAME, goods.name);
            contentValues.put(PURCHASE_PRICE, goods.purchase_price);
            contentValues.put(SELLING_PRICE, goods.selling_price);
            contentValues.put(SUPPLIER, goods.supplier);
            contentValues.put(UPDATE_STATE, goods.update_state);
            contentValues.put(QUANTITY, goods.quantity);
            return contentValues;
        }
    }
}
