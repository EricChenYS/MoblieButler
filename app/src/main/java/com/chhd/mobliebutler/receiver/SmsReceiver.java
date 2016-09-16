package com.chhd.mobliebutler.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.chhd.mobliebutler.R;
import com.chhd.mobliebutler.global.Consts;
import com.chhd.mobliebutler.service.LocationService;
import com.chhd.util.SpUtils;
import com.chhd.util.ToastUtils;

/**
 * Created by CWQ on 2016/8/31.
 */
public class SmsReceiver extends BroadcastReceiver implements Consts {

    private static MediaPlayer mediaPlayer;
    private static Intent service;
    private static ComponentName componentName;
    private static DevicePolicyManager devicePolicyManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SpUtils.getBoolean(context, KEY_IS_OPEN_GUARD_AGAINST_THEFT, false) == true) {
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for (Object object : objects) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);
                String number = smsMessage.getOriginatingAddress();
                String messageBody = smsMessage.getMessageBody();
                if (messageBody.contains("#*alarm*#")) {
                    if (mediaPlayer == null) {
                        mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                    }
                } else if (messageBody.contains("#*stopalarm*#")) {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                } else if (messageBody.contains("#*location*#")) {
                    if (service == null) {
                        service = new Intent(context, LocationService.class);
                        service.putExtra(KEY_NUMBER, number);
                        context.startService(service);
                    }
                } else if (messageBody.contains("#*stoplocation*#")) {
                    if (service != null) {
                        context.stopService(service);
                        service = null;
                    }
                } else if (messageBody.contains("#*lock*#")) {
                    if (devicePolicyManager == null && componentName == null) {
                        devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                        componentName = new ComponentName(context, MyDeviceAdminReceiver.class);
                    }
                    if (devicePolicyManager.isAdminActive(componentName)) {
                        String pwd = SpUtils.getString(context, KEY_GUARD_AGAINST_THEFT_PWD, null);
                        devicePolicyManager.lockNow();
                        devicePolicyManager.resetPassword(pwd, 0);
                    }
                } else if (messageBody.contains("#*clear*#")) {
                    if (devicePolicyManager == null && componentName == null) {
                        devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                        componentName = new ComponentName(context, MyDeviceAdminReceiver.class);
                    }
                    if (devicePolicyManager.isAdminActive(componentName)) {
                        ToastUtils.makeText(context, "数据无价，谨慎操作");
                    }
                }
            }
        }

    }
}
