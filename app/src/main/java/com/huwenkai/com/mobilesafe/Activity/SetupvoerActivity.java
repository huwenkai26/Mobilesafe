package com.huwenkai.com.mobilesafe.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Util.ContactsValues;
import com.huwenkai.com.mobilesafe.Util.SpUtils;

/**
 * 设置完成跳转界面
 * Created by huwenkai on 2016/4/27.
 */
public class SetupvoerActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean set_over = SpUtils.getBoolean(ContactsValues.SET_OVER, false, this);
        if(set_over){
            //进入设置完成界面
            setContentView(R.layout.activity_setover);

        }else{
            //进入设置向导界面
            Intent intent = new Intent(this,Setup1Activity.class);
            startActivity(intent);
            finish();
        }

    }
}
