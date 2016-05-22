package com.huwenkai.com.mobilesafe.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Service.AddressService;
import com.huwenkai.com.mobilesafe.Service.BlackNumberService;
import com.huwenkai.com.mobilesafe.Util.ContactsValues;
import com.huwenkai.com.mobilesafe.Util.ServiceUtils;
import com.huwenkai.com.mobilesafe.Util.SpUtils;
import com.huwenkai.com.mobilesafe.View.AddressStyleView;
import com.huwenkai.com.mobilesafe.View.SetingItemView;

/**
 * 设置界面的activity
 * Created by huwenkai on 2016/4/24.
 */
public class SettingsActivity extends Activity {

    private String[] mStyles;
    private int styleid;
    private AddressStyleView asv_style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initupdata();
        initAddress();
        initAddressStyle();
        initaddressloaction();
        initblacknumber();
    }

    private void initblacknumber() {
        final SetingItemView siv_blacknumber = (SetingItemView) findViewById(R.id.siv_blacknumber);
        boolean isRunning = ServiceUtils.IsRunning("com.huwenkai.com.mobilesafe.Service.BlackNumberService", getApplicationContext());
        siv_blacknumber.setCheak(isRunning);
        siv_blacknumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischeck = siv_blacknumber.isCheck();
                siv_blacknumber.setCheak(!ischeck);
                if(!ischeck){
                    Intent intent = new Intent(getApplicationContext(), BlackNumberService.class);
                    startService(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), BlackNumberService.class);
                    stopService(intent);
                }
            }
        });
    }

    private void initaddressloaction() {
        AddressStyleView asv_loacltion = (AddressStyleView) findViewById(R.id.asv_loacltion);
        asv_loacltion.setTitle("归属地提示框位置");
        asv_loacltion.setDec("设置归属地提示框位置");
        asv_loacltion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Toastloacltionactivity.class);
                startActivity(intent);
            }
        });

    }

    private void initAddressStyle() {
        asv_style = (AddressStyleView) findViewById(R.id.asv_style);
        mStyles = new String[]{"透明", "橙色", "蓝色", "灰色", "绿色"};
        styleid = SpUtils.getInt(ContactsValues.ADDRESS_STYLE, 0, getApplicationContext());
        asv_style.setTitle("设置归属地显示风格");
        asv_style.setDec(mStyles[styleid]);
        asv_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


    }

    private void showDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("选择归属地样式");
        builder.setSingleChoiceItems(mStyles, styleid, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                asv_style.setDec(mStyles[which]);
                dialog.dismiss();
                styleid = which;
                SpUtils.putInt(ContactsValues.ADDRESS_STYLE,which,getApplicationContext());
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void initAddress() {
        final SetingItemView siv_address = (SetingItemView) findViewById(R.id.siv_address);
        boolean isRunning = ServiceUtils.IsRunning("com.huwenkai.com.mobilesafe.Service.AddressService", this);
        siv_address.setCheak(isRunning);
        siv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischeck = siv_address.isCheck();
                siv_address.setCheak(!ischeck);
                Intent intent = new Intent(getApplicationContext(), AddressService.class);
                if (!ischeck) {
                    startService(intent);
                }else {
                    stopService(intent);
                }
            }
        });
    }

    private void initupdata() {
        //读取本地的配置查看是否勾选
        Boolean check_updata = SpUtils.getBoolean(ContactsValues.CHEACK_UPDATA, false, this);
        final SetingItemView updataitem = (SetingItemView) findViewById(R.id.siv_updata);
        //将读取的信息设置到checkbox上
        updataitem.setCheak(check_updata);
        updataitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischeck = updataitem.isCheck();
                updataitem.setCheak(!ischeck);
                SpUtils.putBoolean(ContactsValues.CHEACK_UPDATA, !ischeck, getApplicationContext());
            }
        });
    }
}
