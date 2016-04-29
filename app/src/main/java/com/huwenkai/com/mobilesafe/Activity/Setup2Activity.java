package com.huwenkai.com.mobilesafe.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Util.ContactsValues;
import com.huwenkai.com.mobilesafe.Util.LogUtils;
import com.huwenkai.com.mobilesafe.Util.SpUtils;
import com.huwenkai.com.mobilesafe.Util.ToastUtils;
import com.huwenkai.com.mobilesafe.View.SetingItemView;

/**
 * Created by huwenkai on 2016/4/27.
 * 第二个想到界面
 */
public class Setup2Activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initUI();
        findViewById(R.id.bt_nextpage2).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   //读取到sim序列号后才能进入下一页
                   String sim_number = SpUtils.getString(ContactsValues.SIM_NUMBER, "", getApplication());

                   if(!TextUtils.isEmpty(sim_number)){
                       Intent intent = new Intent(Setup2Activity.this, Setup3Activity.class);
                       startActivity(intent);

                       finish();
                       overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);

                   }else {
                       ToastUtils.show(getApplication(),"必须绑定sim卡");
                   }

               }
           }
        );
        findViewById(R.id.bt_prepage_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setup2Activity.this, Setup1Activity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
            }
        });
    }

    private void initUI() {
        final SetingItemView sim_bound = (SetingItemView) findViewById(R.id.siv_bound_sim);
        String number = SpUtils.getString(ContactsValues.SIM_NUMBER, "", getApplicationContext());
        if (TextUtils.isEmpty(number)) {
            sim_bound.setcheak(false);
        } else {
            sim_bound.setcheak(true);
        }
        sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischeck = sim_bound.ischeck();
                sim_bound.setcheak(!ischeck);
                if(!ischeck){
                    //存储绑定的sim卡号码
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String simSerialNumber = telephonyManager.getSimSerialNumber();
                    SpUtils.putString(ContactsValues.SIM_NUMBER,simSerialNumber,getApplicationContext());

                }else{
                    //删除掉存储的节点
                    SpUtils.remove(ContactsValues.SIM_NUMBER,getApplicationContext());
                }
            }
        });

    }
}
