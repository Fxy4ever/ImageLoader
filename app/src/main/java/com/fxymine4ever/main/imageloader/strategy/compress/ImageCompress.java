package com.fxymine4ever.main.imageloader.strategy.compress;

import android.graphics.Bitmap;

/**
 * create by:Fxymine4ever
 * time: 2019/2/12
 * 压缩策略接口
 * 鄙人觉得在缓存策略下载完成的时候使用比较好
 */
public interface ImageCompress {
    Bitmap compress(Bitmap bitmap,int width,int height);
}
