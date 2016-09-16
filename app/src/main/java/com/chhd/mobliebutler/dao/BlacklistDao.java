package com.chhd.mobliebutler.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chhd.mobliebutler.db.BlacklistDB;
import com.chhd.mobliebutler.entity.Blacklist;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CWQ on 2016/9/3.
 */
public class BlacklistDao implements Consts {

    private Context context;
    private BlacklistDB blacklistDB;

    public BlacklistDao(Context context) {
        this.context = context;
        blacklistDB = new BlacklistDB(context);
    }

    public long insert(String number, String name, int mode) {
        SQLiteDatabase sqLiteDatabase = blacklistDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NUMBER, number);
        values.put(KEY_NAME, name);
        values.put(KEY_MODE, mode);
        long id = sqLiteDatabase.insert(TABLE_BLACKLIST, "", values);
        sqLiteDatabase.close();
        return id;
    }

    public void delete(String number) {
        SQLiteDatabase sqLiteDatabase = blacklistDB.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_BLACKLIST, "number = ?", new String[]{number});
        sqLiteDatabase.close();
    }

    public void update(String number, String name, int mode) {
        SQLiteDatabase sqLiteDatabase = blacklistDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NUMBER, number);
        values.put(KEY_NAME, name);
        values.put(KEY_MODE, mode);
        sqLiteDatabase.update(TABLE_BLACKLIST, values, "number = ?", new String[]{number});
        sqLiteDatabase.close();
    }

    public List<Blacklist> query() {
        List<Blacklist> blacklists = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = blacklistDB.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_BLACKLIST, new String[]{KEY_NUMBER, KEY_NAME, KEY_MODE}, null, null, null, null, "_id desc");
        while (cursor.moveToNext()) {
            String number = cursor.getString(0);
            String name = cursor.getString(1);
            int mode = cursor.getInt(2);
            blacklists.add(new Blacklist(number, name, mode));
        }
        cursor.close();
        sqLiteDatabase.close();
        return blacklists;
    }
}
