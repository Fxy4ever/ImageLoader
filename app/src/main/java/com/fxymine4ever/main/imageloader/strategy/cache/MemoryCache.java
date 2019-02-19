package com.fxymine4ever.main.imageloader.strategy.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.fxymine4ever.main.imageloader.factory.CacheFactory;
import com.fxymine4ever.main.imageloader.util.CacheUtil;
import com.fxymine4ever.main.imageloader.util.NetUtil;


/**
 * create by:Fxymine4ever
 * time: 2019/2/12
 * 默认内存缓存
 */
public class MemoryCache implements ImageCache {
    private volatile static MemoryCache instance;

    public static MemoryCache getInstance() {//一定要设置单例，否则在recyclerView或者ListView中用的不是一个Cache
        if (instance == null) {
            synchronized (MemoryCache.class) {
                if (instance == null) {
                    instance = new MemoryCache();
                }
            }
        }
        return instance;
    }

    private LruCache<String, Bitmap> memoryCache;

    private MemoryCache() {
        memoryCache = CacheFactory.makeLruCache();
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        CacheUtil.addBitmapToMemory(url, bitmap, memoryCache);
    }

    @Override
    public Bitmap get(String url,int width,int height) {
        Bitmap bitmap = CacheUtil.getBitmapFromMemory(url, memoryCache);//先从内存加载
        if (bitmap != null) {
            return bitmap;
        }
        return null;
    }

}
