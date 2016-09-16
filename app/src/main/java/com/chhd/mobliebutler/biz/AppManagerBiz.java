package com.chhd.mobliebutler.biz;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;

import com.chhd.mobliebutler.entity.App;
import com.chhd.mobliebutler.global.Consts;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CWQ on 2016/8/26.
 */
public class AppManagerBiz implements Consts {

    private Context context;
    private Handler handler;
    private PackageManager packageManager;
    private int appsSize;
    private int num;
    private List<App> userApps = new ArrayList<>();
    private List<App> systemApps = new ArrayList<>();
    IPackageStatsObserver.Stub statsObserver = new IPackageStatsObserver.Stub() {
        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            try {
                num++;
                long romSize = pStats.cacheSize + pStats.dataSize + pStats.codeSize;
                String packageName = pStats.packageName;
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                Drawable icon = applicationInfo.loadIcon(packageManager);
                String name = (String) applicationInfo.loadLabel(packageManager);
                App app = new App();
                app.setIcon(icon);
                app.setName(name);
                app.setPackageName(packageName);
                app.setRomSize(romSize);
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                    app.setIsSystem(true);
                    systemApps.add(app);
                } else {
                    app.setIsSystem(false);
                    userApps.add(app);
                }
                if (num == appsSize) {
                    Message message = Message.obtain();
                    message.what = MSG_GET_ALL_APPS;
                    Bundle data = new Bundle();
                    data.putSerializable(KEY_USER_APP, (Serializable) userApps);
                    data.putSerializable(KEY_SYSTEM_APP, (Serializable) systemApps);
                    message.setData(data);
                    handler.sendMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public AppManagerBiz(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void getAllApp() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    packageManager = context.getPackageManager();
                    List<ApplicationInfo> applicationInfos = packageManager.getInstalledApplications(0);
                    appsSize = applicationInfos.size();
                    for (ApplicationInfo applicationInfo : applicationInfos) {
                        String packageName = applicationInfo.packageName;
                        getAppRomInfo(packageName);
                        sleep(10);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void getAppRomInfo(String packageName) {
        try {
            Class<?> clazz = Class.forName("android.content.pm.PackageManager");
            Method method = clazz.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            method.invoke(packageManager, packageName, statsObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
