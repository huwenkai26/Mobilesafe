package com.huwenkai.com.mobilesafe.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Util.ContactsValues;
import com.huwenkai.com.mobilesafe.Util.SpUtils;
import com.huwenkai.com.mobilesafe.Util.ToastUtils;

public class Toastloacltionactivity extends Activity {

    private Button bt_top;
    private ImageView iv_drag;
    private Button bt_buttom;
    private int startX;
    private int startY;
    private int mScreenWidth;
    private int mScreenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toastloacltionactivity);
        initUI();
    }

    private void initUI() {
        iv_drag = (ImageView) findViewById(R.id.iv_drag);
        bt_top = (Button) findViewById(R.id.bt_top);
        bt_buttom = (Button) findViewById(R.id.bt_buttom);
        WindowManager mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
        mScreenHeight = mWM.getDefaultDisplay().getHeight();
        mScreenWidth = mWM.getDefaultDisplay().getWidth();

        final int localtionx = SpUtils.getInt(ContactsValues.LOCALTIONX, 0, getApplicationContext());
        final int localtiony = SpUtils.getInt(ContactsValues.LOCALTIONY, 0, getApplicationContext());
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = localtionx;
        layoutParams.topMargin = localtiony;
        iv_drag.setLayoutParams(layoutParams);
        if (localtiony>mScreenHeight/2){
            bt_buttom.setVisibility(View.INVISIBLE);
            bt_top.setVisibility(View.VISIBLE);
        }else{
            bt_buttom.setVisibility(View.VISIBLE);
            bt_top.setVisibility(View.INVISIBLE);
        }

        iv_drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();
                        int MoX = endX - startX;
                        int MoY = endY - startY;
                        int left = iv_drag.getLeft() + MoX;
                        int right = iv_drag.getRight() + MoX;
                        int top = iv_drag.getTop() + MoY;
                        int bottom = iv_drag.getBottom() + MoY;

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        //容错处理(iv_drag不能拖拽出手机屏幕)
                        //左边缘不能超出屏幕
                        if (left < 0 || right > mScreenWidth || top < 0
                                || bottom > mScreenHeight - 20) {
                            break;
                        }
                        if (top>mScreenHeight/2){
                            bt_buttom.setVisibility(View.INVISIBLE);
                            bt_top.setVisibility(View.VISIBLE);
                        }else{
                            bt_buttom.setVisibility(View.VISIBLE);
                            bt_top.setVisibility(View.INVISIBLE);
                        }
                        iv_drag.layout(left, top, right, bottom);
                        break;
                    case MotionEvent.ACTION_UP:
                        SpUtils.putInt(ContactsValues.LOCALTIONX,iv_drag.getLeft() , getApplicationContext());
                        SpUtils.putInt(ContactsValues.LOCALTIONY, iv_drag.getTop(), getApplicationContext());
                        break;
                }
                return false;

            }
        });

        //双击居中
        iv_drag.setOnClickListener(new View.OnClickListener() {

            long[] mHits = new long[2];
            public void onClick(View v) {


                    // 双击事件响应
                    /**
                     * arraycopy,拷贝数组
                     * src 要拷贝的源数组
                     * srcPos 源数组开始拷贝的下标位置
                     * dst 目标数组
                     * dstPos 开始存放的下标位置
                     * length 要拷贝的长度（元素的个数）
                     *
                     */
                    //实现数组的移位操作，点击一次，左移一位，末尾补上当前开机时间（cpu的时间）
                    System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                    mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                    //双击事件的时间间隔500ms
                    if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                        //双击后具体的操作
                        layoutParams.leftMargin = mScreenWidth/2-iv_drag.getWidth()/2;
                        layoutParams.topMargin = mScreenHeight/2-iv_drag.getHeight()/2;
                        iv_drag.setLayoutParams(layoutParams);
                        SpUtils.putInt(ContactsValues.LOCALTIONX,iv_drag.getLeft() , getApplicationContext());
                        SpUtils.putInt(ContactsValues.LOCALTIONY, iv_drag.getTop(), getApplicationContext());
                    }
                }


        });

    }
}

