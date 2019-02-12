package com.fxymine4ever.main.imageloader.strategy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * create by:Fxymine4ever
 * time: 2019/2/5
 * 图片压缩，利用Bitmap的采样率
 */
public class ImageResizer {

    public static Bitmap resizeBitmap(Bitmap bitmap, ImageView imageView) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = calculateInSampleSize(options, imageView.getWidth(), imageView.getHeight());
        options.inJustDecodeBounds = false;
        ByteArrayInputStream is = new ByteArrayInputStream(out.toByteArray());
        return BitmapFactory.decodeStream(is, null, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
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
