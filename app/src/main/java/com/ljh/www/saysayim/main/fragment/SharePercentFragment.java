package com.ljh.www.saysayim.main.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.common.collect.Lists;
import com.ljh.www.imkit.common.http.CustomTrust;
import com.ljh.www.imkit.common.http.GzipRequestInterceptor;
import com.ljh.www.imkit.common.http.LoggingInterceptor;
import com.ljh.www.imkit.common.http.RetrofitProvider;
import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.saysayim.ContactBinding;
import com.ljh.www.saysayim.R;
import com.ljh.www.saysayim.SharePercentBinding;
import com.ljh.www.saysayim.common.fragment.TabBaseFragment;
import com.ljh.www.saysayim.data.remote.RemoteDataService;
import com.ljh.www.saysayim.main.adapter.SharePercentAdapter;
import com.ljh.www.saysayim.main.viewmodel.SharePercentItemVM;
import com.ljh.www.saysayim.main.viewmodel.SharePercentVM;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CertificatePinner;
import okhttp3.ConnectionPool;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by ljh on 2016/5/30.
 */
public class SharePercentFragment extends TabBaseFragment<SharePercentVM, SharePercentBinding> {


    private static final String TAG = LogUtils.makeLogTag(SharePercentFragment.class.getSimpleName());
    private SharePercentAdapter sharePercentAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setBinding(DataBindingUtil.<SharePercentBinding>inflate(inflater, R.layout.fragment_share_percent, container, false));
        SharePercentVM sharePercentVM = new SharePercentVM(getActivity(), this);
        getBinging().setSharePercentVm(sharePercentVM);
        setViewModel(sharePercentVM);
        return getBinging().getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharePercentAdapter = new SharePercentAdapter(getActivity());
        getBinging().rlvSharePercent.setHasFixedSize(true);
        getBinging().rlvSharePercent.setLayoutManager(new LinearLayoutManager(getActivity()));
        getBinging().rlvSharePercent.setAdapter(sharePercentAdapter);

        final Action0 action0 = new Action0() {
            @Override
            public void call() {
                LogUtils.LOGD(TAG, "doOnSubscribe" + Thread.currentThread().getName());
            }
        };
        final Action1<List<SharePercentItemVM>> action1 = new Action1<List<SharePercentItemVM>>() {
            @Override
            public void call(List<SharePercentItemVM> sharePercentItemVMs) {
//                LogUtils.LOGD(TAG, sharePercentItemVMs.size() + Thread.currentThread().getName());
                getBinging().refreshLayout.setRefreshing(false);
                sharePercentAdapter.setData(sharePercentItemVMs);
            }
        };

        getBinging().refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                getBinging().refreshLayout.setRefreshing(true);
                getViewModel().loadSharePercent(action0, action1);
            }
        }, 50);

        getBinging().refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getViewModel().loadSharePercent(action0, action1);
                testSpdy();
            }
        });


        getBinging().rlvSharePercent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LogUtils.LOGD(TAG, "can scroll" + getBinging().rlvSharePercent.canScrollVertically(1));
                if (!getBinging().rlvSharePercent.canScrollVertically(1)) {
                    getViewModel().loadMoreSharePercent(action0, action1);
                }
            }
        });

//        Glide.with(this).load("http://7xog45.dl1.z0.glb.clouddn.com/my-python-logo.webp?e=1500090436&token=CqnMdvAgsZiHf0KGbsQP6GvJtR7sYTHYm1PmTsdU:JcdwafkrOWyaw1uhcKNJUijbcuc=").into(getBinging().iv);
    }


    private void testSpdy() {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("TLS");

            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }

        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory;
        try {
            CustomTrust customTrust = new CustomTrust();
            trustManager = customTrust.trustManagerForCertificates(customTrust.trustedCertificatesInputStream());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        List<Protocol> protocols = Lists.newArrayList();

//        protocols.add(Protocol.HTTP_2);
        protocols.add(Protocol.SPDY_3);
        protocols.add(Protocol.HTTP_1_1);


        OkHttpClient client = new OkHttpClient.Builder()
//                .sslSocketFactory(sslSocketFactory, trustManager)
                .addInterceptor(new GzipRequestInterceptor())
                .build();


        final Request request = new Request.Builder().url("https://t.assistant.120yibao.com/images/logo.png").build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.LOGD(TAG, " onFailure " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.LOGD(TAG, " onResponse " + response.isSuccessful() + response.message() + response.request().url().toString());
                Headers headers = request.headers();
                Iterator<String> it = headers.names().iterator();
                LogUtils.LOGD(TAG, "request headers" + request.headers().size()
                );
                while (it.hasNext()) {
                    String name = it.next();
                    LogUtils.LOGD(TAG, name + " = " + headers.get(name));
                }

                Headers headers1 = response.headers();
                Iterator<String> it1 = headers1.names().iterator();
                LogUtils.LOGD(TAG, "response headers");
                while (it1.hasNext()) {
                    String name = it1.next();
                    LogUtils.LOGD(TAG, name + " = " + headers1.get(name));
                }
                LogUtils.LOGD(TAG, response.protocol().toString() + "bytes" + Thread.currentThread().getName());
//                getBinging().iv.setImageBitmap(BitmapFactory.decodeStream( response.body().byteStream()));
            }
        });


    }

    @Override
    public void onCurrent() {
        super.onCurrent();
        LogUtils.LOGD(TAG, "onCurrent");
    }

    @Override
    public void onLeave() {
        super.onLeave();
        LogUtils.LOGD(TAG, "onLeave");
    }
}
