package com.fxymine4ever.main.imageloader.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.util.Objects;

/**
 * create by:Fxymine4ever
 * time: 2019/2/5
 * 文件操作类
 */
public class FileUtil {
    public static File getDiskCacheDir(Context context,String name){
        boolean externalStorageAvailable =
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if(externalStorageAvailable){
            cachePath = Objects.requireNonNull(context.getExternalCacheDir()).getPath();
        }else{
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + name);
    }

    public static long getUsableSpace(File path){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            return path.getUsableSpace();
        }
        final StatFs statFs = new StatFs(path.getPath());
        return statFs.getBlockSize() * (long) statFs.getAvailableBlocks();
    }
}
