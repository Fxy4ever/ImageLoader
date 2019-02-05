package com.fxymine4ever.main.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.fxymine4ever.imageloader.R;
import com.fxymine4ever.main.imageloader.factory.ExecutorFactory;
import com.fxymine4ever.main.imageloader.util.FileUtil;
import com.fxymine4ever.main.imageloader.util.MD5Util;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;

/**
 * create by:Fxymine4ever
 * time: 2019/2/5
 * 图片加载核心类
 */
public class ImageLoader {
    public static final String TAG = "FXYImageLoader";

    private static final int MESSAGE_POST_RESULT = 1;

    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 50;
    private static final int DISK_CACHE_INDEX = 0;
    private static final int IO_BUFFER_SIZE = 8 * 1024;
    private static final int TAG_KEY_URI = R.id.imageloader_uri;
    private boolean isDiskLruCacheCreated = false;


    private static ImageLoader imageLoader;
    private ImageResizer resizer = new ImageResizer();
    private LruCache<String, Bitmap> memoryCache;
    private DiskLruCache diskLruCache;
    private Executor THREAD_POOL_EXECUTOR = ExecutorFactory.createExecutor();
    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            LoaderResultBean result = (LoaderResultBean) msg.obj;
            ImageView imageView = result.getImageView();
            imageView.setImageBitmap(result.getBitmap());
            String uri = (String) imageView.getTag(TAG_KEY_URI);
            if (uri.equals(result.getUri())) {
                imageView.setImageBitmap(result.getBitmap());
            } else {
                Log.d(TAG, "set image bitmap,but url has changed,ignored");
            }
        }
    };

    //双校验单例
    public static ImageLoader getInstance(Context context) {
        if(imageLoader == null){
            synchronized (ImageLoader.class){
                if(imageLoader == null){
                    imageLoader = new ImageLoader(context);
                }
            }
        }
        return imageLoader;
    }

    private ImageLoader(Context context) {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);//转kb
        int cacheSize = maxMemory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
        File diskCacheDir = FileUtil.getDiskCacheDir(context, "bitmap");
        if (!diskCacheDir.exists())
            diskCacheDir.mkdirs();

        if (FileUtil.getUsableSpace(diskCacheDir) >= DISK_CACHE_SIZE) {//检测可用空间大小
            try {
                diskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                isDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /*
    异步加载接口设计
     */
    private void bindBitmap(final String uri, final ImageView imageView,
                            final int reqWidth, final int reqHeight) {
        imageView.setTag(TAG_KEY_URI, uri);
        Bitmap bitmap = loadBitmapFromMemoryCache(uri);
        if (bitmap != null) {//首先从内存中找
            imageView.setImageBitmap(bitmap);
            return;
        }

        Runnable loadBitmapTask = () -> {
            Bitmap bitmap1 = loadBitmap(uri, reqWidth, reqHeight);//调用同步加载的方法
            if (bitmap1 != null) {
                LoaderResultBean result = new LoaderResultBean(imageView, uri, bitmap1);
                mMainHandler.obtainMessage(MESSAGE_POST_RESULT, result)
                        .sendToTarget();
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    /*
    同步加载接口设计
     */
    public Bitmap loadBitmap(String uri, int reqWidth, int reqHeight) {
        Bitmap bitmap = loadBitmapFromMemoryCache(uri);//从内存中加载
        if (bitmap != null) {
            Log.d(TAG, "Load from memory:" + uri);
            return bitmap;
        }
        try {
            bitmap = loadBitmapFromDiskCache(uri, reqWidth, reqHeight);//从磁盘中加载
            if (bitmap != null) {
                Log.d(TAG, "Load from disk:" + uri);
                return bitmap;
            }
            bitmap = loadBitmapFromHttp(uri, reqWidth, reqHeight);//从网络中下载
            Log.d(TAG, "Load from netWork:" + uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap == null && !isDiskLruCacheCreated) {//若DiskLruCache未创建，则直接返回Bitmap不进行缓存
            bitmap = downloadBitmapFromUrl(uri);
            Log.d(TAG, "encounter error,DiskLruCache is not created");
        }
        return bitmap;
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemoryCache(String key) {
        return memoryCache.get(key);
    }

    private Bitmap loadBitmapFromMemoryCache(String url) {
        final String key = MD5Util.UrltoMd5(url);
        return getBitmapFromMemoryCache(key);
    }

    private Bitmap loadBitmapFromDiskCache(String url, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper())
            throw new RuntimeException("Don't load bitmap from UI Thread");

        if (diskLruCache == null)
            return null;

        Bitmap bitmap = null;
        String key = MD5Util.UrltoMd5(url);

        DiskLruCache.Snapshot snapshot = diskLruCache.get(key);//利用snapshot得到缓存的文件输入流
        if (snapshot != null) {
            FileInputStream inputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor descriptor = inputStream.getFD();
            bitmap = resizer.decodeSampledBitmapFromFileDescriptor(descriptor, reqWidth, reqHeight);//压缩图片
            if (bitmap != null)
                addBitmapToMemoryCache(key, bitmap);//添加至内存
        }

        return bitmap;
    }

    public void bindBitmap(final String uri, final ImageView imageView) {
        bindBitmap(uri, imageView, imageView.getWidth(), imageView.getHeight());
    }




    private Bitmap loadBitmapFromHttp(String url, int reqWidth, int reqHeight) {
        if (Looper.myLooper() == Looper.getMainLooper())
            throw new RuntimeException("Can't visit network from UI Thread");

        if (diskLruCache == null)
            return null;

        String key = MD5Util.UrltoMd5(url);
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            if (editor != null) {//若该url的图片，未进行缓存，先下载图片
                OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
                if (downloadUrlToSteam(url, outputStream)) {//网络下载后，利用DiskLruCache的steam进行存储图片
                    editor.commit();//提交存储
                } else {
                    editor.abort();//回退
                }
                diskLruCache.flush();
            }
            return loadBitmapFromDiskCache(url, reqWidth, reqHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean downloadUrlToSteam(String urlString, OutputStream os) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(os, IO_BUFFER_SIZE);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "downloadBitmap failed." + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private Bitmap downloadBitmapFromUrl(String urlString) {
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            Log.e(TAG, "Error in downloadBitmap:" + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}

