package com.fxymine4ever.imageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * create by:Fxymine4ever
 * time: 2019/2/6
 * Handler传递的实体类
 */
public class LoaderResultBean {
    private ImageView imageView;
    private String uri;

    public ImageView getImageView() {
        return imageView;
    }

    public String getUri() {
        return uri;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    private Bitmap bitmap;

    public LoaderResultBean(ImageView imageView, String uri, Bitmap bitmap) {
        this.imageView = imageView;
        this.uri = uri;
        this.bitmap = bitmap;
    }
}
