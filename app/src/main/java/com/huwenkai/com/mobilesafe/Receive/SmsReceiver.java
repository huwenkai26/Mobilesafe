package com.huwenkai.com.mobilesafe.Receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Service.GPSService;
import com.huwenkai.com.mobilesafe.Util.ContactsValues;
import com.huwenkai.com.mobilesafe.Util.SpUtils;

public class SmsReceiver extends BroadcastReceiver {
    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Boolean opensafe = SpUtils.getBoolean(ContactsValues.OPENSAFE, false, context);
        if(opensafe){
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for (Object object: objects) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
                String originatingAddress = message.getOriginatingAddress();
                String messageBody = message.getMessageBody();
                if(messageBody.contains("#*alarm*#")){
                    MediaPlayer mediaPlayer = MediaPlayer.create(context,R.raw.ylzs);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
                if(messageBody.contains("#*location*#")){
                   Intent intent1 = new Intent(context ,GPSService.class);
                    context.startService(intent1);
                }
                if(messageBody.contains("#*wipedata*#")){

                }
                if(messageBody.contains("#*lockscreen*")){

                }


            }
        }

    }
}
