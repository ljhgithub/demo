package com.ljh.www.imkit.common.http;

import android.util.Log;

import com.ljh.www.imkit.AppHelper;
import com.ljh.www.imkit.util.sys.NetworkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ljh on 2016/4/25.
 */
public class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtil.isNetAvailable(AppHelper.getContext())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        Log.d("tag","暂无网络");
        }

        Response response = chain.proceed(request);
        if (NetworkUtil.isNetAvailable(AppHelper.getContext())) {
            int maxAge = 60 * 60; // read from cache for 1 minute
         response=   response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
          response=response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return response;
    }









}
