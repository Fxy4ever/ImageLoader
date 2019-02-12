package com.fxymine4ever.main.imageloader.util;

import android.graphics.Bitmap;
import android.util.Log;

import static com.fxymine4ever.main.imageloader.start.ImageLoader.TAG;

/**
 * create by:Fxymine4ever
 * time: 2019/2/12
 */
public class BitmapUtil {
    public static Bitmap getEmptyBitmap(){
        return Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_4444);
    }

    public static Bitmap resize(Bitmap bitmap,int width,int height){
        return Bitmap.createScaledBitmap(bitmap,width,height,true);
    }

    public static Bitmap autoResizeByWidth(Bitmap bitmap,int width){
        float w = bitmap.getWidth();
        float h = bitmap.getHeight();
        int height = (int)(h / w * width);
        Log.d(TAG,"height="+height+" width="+width);
        return resize(bitmap,width,height);
    }

    public static Bitmap autoResizeByHeight(Bitmap bitmap,int height){
        float w = bitmap.getWidth();
        float h = bitmap.getHeight();
        int width = (int) (w / h * height);
        return resize(bitmap,width,height);
    }
}
