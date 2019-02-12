package com.fxymine4ever.main.imageloader.util;

import android.graphics.Bitmap;

/**
 * create by:Fxymine4ever
 * time: 2019/2/12
 */
public class BitmapUtil {
    public static Bitmap getEmptyBitmap() {
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_4444);
    }
}
