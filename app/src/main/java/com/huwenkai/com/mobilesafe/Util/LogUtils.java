package com.huwenkai.com.mobilesafe.Util;

import android.util.Log;

/**
 * log的工具类
 * Created by huwenkai on 2016/4/29.
 */
public class LogUtils {
    public static final int FLAG = 1;

    /**
     * @param tag log 传入的Tag
     * @param msg 用来打印的消息
     */
    public static void d(String tag,String msg){
        if(FLAG==1){
            Log.d(tag,msg);
        }
    }

    /**
     * @param tag log 传入的Tag
     * @param msg 用来打印的消息
     */
    public static void v(String tag,String msg){
        if(FLAG==1){
            Log.v(tag,msg);
        }
    }

    /**
     * @param tag log 传入的Tag
     * @param msg 用来打印的消息
     */
    public static void i(String tag,String msg){
        if(FLAG==1){
            Log.i(tag,msg);
        }
    }
    /**
     * @param tag log 传入的Tag
     * @param msg 用来打印的消息
     */
    public static void e(String tag,String msg){
        if(FLAG==1){
            Log.e(tag,msg);
        }
    }

    /**
     * @param tag log 传入的Tag
     * @param msg 用来打印的消息
     */
    public static void w(String tag,String msg){
        if(FLAG==1){
            Log.w(tag,msg);
        }
    }

}
