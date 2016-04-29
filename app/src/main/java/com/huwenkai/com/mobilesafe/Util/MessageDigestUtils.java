package com.huwenkai.com.mobilesafe.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 * Created by huwenkai on 2016/4/26.
 */
public class MessageDigestUtils {
    public static String StringToMD5(String value,String algorithm){
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            byte[] digest = messageDigest.digest(value.getBytes());
            for (byte s:digest) {
                int i = s & 0xff;
                String s1 = Integer.toHexString(i);
                sb.append(s1);
            }
            return  sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
