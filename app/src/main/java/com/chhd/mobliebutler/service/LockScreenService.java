package com.chhd.mobliebutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.chhd.mobliebutler.biz.ProcessBiz;

/**
 * Created by CWQ on 2016/9/2.
 */
public class LockScreenService extends Service {

    private BroadcastReceiver receiver;
    private ProcessBiz biz;

    @Override
    public void onCreate() {
        super.onCreate();

        receiver = new InnerReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, filter);

        biz = new ProcessBiz(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            biz.killAllBackgroundProcess();
        }
    }
}
