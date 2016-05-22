package com.huwenkai.com.mobilesafe.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Service.LockscreenService;
import com.huwenkai.com.mobilesafe.Util.ContactsValues;
import com.huwenkai.com.mobilesafe.Util.ServiceUtils;
import com.huwenkai.com.mobilesafe.Util.SpUtils;

public class ProcessSettingActivity extends AppCompatActivity {

    private CheckBox mCb_show_system;
    private CheckBox mCb_lockscreen_clean;
    private CheckBox cb_lockscreen_clean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_setting);
        initshowsystemUI();
        initLockscreenClean();
    }

    private void initLockscreenClean() {
        cb_lockscreen_clean = (CheckBox) findViewById(R.id.cb_lockscreen_clean);
        boolean isRunning = ServiceUtils.IsRunning("com.huwenkai.com.mobilesafe.Service.LockscreenService", getApplicationContext());
        cb_lockscreen_clean.setChecked(isRunning);
        if(isRunning){
            cb_lockscreen_clean.setText("锁屏清理已关闭");
        }else {
            cb_lockscreen_clean.setText("锁屏清理已开启");
        }
        cb_lockscreen_clean.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb_lockscreen_clean.setText("锁屏清理已关闭");
                    startService(new Intent(getApplicationContext(),LockscreenService.class));
                }else {
                    cb_lockscreen_clean.setText("锁屏清理已开启");
                    stopService(new Intent(getApplicationContext(),LockscreenService.class));
                }

            }
        });
    }

    private void initshowsystemUI() {
        mCb_show_system = (CheckBox) findViewById(R.id.cb_show_system);

        Boolean isShow = SpUtils.getBoolean(ContactsValues.SHOW_SYSTEM, false, getApplicationContext());
        mCb_show_system.setChecked(isShow);
        if(isShow){
            mCb_show_system.setText("显示系统进程");
        }else {
            mCb_show_system.setText("隐藏系统进程");
        }
        mCb_show_system.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mCb_show_system.setText("显示系统进程");
                }else {
                    mCb_show_system.setText("隐藏系统进程");
                }
                SpUtils.putBoolean(ContactsValues.SHOW_SYSTEM,isChecked,getApplicationContext());
            }
        });
    }
}
