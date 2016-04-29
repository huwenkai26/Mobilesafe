package com.huwenkai.com.mobilesafe.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Util.ContactsValues;
import com.huwenkai.com.mobilesafe.Util.SpUtils;
import com.huwenkai.com.mobilesafe.View.SetingItemView;

/**
 * 设置界面的activity
 * Created by huwenkai on 2016/4/24.
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_settings);
        initupdata();
    }

    private void initupdata() {
        //读取本地的配置查看是否勾选
        Boolean check_updata = SpUtils.getBoolean(ContactsValues.CHEACK_UPDATA, false, this);
        final SetingItemView updataitem = (SetingItemView) findViewById(R.id.siv_updata);
        //将读取的信息设置到checkbox上
        updataitem.setcheak(check_updata);
        updataitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischeck = updataitem.ischeck();
                updataitem.setcheak(!ischeck);
                SpUtils.putBoolean(ContactsValues.CHEACK_UPDATA,!ischeck,getApplicationContext());
            }
        });
    }
}
