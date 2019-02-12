package com.fxymine4ever.main.imageloader.config;

import android.widget.ImageView;

import com.fxymine4ever.main.imageloader.strategy.cache.ImageCache;
import com.fxymine4ever.main.imageloader.strategy.cache.MemoryCache;

/**
 * create by:Fxymine4ever
 * time: 2019/2/12
 * 配置类
 */
public class RequestOptions {
    private String mUrl;
    private ImageCache mCache;
    private int mPlaceHolder;
    private int mError;
    private ImageView.ScaleType type;

    public RequestOptions() {
        this.mCache = MemoryCache.getInstance();
        this.mPlaceHolder = -1;
        this.mError = -1;
        this.type = null;
    }

    public String getUrl() {
        return mUrl;
    }

    public ImageCache getCache() {
        return mCache;
    }

    public int getPlaceHolder() {
        return mPlaceHolder;
    }

    public int getError() {
        return mError;
    }

    public ImageView.ScaleType getType() {
        return type;
    }

    public RequestOptions placeHolder(int mPlaceHolder) {
        this.mPlaceHolder = mPlaceHolder;
        return this;
    }

    public RequestOptions error(int mError) {
        this.mError = mError;
        return this;
    }

    public RequestOptions cache(ImageCache cache) {
        this.mCache = cache;
        return this;
    }

    public RequestOptions scaleType(ImageView.ScaleType scaleType) {
        this.type = scaleType;
        return this;
    }

    public RequestOptions url(String url) {
        this.mUrl = url;
        return this;
    }
}
