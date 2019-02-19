package com.fxymine4ever.main.imageloader.strategy.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.fxymine4ever.main.imageloader.factory.CacheFactory;
import com.fxymine4ever.main.imageloader.strategy.compress.ResizeCompress;
import com.fxymine4ever.main.imageloader.util.CacheUtil;
import com.fxymine4ever.main.imageloader.util.NetUtil;
import com.jakewharton.disklrucache.DiskLruCache;

/**
 * create by:Fxymine4ever
 * time: 2019/2/12
 * 磁盘缓存+内存缓存
 */
public class DoubleCache implements ImageCache {
    private LruCache<String, Bitmap> memoryCache;
    private DiskLruCache diskLruCache;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 50;

    private volatile static DoubleCache instance;

    //一定要设置单例，否则在recyclerView或者ListView中用的不是一个Cache
    public static DoubleCache getInstance(Context context) {
        if (instance == null) {
            synchronized (DoubleCache.class) {
                if (instance == null) {
                    instance = new DoubleCache(context);
                }
            }
        }
        return instance;
    }

    private DoubleCache(Context context) {
        memoryCache = CacheFactory.makeLruCache();
        diskLruCache = CacheFactory.makeDiskLruCache(context, DISK_CACHE_SIZE);
    }


    @Override
    public void put(String url, Bitmap bitmap) {
        CacheUtil.addBitmapToMemory(url, bitmap, memoryCache);
        CacheUtil.addBitmapToDiskCache(url, bitmap, diskLruCache);
    }

    @Override
    public Bitmap get(String url, int width, int height) {
        Bitmap bitmap = CacheUtil.getBitmapFromMemory(url, memoryCache);
        if (bitmap != null) {
            return bitmap;
        }
        bitmap = CacheUtil.getBitmapFromDisk(url, diskLruCache);
        if (bitmap != null) {
            put(url, bitmap);//关闭应用后，内存被清空，此时应该再次存在内存中
            return bitmap;
        }
        return null;
    }
}
