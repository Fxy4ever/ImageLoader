package com.fxymine4ever.main.imageloader.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.fxymine4ever.main.imageloader.ImageLoader.TAG;

/**
 * create by:Fxymine4ever
 * time: 2019/2/12
 * 利用OkHttp或者HttpURLConnection获取Bitmap
 */
public class NetUtil {
    private static OkHttpClient client = new OkHttpClient();

    public static Bitmap downloadBitmapFromUrl(String path) {
        InputStream is = null;
        Bitmap bitmap = null;
        HttpURLConnection connection = null;
        Log.d(TAG, path + "ss  ");
        try {
            URL url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            if (bitmap != null) {
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(is);
        }
        return bitmap;
    }

    public static Bitmap downloadBitmapFromUrlWithOkHttp(String path) {
        Request request = new Request.Builder().url(path).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                InputStream is = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
