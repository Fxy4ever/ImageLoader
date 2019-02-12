package com.fxymine4ever.main.imageloader.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * create by:Fxymine4ever
 * time: 2019/2/12
 * Cache读写工具类
 */
public class CacheUtil {

    public static void addBitmapToMemory(String url, Bitmap bitmap, LruCache<String, Bitmap> memoryCache) {
        String md5 = MD5Util.UrltoMd5(url);
        if (memoryCache.get(md5) == null) {
            memoryCache.put(md5, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemory(String url, LruCache<String, Bitmap> memoryCache) {
        String md5 = MD5Util.UrltoMd5(url);
        return memoryCache.get(md5);
    }

    public static void addBitmapToDiskCache(String url, Bitmap bitmap, DiskLruCache diskLruCache) {
        String md5 = MD5Util.UrltoMd5(url);
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(md5);
            if (editor != null) {
                OutputStream os = editor.newOutputStream(0);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                editor.commit();
            }
            diskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromDisk(String url, DiskLruCache diskLruCache) {
        Bitmap bitmap = null;
        String md5 = MD5Util.UrltoMd5(url);
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(md5);
            if (snapshot != null) {
                FileInputStream fis = (FileInputStream) snapshot.getInputStream(0);
                FileDescriptor descriptor = fis.getFD();
                bitmap = BitmapFactory.decodeFileDescriptor(descriptor);
            }
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
