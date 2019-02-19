package com.fxymine4ever.main.imageloader.config;

import android.widget.ImageView;

import com.fxymine4ever.main.imageloader.loader.ILoader;
import com.fxymine4ever.main.imageloader.loader.LoaderManager;
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
    private ILoader loader;

    public RequestOptions(String mUrl, ImageCache mCache, int mPlaceHolder, int mError, ImageView.ScaleType type, ILoader loader) {
        this.mUrl = mUrl;
        this.mCache = mCache;
        this.mPlaceHolder = mPlaceHolder;
        this.mError = mError;
        this.type = type;
        this.loader = loader;
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

    public ILoader getLoader() {
        return loader;
    }

    public static class Builder{
        private String mUrl;
        private ImageCache mCache;
        private int mPlaceHolder;
        private int mError;
        private ImageView.ScaleType type;
        private ILoader loader;
        private RequestOptions requestOptions;

        public Builder() {
            this.mCache = MemoryCache.getInstance();
            this.mPlaceHolder = -1;
            this.mError = -1;
            this.type = null;
        }


        public Builder placeHolder(int mPlaceHolder) {
            this.mPlaceHolder = mPlaceHolder;
            return this;
        }

        public Builder error(int mError) {
            this.mError = mError;
            return this;
        }

        public Builder cache(ImageCache cache) {
            this.mCache = cache;
            return this;
        }

        public Builder scaleType(ImageView.ScaleType scaleType) {
            this.type = scaleType;
            return this;
        }


        public Builder url(String url) {
            this.mUrl = url;
            if(url.contains("file")){
                loader = LoaderManager.getInstance().getLoader(LoaderManager.FILE);
            }else if(url.contains("https")){
                loader = LoaderManager.getInstance().getLoader(LoaderManager.HTTPS);
            }else if(url.contains("http")){
                loader = LoaderManager.getInstance().getLoader(LoaderManager.HTTP);
            }
            return this;
        }

        public RequestOptions build(){
            return new RequestOptions(mUrl,mCache,mPlaceHolder,mError,type,loader);
        }
    }
}
