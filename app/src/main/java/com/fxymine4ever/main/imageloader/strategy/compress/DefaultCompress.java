package com.fxymine4ever.main.imageloader.strategy.compress;

import android.graphics.Bitmap;

/**
 * create by:Fxymine4ever
 * time: 2019/2/12
 * 默认压缩策略 = 不压缩
 */
public class DefaultCompress implements ImageCompress {
    private static DefaultCompress instance;

    public static DefaultCompress getInstance(){
        if(instance == null){
            synchronized (DefaultCompress.class){
                if(instance == null){
                    instance =  new DefaultCompress();
                }
            }
        }
        return instance;
    }

    private DefaultCompress() {
    }

    @Override
    public Bitmap compress(Bitmap bitmap, int width, int height) {
        return bitmap;
    }
}
