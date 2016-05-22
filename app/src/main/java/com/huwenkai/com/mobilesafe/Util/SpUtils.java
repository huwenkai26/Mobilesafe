package com.huwenkai.com.mobilesafe.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences的工具类
 * Created by huwenkai on 2016/4/25.
 */
public class SpUtils {
    //将sp写成static可以避免多次赋值
    private static SharedPreferences sp;

    /**
     * @param key     传入的key
     * @param value   传入的值
     * @param context 需要的上下文
     */
    public static void putBoolean(String key, boolean value, Context context) {

        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();

    }

    /**
     * @param key      传入的key
     * @param defvalue key不匹配得到的默认值
     * @param context  传入的上下文
     * @return 匹配key得到的值
     */
    public static Boolean getBoolean(String key, boolean defvalue, Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        boolean spBoolean = sp.getBoolean(key, defvalue);
        return spBoolean;
    }

    /**
     * @param key     传入的key
     * @param value   传入的值
     * @param context 需要的上下文
     */
    public static void putString(String key, String value, Context context) {

        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();

    }

    /**
     * @param key      传入的key
     * @param defvalue key不匹配得到的默认值
     * @param context  传入的上下文
     * @return 匹配key得到的值
     */
    public static String getString(String key, String defvalue, Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        String spString = sp.getString(key, defvalue);
        return spString;
    }

    /**
     * 删除sharedpreference的节点
     * @param key
     * @param context
     */
    public static void remove(String key,Context context){
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).commit();

    }
    /**
     * @param key     传入的key
     * @param value   传入的值
     * @param context 需要的上下文
     */
    public static void putInt(String key,int value, Context context) {

        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();

    }

    /**
     * @param key      传入的key
     * @param defvalue key不匹配得到的默认值
     * @param context  传入的上下文
     * @return 匹配key得到的值
     */
    public static int getInt(String key, int defvalue, Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        int spString = sp.getInt(key, defvalue);
        return spString;
    }

}
