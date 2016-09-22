package com.ljh.www.saysayim.main.viewmodel;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


import com.google.common.collect.Lists;
import com.ljh.www.imkit.common.http.RetrofitProvider;
import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.saysayim.Config;
import com.ljh.www.saysayim.common.viewmode.ViewModel;
import com.ljh.www.saysayim.data.provider.FINContact;
import com.ljh.www.saysayim.data.remote.RemoteDataService;
import com.ljh.www.saysayim.main.model.SharePercentModel;
import com.ljh.www.saysayim.model.JuheRemoteDataSource;
import com.trello.rxlifecycle.FragmentLifecycleProvider;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ljh on 2016/6/29.
 */
public class SharePercentVM extends ViewModel {

    private int curPosition = 0;
    private static final int OFFSET = 20;
    private static final String TAG = LogUtils.makeLogTag(SharePercentVM.class.getSimpleName());
    private Context mContext;
    private boolean isLast = false;
    private FragmentLifecycleProvider lifecycleProvider;

    public SharePercentVM(Context context, FragmentLifecycleProvider lifecycleProvider) {
        this.mContext = context;
        this.lifecycleProvider = lifecycleProvider;
    }


    public void loadSharePercent(final Action0 action0, final Action1<List<SharePercentItemVM>> action1) {
        curPosition = 0;
        buildSharePercent(curPosition, OFFSET, action0, action1);

    }

    public void loadMoreSharePercent(final Action0 action0, final Action1<List<SharePercentItemVM>> action1) {
        buildSharePercent(curPosition * OFFSET, OFFSET, action0, action1);

    }

    public void buildSharePercent(final int start, final int offset, final Action0 action0, final Action1<List<SharePercentItemVM>> action1) {
        query(start, offset, action0, new Action1<List<SharePercentItemVM>>() {
            @Override
            public void call(List<SharePercentItemVM> sharePercentItemVMs) {
                if (null == sharePercentItemVMs || sharePercentItemVMs.size() == 0) {
                    if (start == 0 && !isLast) {
                        getRemoteData(new Action1<Integer>() {
                            @Override
                            public void call(Integer count) {
                                query(start, offset, new Action0() {
                                    @Override
                                    public void call() {
                                        LogUtils.LOGD(TAG, "fetch share percent by jehu'server! " + Thread.currentThread().getName());
                                    }
                                }, action1);
                            }
                        });
                    } else {
                        isLast = true;
                    }

                } else {
                    isLast = false;
                    curPosition++;
                    action1.call(sharePercentItemVMs);
                }
            }
        });
    }

