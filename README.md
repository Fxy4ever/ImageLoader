# ImageLoader
对三级缓存进行了封装解耦
<br>
使用如下
```java
        RequestOptions options = new RequestOptions.Builder()
                .url(urls[i])
                .cache(MemoryCache.getInstance())
                .error(R.drawable.ic_launcher_background)
                .placeHolder(R.drawable.ic_launcher_foreground)
                .scaleType(ImageView.ScaleType.CENTER_CROP)
                .build();

        ImageLoader
                .getInstance()
                .with(context)
                .load(options,myViewHolder.imageView););//加载图片
```
