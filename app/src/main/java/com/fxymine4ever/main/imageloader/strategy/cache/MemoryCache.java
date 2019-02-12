package com.fxymine4ever.main.imageloader.strategy.cache;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.support.v4.util.LruCache;

import com.fxymine4ever.main.imageloader.util.BitmapUtil;
import com.fxymine4ever.main.imageloader.util.MD5Util;
import com.fxymine4ever.main.imageloader.util.NetUtil;

import static start.ImageLoader.TAG;


/**
 * create by:Fxymine4ever
 * time: 2019/2/12
 */
public class MemoryCache implements ImageCache {
    private volatile static MemoryCache instance;

    public static MemoryCache getInstance(){//一定要设置单例，否则在recyclerView或者ListView中用的不是一个Cache
        if(instance == null){
            synchronized (MemoryCache.class){
                if(instance==null){
                    instance = new MemoryCache();
                }
            }
        }
        return instance;
    }

    private LruCache<String, Bitmap> memoryCache;

    private MemoryCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);//转kb
        int cacheSize = maxMemory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(@NonNull String key, @NonNull Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        addBitmapToMemory(url, bitmap);
    }

    @Override
    public Bitmap get(String url, int reqWidth, int reqHeight) {
        Bitmap bitmap = getBitmapFromMemory(url);//先从内存加载
        if (bitmap != null) {
            Log.d(TAG,"load from memory");
            return bitmap;
        }
        bitmap = NetUtil.downloadBitmapFromUrlWithOkHttp(url);//下载
        if (bitmap != null) {
            Log.d(TAG,"load from network");
            put(url, bitmap);
        }
        return bitmap;
    }

    private void addBitmapToMemory(String url, Bitmap bitmap) {
        String md5 = MD5Util.UrltoMd5(url);
        if (memoryCache.get(md5) == null) {
            memoryCache.put(md5, bitmap);
        }
    }

    private Bitmap getBitmapFromMemory(String url) {
        String md5 = MD5Util.UrltoMd5(url);
        return memoryCache.get(md5);
    }
}
