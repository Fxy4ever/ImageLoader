package com.fxymine4ever.main.imageloader.factory;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

import com.fxymine4ever.main.imageloader.util.FileUtil;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;

/**
 * create by:Fxymine4ever
 * time: 2019/2/12
 * 缓存工厂类，负责创建DiskLruCache和LruCache
 */
public class CacheFactory {

    public static DiskLruCache makeDiskLruCache(Context context, int DISK_CACHE_SIZE) {
        DiskLruCache diskLruCache = null;
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
        return diskLruCache;
    }

    public static LruCache<String, Bitmap> makeLruCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        return new LruCache<String, Bitmap>(maxMemory / 8) {
            @Override
            protected int sizeOf(@NonNull String key, @NonNull Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }
}
