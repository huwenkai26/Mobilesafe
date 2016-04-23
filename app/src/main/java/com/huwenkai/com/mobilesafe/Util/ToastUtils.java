package com.huwenkai.com.mobilesafe.Util;

import android.content.Context;
import android.widget.Toast;

import com.huwenkai.com.mobilesafe.Activity.SplashActivity;

/**
 * Toast工具类
 * Created by huwenkai on 2016/4/23.
 */
public class ToastUtils {
    /**
     * @param splashActivity 上下文
     * @param mes 弹出的消息
     */
    public static void show(Context splashActivity, String mes) {
        Toast.makeText(splashActivity,mes,Toast.LENGTH_SHORT).show();
    }
}
