package com.huwenkai.com.mobilesafe.Engine;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 短信备份引擎类
 * Created by huwenkai on 2016/5/9.
 */
public class SmsBcak {

    public  interface  BackCall{
        public void setMax(int max);
        public void setProgress(int index);

    }
    private static Cursor sCursor;

    public static void backup(Context context, File file, BackCall backCall) {

        int index = 0;
        FileOutputStream fos = null;
        try {
            XmlSerializer xmlSerializer = Xml.newSerializer();


            fos = new FileOutputStream(file);
            sCursor = context.getContentResolver().query(Uri.parse("content://sms/"), new String[]{"address", "date", "type", "body"}, null, null, null);
//            progressDialog.setMax(sCursor.getCount());
            if(backCall!=null)
            backCall.setMax(sCursor.getCount());

            xmlSerializer.setOutput(fos, "UTF-8");
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "smss");
            while (sCursor.moveToNext()) {

                String address = sCursor.getString(0);
                xmlSerializer.startTag(null, "address");
                xmlSerializer.text(address);
                xmlSerializer.endTag(null,"address");
                String date = sCursor.getString(1);
                xmlSerializer.startTag(null, "date");
                xmlSerializer.text(date);
                xmlSerializer.endTag(null,"date");
                String type = sCursor.getString(2);
                xmlSerializer.startTag(null, "type");
                xmlSerializer.text(type);
                xmlSerializer.endTag(null,"type");
                String body = sCursor.getString(3);
                xmlSerializer.startTag(null, "body");
                xmlSerializer.text(body);
                xmlSerializer.endTag(null,"body");

                index++;
                if (backCall != null){
//                    progressDialog.setProgress(index);
                backCall.setProgress(index);}
                Thread.sleep(100);
            }
            xmlSerializer.endTag(null, "smss");
            xmlSerializer.endDocument();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sCursor != null && fos != null) {
                    fos.close();
                    sCursor.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
