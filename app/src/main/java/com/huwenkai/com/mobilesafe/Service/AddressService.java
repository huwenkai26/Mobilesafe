package com.huwenkai.com.mobilesafe.Service;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.huwenkai.com.mobilesafe.Engine.AddressDao;
import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Util.ContactsValues;
import com.huwenkai.com.mobilesafe.Util.LogUtils;
import com.huwenkai.com.mobilesafe.Util.SpUtils;

public class AddressService extends Service {
    private String tag = "AddressService";
    private TelephonyManager telephonyManager;
    private MyPhoneStateListener myPhoneStateListener;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private View mView;
    private TextView mTv_toast;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mTv_toast.setText(mAddress);
        }
    };
    private String mAddress;
    private int startX;
    private int startY;
    private int mScreenWidth;
    private int mScreenHeight;


    public AddressService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();

        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mScreenWidth = mWindowManager.getDefaultDisplay().getHeight();
        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        InnerOutCallReceiver innerOutCallReceiver = new InnerOutCallReceiver();
        registerReceiver(innerOutCallReceiver,intentFilter);
    }
     class   InnerOutCallReceiver extends BroadcastReceiver{

         @Override
         public void onReceive(Context context, Intent intent) {
             String phone = getResultData();
             String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
             showToast(phone);
             LogUtils.d("AddressService",phone+"......"+phoneNumber);

         }
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
                    //空闲状态
                    if (mParams != null && mWindowManager != null) {
                        mWindowManager.removeView(mView);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //接起电话时

                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //响铃
                    showToast(incomingNumber);

                    break;

            }
        }
    }

    private void showToast(String incomingNumber) {
        mParams = new WindowManager.LayoutParams();
        final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;

        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        params.gravity = Gravity.LEFT + Gravity.TOP;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mView = View.inflate(this, R.layout.addrss_view, null);
        mTv_toast = (TextView) mView.findViewById(R.id.tv_toast);
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();

                        int disX = moveX-startX;
                        int disY = moveY-startY;

                        params.x = params.x+disX;
                        params.y = params.y+disY;

                        //容错处理
                        if(params.x<0){
                            params.x = 0;
                        }

                        if(params.y<0){
                            params.y=0;
                        }

                        if(params.x>mScreenWidth-mView.getWidth()){
                            params.x = mScreenWidth-mView.getWidth();
                        }

                        if(params.y>mScreenHeight-mView.getHeight()-22){
                            params.y = mScreenHeight-mView.getHeight()-22;
                        }

                        //告知窗体吐司需要按照手势的移动,去做位置的更新
                        mWindowManager.updateViewLayout(mView, params);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                    case MotionEvent.ACTION_UP:
                        SpUtils.putInt(ContactsValues.LOCALTIONX, params.x,getApplicationContext());
                        SpUtils.putInt(ContactsValues.LOCALTIONY, params.y, getApplicationContext());
                        break;
                }
                return false;
            }
        });
        int[] mDrawableID = new int[]{R.drawable.call_locate_white,
                R.drawable.call_locate_orange,
                R.drawable.call_locate_blue,
                R.drawable.call_locate_gray,
                R.drawable.call_locate_green};
        int style = SpUtils.getInt(ContactsValues.ADDRESS_STYLE, 0, getApplicationContext());
        mTv_toast.setBackgroundResource(mDrawableID[style]);
        params.x = SpUtils.getInt(ContactsValues.LOCALTIONX, 0, getApplicationContext());
        params.y =  SpUtils.getInt(ContactsValues.LOCALTIONY, 0, getApplicationContext());
        query(incomingNumber);
        mWindowManager.addView(mView, params);


    }

    private void query(final String incomingNumber) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAddress = AddressDao.getAddress(incomingNumber);
                Message message = Message.obtain();
                mHandler.sendEmptyMessage(0);

            }
        }).start();
    }

    @Override
    public void onDestroy() {
        if (telephonyManager != null && myPhoneStateListener != null) {
            telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
            super.onDestroy();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
