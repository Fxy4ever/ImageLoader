package com.fxymine4ever.main.imageloader.strategy.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.fxymine4ever.main.imageloader.factory.CacheFactory;
import com.fxymine4ever.main.imageloader.strategy.compress.ResizeCompress;
import com.fxymine4ever.main.imageloader.util.CacheUtil;
import com.fxymine4ever.main.imageloader.util.NetUtil;
import com.jakewharton.disklrucache.DiskLruCache;

import static com.fxymine4ever.main.imageloader.core.ImageLoader.TAG;

/**
 * create by:Fxymine4ever
 * time: 2019/2/13
 */
public class DiskCache implements ImageCache {
    private DiskLruCache diskLruCache;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 50;

    private volatile static DiskCache instance;

    //一定要设置单例，否则在recyclerView或者ListView中用的不是一个Cache
    public static DiskCache getInstance(Context context) {
        if (instance == null) {
            synchronized (DoubleCache.class) {
                if (instance == null) {
                    instance = new DiskCache(context);
                }
            }
        }
        return instance;
    }

    private DiskCache(Context context) {
        diskLruCache = CacheFactory.makeDiskLruCache(context, DISK_CACHE_SIZE);
    }


    @Override
    public void put(String url, Bitmap bitmap) {
        CacheUtil.addBitmapToDiskCache(url, bitmap, diskLruCache);
    }

    @Override
    public Bitmap get(String url, int width, int height) {
        Bitmap bitmap = CacheUtil.getBitmapFromDisk(url, diskLruCache);
        if (bitmap != null) {
            Log.d(TAG, "load from disk");
            return bitmap;
        }
        return null;
    }
}
