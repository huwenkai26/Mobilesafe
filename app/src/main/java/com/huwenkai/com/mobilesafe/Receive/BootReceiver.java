package com.huwenkai.com.mobilesafe.Receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.huwenkai.com.mobilesafe.Util.ContactsValues;
import com.huwenkai.com.mobilesafe.Util.LogUtils;
import com.huwenkai.com.mobilesafe.Util.SpUtils;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d("BootReceiver","重新启动");
        String sim_number = SpUtils.getString(ContactsValues.SIM_NUMBER, "", context);
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simSerialNumber = telephonyManager.getSimSerialNumber();
        if(!TextUtils.equals(sim_number,simSerialNumber)){
            SmsManager smsManager = SmsManager.getDefault();
           smsManager.sendTextMessage("5556",null,"sim change",null,null);
        }


    }
}
