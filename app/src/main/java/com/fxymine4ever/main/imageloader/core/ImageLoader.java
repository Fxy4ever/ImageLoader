package com.fxymine4ever.main.imageloader.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.fxymine4ever.main.imageloader.config.RequestOptions;
import com.fxymine4ever.main.imageloader.factory.ExecutorFactory;
import com.fxymine4ever.main.imageloader.util.BitmapUtil;
import com.fxymine4ever.main.imageloader.util.MD5Util;
import com.fxymine4ever.main.imageloader.util.ViewUtil;

import java.util.concurrent.Executor;

/**
 * create by:Fxymine4ever
 * time: 2019/2/5
 * 图片加载核心类
 */
public class ImageLoader {
    public static final String TAG = "FXYImageLoader";
    private static final int TAG_KEY_URI = 123456789;//
    private Executor THREAD_POOL_EXECUTOR = ExecutorFactory.createExecutor();

    private static ImageLoader imageLoader;
    private Context context;



    public static ImageLoader getInstance() {
        if (imageLoader == null) {
            synchronized (ImageLoader.class) {
                if (imageLoader == null) {
                    imageLoader = new ImageLoader();
                }
            }
        }
        return imageLoader;
    }


    private ImageLoader() {
    }


    public ImageLoader with(Context context) {
        this.context = context;
        return this;
    }

    /*
    异步加载图片
     */
    public void load(RequestOptions options,ImageView imageView) {
        String tag = MD5Util.UrltoMd5(options.getUrl()) + System.currentTimeMillis();

        if (options.getPlaceHolder() > 0) {//占位图
            imageView.setBackgroundResource(options.getPlaceHolder());
        }
        if (options.getType() != null) {//如果设置了ScaleType
            imageView.setScaleType(options.getType());
        }
        Runnable loadBitmapTask = () -> {
            imageView.setTag(TAG_KEY_URI, tag);
            int[] res = ViewUtil.measureView(imageView);
            Bitmap bitmap1 = options.getLoader().load(options.getUrl(),options.getCache(), res[0], res[1]);

            if (bitmap1 != null) {
                ((Activity) context).runOnUiThread(() -> {
                    String mTag = (String) imageView.getTag(TAG_KEY_URI);
                    if (mTag.equals(tag)) {
                        imageView.setImageBitmap(bitmap1);
                    }
                });
            } else {
                if (options.getError() > 0) {//错误图
                    imageView.setBackgroundResource(options.getError());
                }else{
                    imageView.setImageBitmap(BitmapUtil.getEmptyBitmap());
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }
}

