package com.ljh.www.imkit.bindingadapter.swiperefreshlayout;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljh.www.imkit.command.RefreshCommand;
import com.ljh.www.imkit.util.log.LogUtils;

/**
 * Created by ljh on 2016/6/29.
 */
public class ViewBindingAdapter {

    private static String TAG = LogUtils.makeLogTag(ViewBindingAdapter.class.getSimpleName());
    @BindingAdapter({"onRefreshCommand"})
    public static void onRefreshCommand(SwipeRefreshLayout refreshLayout, final RefreshCommand onRefreshCommand) {
        LogUtils.LOGD(TAG, refreshLayout.getId() + "onRefreshCommand"+onRefreshCommand);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (null != onRefreshCommand) {
                    onRefreshCommand.execute();
                    LogUtils.LOGD(TAG, "onRefreshCommand 2");
                }
            }
        });

    }

}
