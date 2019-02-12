package com.fxymine4ever.main.imageloader.strategy.compress;

import android.graphics.Bitmap;

/**
 * create by:Fxymine4ever
 * time: 2019/2/12
 * 直接缩放指定大小
 */
public class AnotherResizeCompress implements ImageCompress {
    private static AnotherResizeCompress instance;

    public static AnotherResizeCompress getInstance(){
        if(instance == null){
            synchronized (AnotherResizeCompress.class){
                if(instance == null){
                    instance =  new AnotherResizeCompress();
                }
            }
        }
        return instance;
    }

    private AnotherResizeCompress() {
    }


    @Override
    public Bitmap compress(Bitmap bitmap, int width, int height) {
        return resize(bitmap,width,height);
    }

    public static Bitmap resize(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    public static Bitmap autoResizeByWidth(Bitmap bitmap, int width) {
        float w = bitmap.getWidth();
        float h = bitmap.getHeight();
        int height = (int) (h / w * width);
        return resize(bitmap, width, height);
    }

    public static Bitmap autoResizeByHeight(Bitmap bitmap, int height) {
        float w = bitmap.getWidth();
        float h = bitmap.getHeight();
        int width = (int) (w / h * height);
        return resize(bitmap, width, height);
    }

}
