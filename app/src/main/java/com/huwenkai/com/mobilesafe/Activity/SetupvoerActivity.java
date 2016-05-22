package com.huwenkai.com.mobilesafe.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Util.ContactsValues;
import com.huwenkai.com.mobilesafe.Util.SpUtils;

/**
 * 设置完成跳转界面
 * Created by huwenkai on 2016/4/27.
 */
public class SetupvoerActivity extends Activity{

    private TextView tv_reset_setup;
    private TextView tv_phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean set_over = SpUtils.getBoolean(ContactsValues.SET_OVER, false, this);
        if(set_over){
            //进入设置完成界面
            setContentView(R.layout.activity_setover);
            initUI();

        }else{
            //进入设置向导界面
            Intent intent = new Intent(this,Setup1Activity.class);
            startActivity(intent);
            finish();
        }

    }

    private void initUI() {
        tv_phone_number = (TextView) findViewById(R.id.tv_phone_number);
        String phone_number = SpUtils.getString(ContactsValues.PHONE_MUNBER, "", getApplicationContext());

        tv_phone_number.setText(phone_number);
        tv_reset_setup= (TextView) findViewById(R.id.tv_reset_setup);
        tv_reset_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetupvoerActivity.this,Setup1Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
