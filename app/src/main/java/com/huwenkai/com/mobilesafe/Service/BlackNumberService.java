package com.huwenkai.com.mobilesafe.Service;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.huwenkai.com.mobilesafe.db.BlacknumberDao.BlackNumberDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 开启服务用来拦截黑名单手机和短信
 */
public class BlackNumberService extends Service {

    private InnerBlackNumberReceive mInnerBlackNumberReceive;
    private IntentFilter mIntentFilter;
    private TelephonyManager telephonyManager;
    private MyPhoneStateListener myPhoneStateListener;
    private MyContentObserver mMyContentObserver;

    public BlackNumberService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
       //拦截短信
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        mIntentFilter.setPriority(1000);
        mInnerBlackNumberReceive = new InnerBlackNumberReceive();
        registerReceiver(mInnerBlackNumberReceive, mIntentFilter);
        //拦截手机
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();

        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

    }
    class InnerBlackNumberReceive extends BroadcastReceiver{


        public void onReceive(Context context, Intent intent) {
            Object[] object = (Object[]) intent.getExtras().get("pdus");
            for (Object obj: object) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String phone = smsMessage.getOriginatingAddress();
                BlackNumberDao blackNumberDao = BlackNumberDao.getInstances(context);
                int mode = blackNumberDao.getMode(phone);

                if (mode==1||mode==3){
                    abortBroadcast();
                }
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mInnerBlackNumberReceive!=null&&mIntentFilter!=null){
            unregisterReceiver(mInnerBlackNumberReceive);
        }
        if(mMyContentObserver!=null){
            getContentResolver().unregisterContentObserver(mMyContentObserver);
        }
        if(myPhoneStateListener!=null){
            telephonyManager.listen(myPhoneStateListener,TelephonyManager.PHONE_TYPE_NONE);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class MyPhoneStateListener extends PhoneStateListener {
        /**
         * Callback invoked when device call state changes.
         *
         * @param state          call state
         * @param incomingNumber incoming call phone number. If application does not have
         *                       {@link Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE} permission, an empty
         *                       string will be passed as an argument.
         * @see TelephonyManager#CALL_STATE_IDLE
         * @see TelephonyManager#CALL_STATE_RINGING
         * @see TelephonyManager#CALL_STATE_OFFHOOK
         */
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //接起电话时

                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //响铃
                    endcall(incomingNumber);



                    break;

            }
        }
    }

    private void deleteDBCalls(String phone) {
        mMyContentObserver = new MyContentObserver(new Handler(), phone);
        getContentResolver().registerContentObserver( Uri.parse("content://call_log/calls"),true,mMyContentObserver);


    }
    class MyContentObserver extends ContentObserver{


        private final String phone;

        public MyContentObserver(Handler handler, String phone) {
            super(handler);
            this.phone = phone;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getContentResolver().delete(
                    Uri.parse("content://call_log/calls"), "number = ?", new String[]{phone});
        }
    }

    private void endcall(String phone) {

        // return ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
        BlackNumberDao blackNumberDao = BlackNumberDao.getInstances(getApplicationContext());
        int mode = blackNumberDao.getMode(phone);
        if(mode==2||mode==3){
            try {
                Class<?> clazz = Class.forName("android.os.ServiceManager");
                Method method = clazz.getMethod("getService", String.class);
                IBinder binder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
                ITelephony iTelephony = ITelephony.Stub.asInterface(binder);
                iTelephony.endCall();


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }


        }

        deleteDBCalls(phone);

    }

}
