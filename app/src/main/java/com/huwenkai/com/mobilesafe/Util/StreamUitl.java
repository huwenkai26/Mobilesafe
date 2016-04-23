package com.huwenkai.com.mobilesafe.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by huwenkai on 2016/4/22.
 */
public class StreamUitl {
    public static String StreamToString(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int count=0;
        try {
            while ((count = inputStream.read(buff))!=-1){
                byteArrayOutputStream.write(buff,0,count);
            }
            return byteArrayOutputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            try {
                inputStream.close();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }
}
