package com.chhd.mobliebutler.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.chhd.mobliebutler.global.Consts;
import com.chhd.util.SpUtils;

/**
 * Created by CWQ on 2016/8/30.
 */
public class BootReceiver extends BroadcastReceiver implements Consts {

    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simSerialNumber = telephonyManager.getSimSerialNumber();
        if (simSerialNumber != null && !simSerialNumber.equals(SpUtils.getString(context, KEY_SIM_SERIAL_NUMBER, null)) && SpUtils.getBoolean(context, KEY_IS_OPEN_GUARD_AGAINST_THEFT, false) == true) {
            SmsManager smsManager = SmsManager.getDefault();
            String number = SpUtils.getString(context, KEY_NUMBER, null);
            smsManager.sendTextMessage(number, null, "SIM卡发生变化", null, null);
        }
    }
}
