package com.huwenkai.com.mobilesafe.Engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Path;

import com.huwenkai.com.mobilesafe.Util.LogUtils;

/**
 * 用来查询来电地址的类
 * Created by huwenkai on 2016/5/1.
 */
public class AddressDao {
    private static String path = "data/data/com.huwenkai.com.mobilesafe/files/address.db";
    public static String getAddress(String phone){
        if(phone.length()>=7){
        phone = phone.substring(0, 7);
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        LogUtils.e("aaaaa",sqLiteDatabase+"");
        Cursor cursor = sqLiteDatabase.query("data1", new String[]{"outkey"}, "id=?", new String[]{phone}, null, null, null);

        if(cursor.moveToNext()){
            String outkey = cursor.getString(0);
            Cursor cursor1 = sqLiteDatabase.query("data2", new String[]{"location"}, "id = ?", new String[]{outkey}, null, null, null);
            if(cursor1.moveToNext()){
                return  cursor1.getString(0);
            }
        }
        }
        else {
            return "号码未知";
        }
        return "号码未知";
    }
}
