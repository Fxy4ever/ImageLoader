package com.fxymine4ever.main.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

/**
 * create by:Fxymine4ever
 * time: 2019/2/5
 * 图片压缩，利用Bitmap的采样率
 */
public class ImageResizer {
    public ImageResizer() {
    }

    //中间仅仅多了一步求采样率
    public Bitmap decodeSampleBitmapFromResource(Resources res, int resId,
                                                 int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//设置为true才能修改采样率
        BitmapFactory.decodeResource(res, resId, options);
        //计算采样率
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public Bitmap decodeSampledBitmapFromFileDescriptor(
            FileDescriptor fd, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }


    public int calculateInSampleSize(BitmapFactory.Options options,
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
