package com.fxymine4ever.main.imageloader.start;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.fxymine4ever.imageloader.R;
import com.fxymine4ever.main.imageloader.factory.ExecutorFactory;
import com.fxymine4ever.main.imageloader.strategy.cache.ImageCache;
import com.fxymine4ever.main.imageloader.util.BitmapUtil;
import com.fxymine4ever.main.imageloader.util.MD5Util;

import java.util.concurrent.Executor;

/**
 * create by:Fxymine4ever
 * time: 2019/2/5
 * 图片加载核心类
 */
public class ImageLoader {
    public static final String TAG = "FXYImageLoader";
    private static final int MESSAGE_POST_RESULT = 1;
    private static final int TAG_KEY_URI = R.id.imageloader_uri;

    private static ImageLoader imageLoader;
    private ImageCache mCache;
    private Context mContext;

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    //双校验单例
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

    public void setmCache(ImageCache mCache) {
        this.mCache = mCache;
    }

    private ImageLoader() {
    }

    private Executor THREAD_POOL_EXECUTOR = ExecutorFactory.createExecutor();

    /*
    异步加载图片
     */
    private void bindBitmap(final String url, final ImageView imageView,
                            final int reqWidth, final int reqHeight) {
        String tag = MD5Util.UrltoMd5(url) + System.currentTimeMillis();
        imageView.setTag(TAG_KEY_URI, tag);
        Runnable loadBitmapTask = () -> {
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            imageView.measure(w, h);
            int height = imageView.getMeasuredHeight();
            int width = imageView.getMeasuredWidth();

            Bitmap bitmap1 = mCache.get(url,width,height);//调用同步加载的方法
            if (bitmap1 != null) {
                ((Activity) mContext).runOnUiThread(() -> {
                    String mTag = (String) imageView.getTag(TAG_KEY_URI);
                    if (mTag.equals(tag)) {
                        imageView.setImageBitmap(bitmap1);
                    }
                });
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }


    public void bindBitmap(final String uri, final ImageView imageView) {
        bindBitmap(uri, imageView, imageView.getWidth(), imageView.getHeight());
    }
}

