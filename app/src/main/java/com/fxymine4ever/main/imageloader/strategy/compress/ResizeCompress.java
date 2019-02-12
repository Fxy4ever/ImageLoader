package com.fxymine4ever.main.imageloader.strategy.compress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * create by:Fxymine4ever
 * time: 2019/2/12
 * 通过采样率来压缩
 */
public class ResizeCompress implements ImageCompress {
    private static ResizeCompress instance;

    public static ResizeCompress getInstance(){
        if(instance == null){
            synchronized (ResizeCompress.class){
                if(instance == null){
                    instance =  new ResizeCompress();
                }
            }
        }
        return instance;
    }

    private ResizeCompress() {
    }

    @Override
    public Bitmap compress(Bitmap bitmap, int width, int height) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        ByteArrayInputStream is = new ByteArrayInputStream(out.toByteArray());
        return BitmapFactory.decodeStream(is, null, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        if (reqHeight == 0 || reqWidth == 0)
            return 1;

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;//通常采样率为2的次方数
            }
        }
        return inSampleSize;
    }
}
