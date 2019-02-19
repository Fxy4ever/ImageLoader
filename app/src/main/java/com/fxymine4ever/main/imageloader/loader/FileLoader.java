package com.fxymine4ever.main.imageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.fxymine4ever.main.imageloader.strategy.cache.ImageCache;
import com.fxymine4ever.main.imageloader.strategy.compress.ResizeCompress;

import java.io.File;

/**
 * create by:Fxymine4ever
 * time: 2019/2/19
 */
public class FileLoader implements ILoader {
    @Override
    public Bitmap load(String url, ImageCache cache, int width, int height) {
        Bitmap bitmap = cache.get(url, width, height);
        if (bitmap == null) {
            final String imagePath = Uri.parse(url).getPath();
            final File imgFile;
            if (imagePath != null) {
                imgFile = new File(imagePath);
                if (!imgFile.exists()) {
                    return null;
                }
                return ResizeCompress.getInstance().compress(BitmapFactory.decodeFile(imagePath), width, height);
            }
        }
        return bitmap;
    }
}
