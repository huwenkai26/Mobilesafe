package com.huwenkai.com.mobilesafe.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huwenkai.com.mobilesafe.Engine.SmsBcak;
import com.huwenkai.com.mobilesafe.R;

import org.w3c.dom.Text;

import java.io.File;

public class AToolActivity extends AppCompatActivity {

    private TextView tv_query_phone_address;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool);
        initUI();
    }

    private void initUI() {
        tv_query_phone_address = (TextView) findViewById(R.id.tv_query_phone_address);
        tv_query_phone_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),PhoneaddressActivity.class);
                startActivity(intent);
            }
        });
        TextView tv_sms_back = (TextView) findViewById(R.id.tv_sms_back);
        tv_sms_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog();
            }
        });

    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("短信备份");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setIcon(R.mipmap.ic_launcher);
        mProgressDialog.show();


        //调用短信备份的引擎方法
        new Thread(){
            @Override
            public void run() {
                super.run();

                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"sms74.xml";

                File file = new File(path);


                SmsBcak.backup(getApplicationContext(), file, new SmsBcak.BackCall() {
                    @Override
                    public void setMax(int max) {
                        mProgressDialog.setMax(max);
                    }

                    @Override
                    public void setProgress(int index) {
                        mProgressDialog.setProgress(index);
                       
                    }
                });
                mProgressDialog.dismiss();
            }
        }.start();

    }

}
