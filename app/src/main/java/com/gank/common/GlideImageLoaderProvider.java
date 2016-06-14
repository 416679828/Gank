package com.gank.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.gank.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by LiXiaoWang
 */
public class GlideImageLoaderProvider extends BaseImageLoaderProvider {
    @Override
    public void loadImage(Context ctx, MyImageLoader img) {
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean isWiFiChecked = shp.getBoolean(ctx.getString(R.string.onlywifi), false);
        if (isWiFiChecked) {
            if (CommonUtils.getConnectedType(ctx) == CommonUtils.NETWORKTYPE_WIFI) {
                loadNormal(ctx, img);
            } else {
                loadCache(ctx, img);
            }

        }else {
            loadNormal(ctx,img);
        }


    }


    /**
     * load image with Glide
     */
    private void loadNormal(Context ctx, MyImageLoader img) {
        Glide.with(ctx).load(img.getUrl()).placeholder(img.getPlaceHolder()).
                diskCacheStrategy(img.getDiskCacheStrategy()).
                into(img.getImgView());
    }


    /**
     * load cache image with Glide
     */
    private void loadCache(Context ctx, MyImageLoader img) {
        Glide.with(ctx).using(new StreamModelLoader<String>() {
            @Override
            public DataFetcher<InputStream> getResourceFetcher(final String model, int i, int i1) {
                return new DataFetcher<InputStream>() {
                    @Override
                    public InputStream loadData(Priority priority) throws Exception {
                        throw new IOException();
                    }

                    @Override
                    public void cleanup() {

                    }

                    @Override
                    public String getId() {
                        return model;
                    }

                    @Override
                    public void cancel() {

                    }
                };
            }
        }).load(img.getUrl()).placeholder(img.getPlaceHolder()).diskCacheStrategy(DiskCacheStrategy.ALL).into(img.getImgView());
    }
}
