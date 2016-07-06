package com.ljh.www.saysayim.main.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.saysayim.ContactBinding;
import com.ljh.www.saysayim.R;
import com.ljh.www.saysayim.SharePercentBinding;
import com.ljh.www.saysayim.common.fragment.TabBaseFragment;
import com.ljh.www.saysayim.main.adapter.SharePercentAdapter;
import com.ljh.www.saysayim.main.model.SharePercentModel;
import com.ljh.www.saysayim.main.viewmodel.SharePercentVM;

import java.util.List;

import rx.functions.Action0;
import rx.functions.Action1;

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
        SharePercentVM sharePercentVM = new SharePercentVM(getActivity());
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
        final Action1<List<SharePercentModel>> action1 = new Action1<List<SharePercentModel>>() {
            @Override
            public void call(List<SharePercentModel> sharePercentModels) {

                LogUtils.LOGD(TAG, sharePercentModels.size() + Thread.currentThread().getName());
                getBinging().refreshLayout.setRefreshing(false);
            }
        };


        getBinging().refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                getBinging().refreshLayout.setRefreshing(true);
                getViewModel().getRemoteData(action0, action1);
            }
        }, 50);
        getBinging().refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sharePercentAdapter.setData(getViewModel().loadSharePercents());
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
