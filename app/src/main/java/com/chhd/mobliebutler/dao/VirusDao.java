package com.chhd.mobliebutler.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chhd.mobliebutler.global.Consts;
import com.chhd.util.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CWQ on 2016/8/12.
 */
public class VirusDao implements Consts {

    private Context context;

    public VirusDao(Context context) {
        this.context = context;
    }

    public List<String> getVirus() {
        List<String> virus = new ArrayList<>();
        String path = context.getFilesDir().getAbsolutePath() + File.separator + ANTIVIRUS_DB;
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(path, SQLiteDatabase.OPEN_READONLY, null);
        Cursor cursor = sqLiteDatabase.query("datable", new String[]{"md5"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            virus.add(cursor.getString(0));
        }
        return virus;
    }
}
