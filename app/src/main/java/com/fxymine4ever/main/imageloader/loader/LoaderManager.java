package com.fxymine4ever.main.imageloader.loader;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;


/**
 * create by:Fxymine4ever
 * time: 2019/2/19
 */
public class LoaderManager {
    private static LoaderManager INSTANCE;
    private Map<String, ILoader> mLoaderMap = new HashMap<>();


    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String FILE = "file";
    private LoaderManager() {
        register(HTTP, new UrlLoader());
        register(HTTPS, new UrlLoader());
        register(FILE, new FileLoader());
    }

    public final synchronized void register(String schema, ILoader loader) {
        mLoaderMap.put(schema, loader);
    }

    public ILoader getLoader(String schema) {
        if (mLoaderMap.containsKey(schema)) {
            return mLoaderMap.get(schema);
        }
        return null;
    }

    public static LoaderManager getInstance() {
        if (INSTANCE == null) {
            synchronized (LoaderManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LoaderManager();
                }
            }
        }
        return INSTANCE;
    }
}
