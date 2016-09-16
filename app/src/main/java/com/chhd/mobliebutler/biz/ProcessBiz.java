package com.chhd.mobliebutler.biz;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;

import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.entity.App;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.mobliebutler.global.MyApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CWQ on 2016/8/17.
 */
public class ProcessBiz implements Consts {

    private Context context;
    private Handler handler;

    public ProcessBiz(Context context) {
        this.context = context;
    }

    public ProcessBiz(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public long getAvailMem() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(outInfo);
        return outInfo.availMem;
    }

    public long getTotalMem() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("proc/meminfo")));
            String line = bufferedReader.readLine();
            StringBuilder stringBuilder = new StringBuilder();
            char[] chars = line.toCharArray();
            for (char c : chars) {
                if (c >= '0' && c <= '9') {
                    stringBuilder.append(c);
                }
            }
            return Integer.parseInt(stringBuilder.toString()) * 1024;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void getRunAppProcessInfos() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                List<App> userApps = new ArrayList<>();
                List<App> systemApps = new ArrayList<>();
                long userAppsMem = 0;
                ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                PackageManager packageManager = context.getPackageManager();
                List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = activityManager.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcessInfos) {

                    String packageName = runningAppProcessInfo.processName;

                    Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
                    long memSize = processMemoryInfo[0].getTotalPrivateDirty() * 1024;

                    try {
                        ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);

                        Drawable icon = applicationInfo.loadIcon(packageManager);
                        String name = (String) applicationInfo.loadLabel(packageManager);

                        boolean isSystem;
                        if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                            isSystem = true;
                            systemApps.add(new App(icon, name, packageName, memSize, isSystem, false));
                        } else {
                            isSystem = false;
                            if (MyApplication.packageName.equals(packageName)) {
                                userApps.add(0, new App(icon, name, packageName, memSize, isSystem, false));
                            } else {
                                userApps.add(new App(icon, name, packageName, memSize, isSystem, true));
                                userAppsMem += memSize;
                            }
                        }

                        sleep(30);
                    } catch (Exception e) {
                        Drawable icon = context.getResources().getDrawable(R.mipmap.ic_launcher);
                        String name = packageName;
                        boolean isSystem = true;
                        systemApps.add(new App(icon, name, packageName, memSize, isSystem, false));
                    }
                }
                Message message = Message.obtain();
                message.what = MSG_GET_RUN_APP_PROCESS_INFOS;
                Bundle bundler = new Bundle();
                bundler.putLong(KEY_USER_APPS_MEM, userAppsMem);
                bundler.putSerializable(KEY_USER_APP, (Serializable) userApps);
                bundler.putSerializable(KEY_SYSTEM_APP, (Serializable) systemApps);
                message.setData(bundler);
                handler.sendMessage(message);
            }
        }.start();
    }

    public void killBackgroundProcess(String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses(packageName);
    }

    public void killAllBackgroundProcess() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                PackageManager packageManager = context.getPackageManager();
                List<ApplicationInfo> applicationInfos = packageManager.getInstalledApplications(0);
                for (ApplicationInfo applicationInfo : applicationInfos) {
                    if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM) {
                        activityManager.killBackgroundProcesses(applicationInfo.packageName);
                    }
                }
            }
        }.start();
    }
}
