package com.fxymine4ever.main.imageloader.loader;

import android.graphics.Bitmap;
import android.util.Log;

import com.fxymine4ever.main.imageloader.strategy.cache.ImageCache;
import com.fxymine4ever.main.imageloader.strategy.compress.ResizeCompress;
import com.fxymine4ever.main.imageloader.util.NetUtil;

import static com.fxymine4ever.main.imageloader.core.ImageLoader.TAG;

/**
 * create by:Fxymine4ever
 * time: 2019/2/19
 */
public class UrlLoader implements ILoader {
    @Override
    public Bitmap load(String url, ImageCache cache, int width, int height) {
        Bitmap bitmap = cache.get(url,width,height);
        if(bitmap == null){
            bitmap = NetUtil.downloadBitmapFromUrlWithOkHttp(url);
            Log.d(TAG, "load from net");
            bitmap = ResizeCompress.getInstance().compress(bitmap,width,height);
            cache.put(url,bitmap);
            return bitmap;
        }
        return bitmap;
    }
}
