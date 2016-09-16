package com.chhd.mobliebutler.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by CWQ on 2016/9/2.
 */
public class ServiceUitls {

    public static boolean isRunning(Context context, String serviceName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServiceInfos) {
            if (serviceName.equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
