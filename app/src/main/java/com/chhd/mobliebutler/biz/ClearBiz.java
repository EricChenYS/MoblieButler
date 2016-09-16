package com.chhd.mobliebutler.biz;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.RemoteException;

import com.chhd.mobliebutler.entity.App;
import com.chhd.mobliebutler.entity.Group;
import com.chhd.mobliebutler.global.Consts;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CWQ on 2016/8/9.
 */
public class ClearBiz implements Consts {
    IPackageDataObserver.Stub dataObserver = new IPackageDataObserver.Stub() {
        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
        }
    };
    private Context context;
    private Handler handler;
    private PackageManager packageManager;
    private List<Group> groups = new ArrayList<>();
    private List<App> userApps = new ArrayList<>();
    private List<App> systemApps = new ArrayList<>();
    private long totalCacheSize;
    private long userCacheSize;
    private long systemCacheSize;
    private int count;
    private int applicationInfosSize;
    IPackageStatsObserver.Stub statsObserver = new IPackageStatsObserver.Stub() {
        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(pStats.packageName, 0);
                ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                Drawable icon = applicationInfo.loadIcon(packageManager);
                String name = (String) applicationInfo.loadLabel(packageManager);
                if (pStats.cacheSize > 0) {
                    boolean isSystem;
                    if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                        isSystem = true;
                        systemApps.add(new App(icon, name, pStats.packageName, pStats.cacheSize, isSystem));
                        systemCacheSize += pStats.cacheSize;
                    } else {
                        isSystem = false;
                        userApps.add(new App(icon, name, pStats.packageName, pStats.cacheSize, isSystem));
                        userCacheSize += pStats.cacheSize;
                    }

                    totalCacheSize += pStats.cacheSize;
                }

                groups.clear();
                groups.add(new Group(KEY_USER_APP, userApps, userCacheSize, 0));
                groups.add(new Group(KEY_SYSTEM_APP, systemApps, 0, systemCacheSize));

                Message message = Message.obtain();
                message.what = MSG_SCANNIG;
                Bundle data = new Bundle();
                data.putLong(KEY_TOTAL_CACHESIZE, totalCacheSize);
                data.putString(KEY_NAME, name);
                data.putParcelableArrayList(KEY_GROUPS, (ArrayList<? extends Parcelable>) groups);
                message.setData(data);
                handler.sendMessage(message);

                count++;
                if (count == applicationInfosSize) {
                    synchronized (ClearBiz.this) {
                        ClearBiz.this.notify();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private boolean mIsRunning;

    public ClearBiz(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void scan() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    packageManager = context.getPackageManager();
                    List<ApplicationInfo> applicationInfos = packageManager.getInstalledApplications(0);
                    applicationInfosSize = applicationInfos.size();
                    Message message = Message.obtain();
                    message.what = MSG_PROGRESSBAR_SET_MAX;
                    message.obj = applicationInfosSize;
                    handler.sendMessage(message);
                    for (int i = 0; i < applicationInfosSize; i++) {
                        ApplicationInfo applicationInfo = applicationInfos.get(i);
                        String packageName = applicationInfo.packageName;
                        getAppCacheInfo(packageName);
                        if (i != applicationInfosSize - 1) {
                            sleep(50);
                        }
                    }
                    synchronized (ClearBiz.this) {
                        ClearBiz.this.wait();
                    }
                    groups.clear();
                    groups.add(new Group(KEY_USER_APP, userApps, userCacheSize, 0));
                    groups.add(new Group(KEY_SYSTEM_APP, systemApps, 0, systemCacheSize));
                    message = Message.obtain();
                    message.what = MSG_SCAN_FINISH;
                    message.obj = groups;
                    handler.sendMessage(message);
                    mIsRunning = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void getAppCacheInfo(String packageName) {
        try {
            Class<?> clazz = Class.forName("android.content.pm.PackageManager");
            Method method = clazz.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            method.invoke(packageManager, packageName, statsObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updataTotalDataSiz() {

        mIsRunning = true;

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    while (mIsRunning) {
                        handler.sendEmptyMessage(MSG_UPDATE_TOTAL_CACHESIZE);
                        sleep(500);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void clear() {
        try {
            PackageManager packageManager = context.getPackageManager();
            Class<?> clazz = Class.forName("android.content.pm.PackageManager");
            Method method = clazz.getMethod("freeStorageAndNotify", long.class, IPackageDataObserver.class);
            method.invoke(packageManager, Long.MAX_VALUE, dataObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
