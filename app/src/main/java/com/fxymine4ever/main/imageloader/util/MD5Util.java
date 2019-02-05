package com.fxymine4ever.main.imageloader.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * create by:Fxymine4ever
 * time: 2019/2/5
 * String转MD5操作类
 */
public class MD5Util {

    public static String UrltoMd5(String url){
        String cacheKey;
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            cacheKey = byteToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

     private static String byteToHexString(byte[] bytes){
        StringBuilder builder = new StringBuilder();
         for (int i = 0; i < bytes.length; i++) {
             String hex = Integer.toHexString(0xFF & bytes[i]);
             if(hex.length() == 1){
                 builder.append('0');
             }
             builder.append(hex);
         }
         return builder.toString();
     }
}
