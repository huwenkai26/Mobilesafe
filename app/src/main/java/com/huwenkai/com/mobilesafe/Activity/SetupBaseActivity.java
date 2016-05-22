package com.huwenkai.com.mobilesafe.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * 所有设置向导的父类
 * Created by huwenkai on 2016/4/29.
 */
public abstract class SetupBaseActivity extends Activity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //相应下一页的事件
//相应上一页的事件
        gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e1.getX()>e2.getX()){
                    //相应下一页的事件
                   showNextPage();
                }else {
                    //相应上一页的事件
                    showPrePage();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    protected abstract void showPrePage();

    protected abstract void showNextPage();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
