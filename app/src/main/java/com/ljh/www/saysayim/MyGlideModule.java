package com.ljh.www.saysayim;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

/**
 * Created by ljh on 2016/5/3.
 */
public class MyGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        // Do nothing.
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
}
