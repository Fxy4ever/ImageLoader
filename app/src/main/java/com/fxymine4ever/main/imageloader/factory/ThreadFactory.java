package com.fxymine4ever.main.imageloader.factory;

import android.support.annotation.NonNull;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * create by:Fxymine4ever
 * time: 2019/2/5
 * Thread工厂类
 */
public class ThreadFactory implements java.util.concurrent.ThreadFactory {
    private static AtomicInteger mCount = new AtomicInteger(1);

    ThreadFactory() {
    }

    @Override
    public Thread newThread(@NonNull Runnable r) {
        return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
    }
}
