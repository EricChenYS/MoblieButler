package com.chhd.mobliebutler.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CWQ on 2016/9/3.
 */
public class BlacklistDB extends SQLiteOpenHelper {

    public BlacklistDB(Context context) {
        super(context, "blacklist.db", null, 1);
    }

    public BlacklistDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table blacklist(_id integer primary key autoincrement,number text unique,name text,mode integer)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
