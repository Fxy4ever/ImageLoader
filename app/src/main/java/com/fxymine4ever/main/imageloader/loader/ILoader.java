package com.fxymine4ever.main.imageloader.loader;

import android.graphics.Bitmap;

import com.fxymine4ever.main.imageloader.strategy.cache.ImageCache;

/**
 * create by:Fxymine4ever
 * time: 2019/2/19
 */
public interface ILoader {
    Bitmap load(String url, ImageCache cache,int width,int height);
}