    /**
     * 获取服务端数据 尽量调用
     *
     * @param action0
     * @param action1
     */
    public void getRemoteData(Action0 action0, Action1<List<SharePercentModel>> action1) {
        final Observable<JuheRemoteDataSource> remoteData = RetrofitProvider.getRetrofitRxJava().create(RemoteDataService.class).getFundZCGJJ("http://web.juhe.cn:8080/fund/zcgjj/", Config.JUHE_APP_KEY);
        remoteData.subscribeOn(Schedulers.io())
                .doOnSubscribe(action0)
                .map(new Func1<JuheRemoteDataSource, List<SharePercentModel>>() {
                    @Override
                    public List<SharePercentModel> call(JuheRemoteDataSource remoteDataModel) {
                        LogUtils.LOGD(TAG, Thread.currentThread().getName() + remoteDataModel.result);
                        if (JuheRemoteDataSource.JUHE_SUCCESS.equalsIgnoreCase(remoteDataModel.resultcode)) {
                            List<SharePercentModel> sharePercentModels = SharePercentModel.buildListFrom(remoteDataModel.result);
                            insertAll(sharePercentModels);
                            return sharePercentModels;
                        }

                        return Lists.newArrayList();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

    public void getRemoteData(Action1<Integer> action1) {
        final Observable<JuheRemoteDataSource> remoteData = RetrofitProvider.getRetrofitRxJava().create(RemoteDataService.class).getFundZCGJJ("http://web.juhe.cn:8080/fund/zcgjj/", Config.JUHE_APP_KEY);
        remoteData.subscribeOn(Schedulers.io())
                .map(new Func1<JuheRemoteDataSource, Integer>() {
                    @Override
                    public Integer call(JuheRemoteDataSource remoteDataModel) {
                        LogUtils.LOGD(TAG, Thread.currentThread().getName() + remoteDataModel.result);
                        if (JuheRemoteDataSource.JUHE_SUCCESS.equalsIgnoreCase(remoteDataModel.resultcode)) {
                            List<SharePercentModel> sharePercentModels = SharePercentModel.buildListFrom(remoteDataModel.result);
                            insertAll(sharePercentModels);
                            return sharePercentModels.size();
                        }

                        return 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

    private void query(int start, int offset, Action0 action0, Action1<List<SharePercentItemVM>> action1) {

        int[] params = new int[]{start, offset};
        Observable.just(params)
                .doOnSubscribe(action0)
                .subscribeOn(Schedulers.io())
                .compose(lifecycleProvider.<int[]>bindToLifecycle())
                .map(new Func1<int[], List<SharePercentItemVM>>() {
                    @Override
                    public List<SharePercentItemVM> call(int[] integers) {
                        Cursor cursor = mContext.getContentResolver().query(FINContact.SharePercent.buildSharePercentLimitUri(integers[0], integers[1]), null, null, null, null);
                        List<SharePercentItemVM> sharePercentVms = Lists.newArrayList();
                        SharePercentModel sharePercent;

                        while (cursor.moveToNext()) {
                            sharePercent = new SharePercentModel();
                            sharePercent.code = cursor.getString(cursor.getColumnIndex(FINContact.SharePercentColumns.CODE));
                            sharePercent.name = cursor.getString(cursor.getColumnIndex(FINContact.SharePercentColumns.NAME));
                            sharePercent.fundnum = cursor.getString(cursor.getColumnIndex(FINContact.SharePercentColumns.FUND_NUM));
                            sharePercent.setTotal(cursor.getString(cursor.getColumnIndex(FINContact.SharePercentColumns.TOTAL)));
                            sharePercent.setChange(cursor.getString(cursor.getColumnIndex(FINContact.SharePercentColumns.CHANGE)));
                            sharePercent.totalcap = cursor.getString(cursor.getColumnIndex(FINContact.SharePercentColumns.TOTAL_CAP));
                            sharePercent.accrate = cursor.getString(cursor.getColumnIndex(FINContact.SharePercentColumns.ACC_RATE));
                            sharePercent.changesta = cursor.getString(cursor.getColumnIndex(FINContact.SharePercentColumns.CHANGE_STATUS));
                            sharePercent.time = cursor.getString(cursor.getColumnIndex(FINContact.SharePercentColumns.TIME));
                            sharePercentVms.add(new SharePercentItemVM(mContext, sharePercent));
//                            LogUtils.LOGD(TAG, "size" + JSON.toJSONString(sharePercent));
                        }
                        LogUtils.LOGD(TAG, "query share percent form database! " + Thread.currentThread().getName());

                        return sharePercentVms;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);


    }

    private void insertAll(List<SharePercentModel> sharePercentModels) {
        ContentResolver cr = mContext.getContentResolver();
        ContentValues values;
        ContentValues[] contentValues = new ContentValues[sharePercentModels.size()];
        SharePercentModel model;
        for (int i = 0; i < sharePercentModels.size(); i++) {
            values = new ContentValues();
            model = sharePercentModels.get(i);
            values.put(FINContact.SharePercent.CODE, model.code);
            values.put(FINContact.SharePercent.NAME, model.name);
            values.put(FINContact.SharePercent.FUND_NUM, model.fundnum);
            values.put(FINContact.SharePercent.TOTAL, model.getTotal());
            values.put(FINContact.SharePercent.CHANGE, model.getChange());
            values.put(FINContact.SharePercent.TOTAL_CAP, model.totalcap);
            values.put(FINContact.SharePercent.ACC_RATE, model.accrate);
            values.put(FINContact.SharePercent.CHANGE_STATUS, model.changesta);
            values.put(FINContact.SharePercent.TIME, model.time);
            contentValues[i] = values;
        }
        cr.bulkInsert(FINContact.SharePercent.CONTENT_URI, contentValues);
    }

}
