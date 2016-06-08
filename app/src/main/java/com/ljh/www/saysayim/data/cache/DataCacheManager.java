package com.ljh.www.saysayim.data.cache;

import com.ljh.www.imkit.util.log.LogUtils;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;

/**
 * Created by ljh on 2016/6/8.
 */
public class DataCacheManager {


    private static final String TAG = LogUtils.makeLogTag(DataCacheManager.class.getSimpleName());

    /**
     * 异步构建缓存数据
     */
    public static void buildDataCacheAsync(Action1 action1) {

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                buildDataCache();
                subscriber.onNext("next");
                LogUtils.LOGD(TAG, "buildDataCacheAsync" + Thread.currentThread().getName());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

    public static void buildDataCache() {
        clearDataCache();
        FriendDataCache.getInstance().buildCache();
    }

    public static void clearDataCache() {
        FriendDataCache.getInstance().clearCache();
    }

}
