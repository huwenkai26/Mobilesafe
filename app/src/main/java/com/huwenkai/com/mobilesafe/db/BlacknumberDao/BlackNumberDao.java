package com.huwenkai.com.mobilesafe.db.BlacknumberDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.huwenkai.com.mobilesafe.db.Domian.BlackNumberInfo;
import com.huwenkai.com.mobilesafe.db.MyblacknumberOpenHelp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huwenkai on 2016/5/7.
 */
public class BlackNumberDao {

    private final MyblacknumberOpenHelp mMyblacknumberOpenHelp;
    private List<BlackNumberInfo> mBlackNumberInfoList;
    private int mCount;
    private int mode;

    private BlackNumberDao(Context context) {
        mMyblacknumberOpenHelp = new MyblacknumberOpenHelp(context);
    }

    private static BlackNumberDao mBlackNumberDao = null;

    public static BlackNumberDao getInstances(Context context) {
        if (mBlackNumberDao == null) {
            mBlackNumberDao = new BlackNumberDao(context);
        }
        return mBlackNumberDao;
    }

    public void insert(String phone, String mode) {
        SQLiteDatabase db = mMyblacknumberOpenHelp.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone",phone);
        values.put("mode",mode);
        db.insert("blacknumber", null, values);
        db.close();
    }
    public void delete(String phone) {
        SQLiteDatabase db = mMyblacknumberOpenHelp.getReadableDatabase();

        db.delete("blacknumber", "phone = ?", new String[]{phone});
        db.close();

    }
    public List<BlackNumberInfo> findall(){
        SQLiteDatabase db = mMyblacknumberOpenHelp.getReadableDatabase();

        Cursor cursor = db.query("blacknumber", new String[]{"phone", "mode"}, null, null, null, null, "_id desc");
        mBlackNumberInfoList = new ArrayList<BlackNumberInfo>();
        while(cursor.moveToNext()){
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.phone = cursor.getString(0);
            blackNumberInfo.mode =  cursor.getString(1);
            mBlackNumberInfoList.add(blackNumberInfo);
        }
        cursor.close();
        db.close();
        return mBlackNumberInfoList;

    }
    public List<BlackNumberInfo> find(int index){
        SQLiteDatabase db = mMyblacknumberOpenHelp.getReadableDatabase();

        Cursor cursor = db.rawQuery("select  * from blacknumber order by _id desc limit ?,20;", new String[]{index+""});
        mBlackNumberInfoList = new ArrayList<BlackNumberInfo>();
        while(cursor.moveToNext()){
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.phone = cursor.getString(1);
            blackNumberInfo.mode =  cursor.getString(2);
            mBlackNumberInfoList.add(blackNumberInfo);
        }
        cursor.close();
        db.close();
        return mBlackNumberInfoList;

    }
    public int getCount (){
        SQLiteDatabase db = mMyblacknumberOpenHelp.getReadableDatabase();

        Cursor cursor = db.rawQuery("select  count(*) from blacknumber ",null);
       if(cursor.moveToNext()){
           mCount = cursor.getInt(0);
       }
        cursor.close();
        db.close();

        return  mCount;
    }
    public int getMode(String phone){
        SQLiteDatabase db = mMyblacknumberOpenHelp.getReadableDatabase();
        Cursor cursor = db.rawQuery("select  mode from blacknumber where phone = ?",new String[]{phone});
        if(cursor.moveToNext()){
            mode = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        return  mode;
    }


}
