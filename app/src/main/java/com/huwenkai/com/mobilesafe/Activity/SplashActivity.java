package com.huwenkai.com.mobilesafe.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.Util.ContactsValues;
import com.huwenkai.com.mobilesafe.Util.SpUtils;
import com.huwenkai.com.mobilesafe.Util.StreamUitl;
import com.huwenkai.com.mobilesafe.Util.ToastUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";
    private static final int UPDATA_VERSION = 100;
    private static final int ENTER_HOME = 101;
    private static final int URL_ERROR = 102;
    private static final int IO_ERROR = 103;
    private static final int JSON_ERROR = 104;
    private TextView version_name;
    private int mVersionCode;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_VERSION:
                    //更新版本
                    updataversion();
                    break;
                case ENTER_HOME:
                    //进入主界面
                    enterHome();
                    break;
                case URL_ERROR:
                    ToastUtils.show(SplashActivity.this, "url错误");
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtils.show(SplashActivity.this, "读取错误");
                    enterHome();
                    break;
                case JSON_ERROR:
                    ToastUtils.show(SplashActivity.this, "json解析错误");
                    enterHome();
                    break;
            }
        }
    };
    private String mDescription;
    private String mDownloadUrl;
    private View rl_splash;

    /**
     * 此方法用的弹出对话框提示用户版本更新
     */
    private void updataversion() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("版本更新");
        builder.setMessage(mDescription);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //获取URl更新地址更新
                downloadApk();
            }

            private void downloadApk() {
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    HttpUtils http = new HttpUtils();
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mobilesafe.apk";
                   http.download(mDownloadUrl, path, new RequestCallBack<File>() {


                                @Override
                                public void onSuccess(ResponseInfo<File> responseInfo) {
                                    Log.d(TAG,"下载成功");
                                    File file = responseInfo.result;
                                    installApk(file);
                                }

                               private void installApk(File file) {
                                   Intent intent =new Intent("android.intent.action.VIEW");
                                   intent.addCategory("android.intent.category.DEFAULT");
                                   intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
                                   startActivityForResult(intent,0);
                               }

                       @Override
                                public void onLoading(long total, long current, boolean isUploading) {
                                    super.onLoading(total, current, isUploading);
                                    Log.d(TAG,"正在下载"+total+"......."+current);
                                }

                                @Override
                                public void onStart() {
                                    super.onStart();
                                    Log.d(TAG,"开始下载");
                                }

                                @Override
                                public void onFailure(HttpException e, String s) {
                                    Log.d(TAG,"下载失败");
                                }
                            });

                }
            }
        });

        builder.setNegativeButton("忽略更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
            }
        });
        builder.show();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initUI();
        initData();


        initAnimation();

    }

    /**
     * 启动页面淡入淡出动画
     */
    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(3000);
        rl_splash.startAnimation(alphaAnimation);
    }

    /**
     * 进入主界面
     */
    private void enterHome() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }

    /**
     * 初始化版本数据并获取网络版本
     */
    private void initData() {
        //1设置版本名称
        version_name.setText("版本名称:" + getVersionName());
        //2判断是否要更新, (通过比较版本号)
        mVersionCode = getVersionCode();
        //3连接网络获取json对象
        /*
        *   json对象包括版本号，版本名称
        *   最新版APK
        *   还有新版本特性
         */
        if(SpUtils.getBoolean(ContactsValues.CHEACK_UPDATA,false,getApplicationContext())) {
            relativeVersion();
        }else{
            mHandler.sendEmptyMessageDelayed(ENTER_HOME,2000);
        }
    }

    /**
     * 网络请求服务器比较版本号
     */
    private void relativeVersion() {
        //网络请求要在子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                long starttime = System.currentTimeMillis();
                Message message = Message.obtain();
                try {

                    URL url = new URL("http://10.0.2.2:8080/updata.json");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setReadTimeout(2000);
                    httpURLConnection.setConnectTimeout(2000);
                    if (httpURLConnection.getResponseCode() == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        String json = StreamUitl.StreamToString(inputStream);
                        //解析json对象/
                        /*
                            {
                            "description": "新版本狂拽炫酷",
                            "downloadUrl": "http://www.baidu.com",
                            "versionCode": "2",
                            "versionName": "2.0"
                            }
                          */
                        JSONObject jsonObject = new JSONObject(json);
                        mDescription = jsonObject.getString("description");
                        mDownloadUrl = jsonObject.getString("downloadUrl");
                        String versionCode = jsonObject.getString("versionCode");
                        String versionName = jsonObject.getString("versionName");

                        if (mVersionCode < Integer.parseInt(versionCode)) {
                            //发送更新消息
                            message.what = UPDATA_VERSION;
                        } else {
                            //发送进入主界面message
                            message.what = ENTER_HOME;
                        }

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();

                    message.what = URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what = IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    message.what = JSON_ERROR;
                } finally {
                    try {
                        //停留2秒后进入
                        long endtime = System.currentTimeMillis();
                        if (endtime - starttime < 2000)
                            Thread.sleep(2000 - (endtime - starttime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendMessage(message);
                }

            }
        }).start();
    }

    /**
     * @return 返回版本号， null时获取失败
     */
    private String getVersionName() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化开始画面的版本号
     */
    private void initUI() {
        version_name = (TextView) findViewById(R.id.tv_version_name);
        rl_splash = findViewById(R.id.rl_splash);
    }

    /**
     * @return 获取版本号 为0时获取失败
     */
    public int getVersionCode() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
