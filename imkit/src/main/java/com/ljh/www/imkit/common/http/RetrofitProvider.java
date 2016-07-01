package com.ljh.www.imkit.common.http;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ljh on 2016/4/20.
 */
public class RetrofitProvider {
    public static final String BASE_URL = "http://news-at.zhihu.com/";
    private static Retrofit retrofit;
    private static OkHttpClient client;

    public static Retrofit getRetrofit() {
        if (null == retrofit) {
            OkHttpClient client = buildClient();
            Retrofit instance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            return instance;
        }
        return retrofit;
    }

    @NonNull
    private static OkHttpClient buildClient() {
        if (null == client) {
            return new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .addInterceptor(new LoggingInterceptor())
                    .build();
        } else {
            return client;
        }
    }

    public static Retrofit getRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(buildClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getRetrofitRxJava(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(buildClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static Retrofit getRetrofitRxJava() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(buildClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
