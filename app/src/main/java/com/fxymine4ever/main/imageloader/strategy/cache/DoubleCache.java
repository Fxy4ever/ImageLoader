package com.fxymine4ever.main.imageloader.strategy.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.fxymine4ever.main.imageloader.util.BitmapUtil;
import com.fxymine4ever.main.imageloader.util.FileUtil;
import com.fxymine4ever.main.imageloader.util.MD5Util;
import com.fxymine4ever.main.imageloader.util.NetUtil;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.fxymine4ever.main.imageloader.start.ImageLoader.TAG;

/**
 * create by:Fxymine4ever
 * time: 2019/2/12
 */
public class DoubleCache implements ImageCache {
    private LruCache<String, Bitmap> memoryCache;
    private DiskLruCache diskLruCache;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 50;
    private static final int DISK_CACHE_INDEX = 0;

    private volatile static DoubleCache instance;

    public static DoubleCache getInstance(Context context) {//一定要设置单例，否则在recyclerView或者ListView中用的不是一个Cache
        if (instance == null) {
            synchronized (DoubleCache.class) {
                if (instance == null) {
                    instance = new DoubleCache(context);
                }
            }
        }
        return instance;
    }

    public DoubleCache(Context context) {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        memoryCache = new LruCache<String, Bitmap>(maxMemory / 8) {
            @Override
            protected int sizeOf(@NonNull String key, @NonNull Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
        File diskCacheDir = FileUtil.getDiskCacheDir(context, "bitmap");
        if (!diskCacheDir.exists())
            diskCacheDir.mkdirs();

        if (FileUtil.getUsableSpace(diskCacheDir) >= DISK_CACHE_SIZE) {//检测可用空间大小
            try {
                diskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void put(String url, Bitmap bitmap) {
        addBitmapToMemory(url, bitmap);
        addBitmapToDiskCache(url, bitmap);
    }

    @Override
    public Bitmap get(String url, int reqWidth, int reqHeight) {
        Bitmap bitmap = getBitmapFromMemory(url);
        if (bitmap != null) {
            Log.d(TAG, "Load bitmap from memory");
            return bitmap;
        }
        bitmap = getBitmapFromDisk(url);
        if (bitmap != null) {
            Log.d(TAG, "Load bitmap from disk");
            return bitmap;
        }

        bitmap = NetUtil.downloadBitmapFromUrlWithOkHttp(url);
        if (bitmap != null) {
            Log.d(TAG, "Load bitmap from network");
            put(url, bitmap);
            return bitmap;
        }
        return null;
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

    private void addBitmapToDiskCache(String url, Bitmap bitmap) {
        String md5 = MD5Util.UrltoMd5(url);
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(md5);
            if (editor != null) {
                OutputStream os = editor.newOutputStream(DISK_CACHE_INDEX);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                editor.commit();
            }
            diskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromDisk(String url) {
        Bitmap bitmap = null;
        String md5 = MD5Util.UrltoMd5(url);
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(md5);
            if (snapshot != null) {
                FileInputStream fis = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
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
