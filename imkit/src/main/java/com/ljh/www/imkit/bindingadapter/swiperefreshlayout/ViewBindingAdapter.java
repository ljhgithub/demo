package com.ljh.www.imkit.bindingadapter.swiperefreshlayout;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;

import com.ljh.www.imkit.command.RefreshCommand;
import com.ljh.www.imkit.util.log.LogUtils;

/**
 * Created by ljh on 2016/6/29.
 */
public class ViewBindingAdapter {

    private static String TAG = LogUtils.makeLogTag(ViewBindingAdapter.class.getSimpleName());

    @BindingAdapter({"onRefreshCommand"})
    public static void onRefreshCommand(SwipeRefreshLayout refreshLayout, final RefreshCommand refreshCommand) {
        LogUtils.LOGD(TAG, "onRefreshCommand");
        if (null!=refreshCommand){

        }

    }
}
