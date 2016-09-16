package com.chhd.mobliebutler.biz;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;

import com.chhd.mobliebutler.entity.Calllog;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CWQ on 2016/9/5.
 */
public class CalllogBiz implements Consts {

    private Context context;
    private Handler handler;

    public CalllogBiz(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void getCalllogs() {
        List<Calllog> calllogs = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, new String[]{CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.CACHED_NAME}, null, null, CallLog.Calls.DATE+" desc");
        while (cursor.moveToNext()) {
            String number = cursor.getString(0);
            long date = cursor.getLong(1);
            int type = cursor.getInt(2);
            String name = cursor.getString(3);
            calllogs.add(new Calllog(number, date, type, name));
        }
        cursor.close();
        Message message = Message.obtain();
        message.what = MSG_GET_CALLLOGS;
        message.obj = calllogs;
        handler.sendMessage(message);
    }
}
