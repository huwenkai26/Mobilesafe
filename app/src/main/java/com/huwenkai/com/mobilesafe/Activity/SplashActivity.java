package com.huwenkai.com.mobilesafe.Activity;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Util.StreamUitl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private TextView version_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initUI();
        initData();

    }


    /**
     * 初始化版本数据并获取网络版本
     */
    private void initData() {
        //1设置版本名称
        version_name.setText("版本名称:"+getVersionName());
        //2判断是否要更新, (通过比较版本号)
        int mVersionCode = getVersionCode();
        //3连接网络获取json对象
        /*
        *   json对象包括版本号，版本名称
        *   最新版APK
        *   还有新版本特性
         */
        relativeVersion();

    }

    /**
     * 网络请求服务器比较版本号
     */
    private void relativeVersion() {
       //网络请求要在子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:8080/updata.json");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setReadTimeout(2000);
                    httpURLConnection.setConnectTimeout(2000);
                    if(httpURLConnection.getResponseCode()==200){
                        InputStream inputStream = httpURLConnection.getInputStream();
                        String json = StreamUitl.StreamToString(inputStream);
                        
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * @return 返回版本号， null时获取失败
     */
    private String getVersionName() {
        try {
            PackageInfo packageInfo= getPackageManager().getPackageInfo(getPackageName(),0);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    /**
     * 初始化开始画面的版本号
     */
    private void initUI() {
        version_name = (TextView) findViewById(R.id.tv_version_name);

    }

    /**
     * @return 获取版本号 为0时获取失败
     */
    public int getVersionCode() {
        try {
            PackageInfo packageInfo= getPackageManager().getPackageInfo(getPackageName(),0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
