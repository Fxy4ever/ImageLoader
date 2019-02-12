package com.fxymine4ever.main.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fxymine4ever.imageloader.R;
import com.fxymine4ever.main.imageloader.ImageLoader;
import com.fxymine4ever.main.imageloader.config.RequestOptions;
import com.fxymine4ever.main.imageloader.strategy.cache.DoubleCache;


/**
 * create by:Fxymine4ever
 * time: 2019/2/12
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {
    private String[] urls;
    private Context context;

    public PhotoAdapter(String[] urls, Context context) {
        this.urls = urls;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        RequestOptions options = new RequestOptions()
                .url(urls[i])
                .cache(DoubleCache.getInstance(context))
                .error(R.drawable.ic_launcher_background)
                .placeHolder(R.drawable.ic_launcher_foreground)
                .scaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader
                .getInstance()
                .with(context)
                .load(options,myViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return urls.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv);
        }
    }
}
