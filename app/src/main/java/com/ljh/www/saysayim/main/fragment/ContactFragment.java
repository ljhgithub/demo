package com.ljh.www.saysayim.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ljh.www.imkit.TabBaseFragment;
import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.saysayim.R;


/**
 * Created by ljh on 2016/5/30.
 */
public class ContactFragment extends TabBaseFragment {

    private static final String TAG = LogUtils.makeLogTag(ContactFragment.class.getSimpleName());

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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
