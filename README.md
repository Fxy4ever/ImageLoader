# ImageLoader
对三级缓存进行了封装解耦<br>
使用如下
```
        RequestOptions options = new RequestOptions()
                .url(urls[i])//图片的地址
                .cache(DoubleCache.getInstance(context))//缓存策略(鄙人认为压缩策略应该在缓存策略中设置比较合适
                .error(R.drawable.ic_launcher_background)//错误图
                .placeHolder(R.drawable.ic_launcher_foreground)//占位图
                .scaleType(ImageView.ScaleType.CENTER_CROP);//ScaleType
                
        ImageLoader
                .getInstance()//获取单利
                .with(context)//获取Context
                .load(options,myViewHolder.imageView);//加载图片
```
