package com.huwenkai.com.mobilesafe.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Util.ContactsValues;
import com.huwenkai.com.mobilesafe.Util.LogUtils;
import com.huwenkai.com.mobilesafe.Util.SpUtils;
import com.huwenkai.com.mobilesafe.Util.ToastUtils;

public class Setup4Activity extends SetupBaseActivity {

    private CheckBox cb_mobilesafe_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        initUI();

    }

    @Override
    protected void showPrePage() {
        Intent intent = new Intent(Setup4Activity.this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    @Override
    protected void showNextPage() {
        if (cb_mobilesafe_flag.isChecked()) {
            Intent intent = new Intent(Setup4Activity.this, SetupvoerActivity.class);
            startActivity(intent);
            finish();
            SpUtils.putBoolean(ContactsValues.SET_OVER, true, getApplicationContext());
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        } else {
            ToastUtils.show(getApplicationContext(), "请开启防盗保护");
        }


    }

    private void initUI() {
        cb_mobilesafe_flag = (CheckBox) findViewById(R.id.cb_mobilesafe_flag);
        //回显checkbox的状态
        Boolean isopen = SpUtils.getBoolean(ContactsValues.OPENSAFE, false, getApplicationContext());
        if (isopen) {

            cb_mobilesafe_flag.setText("防盗保护已开启");
        } else {
            cb_mobilesafe_flag.setText("防盗保护已关闭");
        }
        cb_mobilesafe_flag.setChecked(isopen);
        cb_mobilesafe_flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                SpUtils.putBoolean(ContactsValues.OPENSAFE, isChecked, getApplicationContext());
                if (isChecked) {
                    cb_mobilesafe_flag.setText("防盗保护已开启");
                } else {
                    cb_mobilesafe_flag.setText("防盗保护已关闭");
                }
            }
        });
        findViewById(R.id.bt_set_over).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_mobilesafe_flag.isChecked()) {
                    Intent intent = new Intent(Setup4Activity.this, SetupvoerActivity.class);
                    startActivity(intent);
                    finish();
                    SpUtils.putBoolean(ContactsValues.SET_OVER, true, getApplicationContext());
                    overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
                } else {
                    ToastUtils.show(getApplicationContext(), "请开启防盗保护");
                }
            }
        });
        findViewById(R.id.bt_prepage_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setup4Activity.this, Setup3Activity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
            }
        });
    }
}
