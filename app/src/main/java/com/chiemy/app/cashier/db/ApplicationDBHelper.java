package com.chiemy.app.cashier.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chiemy on 16/2/20.
 */
public class ApplicationDBHelper extends SQLiteOpenHelper{
    public static final String DB_NAME = "cashier.db";
    private static final int VERSION = 1;

    public ApplicationDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GoodsDAO.GoodsBuilder.getCreateSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
