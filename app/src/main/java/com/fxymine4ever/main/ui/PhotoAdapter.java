package com.fxymine4ever.main.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fxymine4ever.imageloader.R;
import com.fxymine4ever.main.imageloader.start.ImageLoader;
import com.fxymine4ever.main.imageloader.strategy.cache.DoubleCache;
import com.fxymine4ever.main.imageloader.strategy.cache.MemoryCache;


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
        ImageLoader loader = ImageLoader.getInstance();
        loader.setmCache(DoubleCache.getInstance(context));
        loader.setmContext(context);
        loader.bindBitmap(urls[i],myViewHolder.imageView);
//        ImageLoader.with(context).load(urls[i]).into(myViewHolder.imageView).display();

//        Glide.with(context).load(urls[i]).into(myViewHolder.imageView);
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
