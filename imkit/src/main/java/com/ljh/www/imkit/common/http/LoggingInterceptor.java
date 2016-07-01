package com.ljh.www.imkit.common.http;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ljh on 2016/4/24.
 */
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        long t1 = System.nanoTime();
        Request request = chain.request().newBuilder().tag("r").build();
        Log.i("tag", String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        Log.i("tag", String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, /*response.headers()*/" "));

//        return response.newBuilder()
//                .header("Cache-Control", "max-age=60")
//                .build();
        return response;
    }
}
