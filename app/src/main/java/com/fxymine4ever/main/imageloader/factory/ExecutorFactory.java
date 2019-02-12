package com.fxymine4ever.main.imageloader.factory;

import android.nfc.Tag;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.fxymine4ever.main.imageloader.start.ImageLoader.TAG;

/**
 * create by:Fxymine4ever
 * time: 2019/2/5
 * 线程池工厂类，负责创建线程池
 */
public class ExecutorFactory {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();//计算机的CPU个数
    private static final int CORE_POOL_SIZE = CPU_COUNT * 10  + 1;//线程池核心线程数
    private static final int MAX_POOL_SIZE = CPU_COUNT * 20 + 1;//线程池最大线程数
    private static final long KEEP_ALIVE = 10L;//线程空闲最大时间

    private ExecutorFactory() {
    }

    public static Executor createExecutor() {
        return new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new ThreadFactory()
        );
    }
}
