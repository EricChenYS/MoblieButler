package com.chhd.mobliebutler.biz;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.chhd.mobliebutler.dao.VirusDao;
import com.chhd.mobliebutler.entity.App;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.util.Md5Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CWQ on 2016/8/12.
 */
public class AntiVirusBiz implements Consts {

    private Context context;
    private Handler handler;

    public AntiVirusBiz(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void initWaveView(final int progress, final int j) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    for (int i = progress; i > j; i--) {
                        Message message = Message.obtain();
                        message.what = MSG_WAVE_VIEW_SET_PROGRESS;
                        message.arg1 = i - 1;
                        handler.sendMessage(message);
                        sleep(500 / progress);
                    }
                    synchronized (AntiVirusBiz.this) {
                        AntiVirusBiz.this.notify();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void scan() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    synchronized (AntiVirusBiz.this) {
                        AntiVirusBiz.this.wait();
                    }

                    List<App> safeApps = new ArrayList<>();
                    List<App> dangerApps = new ArrayList<>();

                    VirusDao virusDao = new VirusDao(context);
                    List<String> virus = virusDao.getVirus();

                    PackageManager packageManager = context.getPackageManager();
                    List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES);
                    Message message = Message.obtain();
                    message.what = MSG_WAVE_VIEW_SET_MAX;
                    message.arg1 = packageInfos.size();
                    handler.sendMessage(message);
                    int i = 0;
                    for (PackageInfo packageInfo : packageInfos) {
                        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                        Drawable icon = applicationInfo.loadIcon(packageManager);
                        String name = (String) applicationInfo.loadLabel(packageManager);
                        String packageName = applicationInfo.packageName;

                        Signature[] signatures = packageInfo.signatures;
                        Signature signature = signatures[0];
                        String md5 = Md5Utils.encoder(packageName);

                        App app = null;

                        boolean isVirus;
                        if (virus.contains(md5)) {
                            isVirus = true;
                            app = new App(icon, name, packageName, isVirus);
                            dangerApps.add(app);
                            handler.sendEmptyMessage(MSG_FIND_VIRUS);
                        } else {
                            isVirus = false;
                            app = new App(icon, name, packageName, isVirus);
                            safeApps.add(app);
                        }

                        message = Message.obtain();
                        message.what = MSG_SCANNIG;
                        message.obj = app;
                        handler.sendMessage(message);
                        message = Message.obtain();
                        i++;
                        message.what = MSG_WAVE_VIEW_SET_PROGRESS;
                        message.arg1 = i;
                        handler.sendMessage(message);
                        sleep(50);
                    }
                    message = Message.obtain();
                    message.what = MSG_SCAN_FINISH;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(KEY_DANGER_APPS, (Serializable) dangerApps);
                    bundle.putSerializable(KEY_SAFE_APPS, (Serializable) safeApps);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
