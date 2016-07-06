package com.ljh.www.saysayim.main.viewmodel;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.databinding.ObservableBoolean;


import com.google.common.collect.Lists;
import com.ljh.www.imkit.command.RefreshCommand;
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

    private static final String TAG = LogUtils.makeLogTag(SharePercentVM.class.getSimpleName());
    private Context mContext;
    private FragmentLifecycleProvider lifecycleProvider;

    public SharePercentVM(Context context) {
        this.mContext = context;

    }

    public SharePercentVM(Context context, FragmentLifecycleProvider lifecycleProvider) {
        this.mContext = context;
        this.lifecycleProvider = lifecycleProvider;
    }

    public List<SharePercentItemVM> loadSharePercents() {
        return query("0");
    }

    public void getRemoteData(Action0 action0, Action1<List<SharePercentModel>> action1) {
        final Observable<JuheRemoteDataSource> remoteData = RetrofitProvider.getRetrofitRxJava().create(RemoteDataService.class).getFundZCGJJ("http://web.juhe.cn:8080/fund/zcgjj/", Config.JUHE_APP_KEY);
        remoteData.subscribeOn(Schedulers.io())
                .doOnSubscribe(action0)
                .map(new Func1<JuheRemoteDataSource, List<SharePercentModel>>() {
                    @Override
                    public List<SharePercentModel> call(JuheRemoteDataSource remoteDataModel) {
//                        LogUtils.LOGD(TAG, Thread.currentThread().getName() + remoteDataModel.result);
                        if (JuheRemoteDataSource.JUHE_SUCCESS.equalsIgnoreCase(remoteDataModel.resultcode)) {
                            List<SharePercentModel> sharePercentModels = SharePercentModel.buildListFrom(remoteDataModel.result);
//                            insertAll(sharePercentModels);
                            return sharePercentModels;
                        }

                        return Lists.newArrayList();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

    private List<SharePercentItemVM> query(String start) {

        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(FINContact.SharePercent.CONTENT_URI, null, null, null, null);

        List<SharePercentItemVM> sharePercentVms = Lists.newArrayList();
        SharePercentModel sharePercent;
        while (cursor.moveToNext()) {
            sharePercent = new SharePercentModel();
            sharePercent.code = cursor.getString(cursor.getColumnIndex(FINContact.SharePercentColumns.CODE));
            sharePercent.name = cursor.getString(cursor.getColumnIndex(FINContact.SharePercentColumns.NAME));
            sharePercent.setTotal(cursor.getString(cursor.getColumnIndex(FINContact.SharePercentColumns.FUND_NUM)));
            sharePercent.setChange(cursor.getString(cursor.getColumnIndex(FINContact.SharePercentColumns.CHANGE)));
            sharePercent.totalcap = cursor.getString(cursor.getColumnIndex(FINContact.SharePercentColumns.TOTAL_CAP));
            sharePercent.accrate = cursor.getString(cursor.getColumnIndex(FINContact.SharePercentColumns.ACC_RATE));
            sharePercent.changesta = cursor.getString(cursor.getColumnIndex(FINContact.SharePercentColumns.CHANGE_STATUS));
            sharePercent.time = cursor.getString(cursor.getColumnIndex(FINContact.SharePercentColumns.TIME));
            sharePercentVms.add(new SharePercentItemVM(mContext, sharePercent));
        }
        LogUtils.LOGD(TAG, "size" + sharePercentVms.size());
        return sharePercentVms;
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
