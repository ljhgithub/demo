package com.ljh.www.saysayim.main.viewmodel;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.databinding.ObservableField;

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
public class SharePercentItemVM extends ViewModel {

    private static final String TAG = LogUtils.makeLogTag(SharePercentItemVM.class.getSimpleName());
    private Context mContext;
    public SharePercentModel sharePercent;

    public ObservableField<String> code = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> fundnum = new ObservableField<>();
    public ObservableField<String> total = new ObservableField<>();

    public SharePercentItemVM(Context context, SharePercentModel sharePercent) {
        this.mContext = context;
        code.set(sharePercent.code);
        name.set(sharePercent.name);
        fundnum.set(sharePercent.fundnum);
        total.set(sharePercent.getTotal());

    }

}
