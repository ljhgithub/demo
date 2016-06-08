package com.ljh.www.saysayim.main.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljh.www.saysayim.ContactBinding;
import com.ljh.www.saysayim.common.fragment.TabBaseFragment;
import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.saysayim.R;
import com.ljh.www.saysayim.common.viewmode.ViewModel;
import com.ljh.www.saysayim.main.adapter.ContactAdapter;

/**
 * Created by ljh on 2016/5/30.
 */
public class ContactFragment extends TabBaseFragment<ViewModel, ContactBinding> {
    private static final String TAG = LogUtils.makeLogTag(ContactFragment.class.getSimpleName());
    private LinearLayoutManager llm;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setBinding(DataBindingUtil.<ContactBinding>inflate(inflater, R.layout.fragment_contact, container, false));
        llm = new LinearLayoutManager(getActivity());
        return getBinging().getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinging().rlvContact.setHasFixedSize(true);
        getBinging().rlvContact.setLayoutManager(llm);
        getBinging().rlvContact.setAdapter(new ContactAdapter(getActivity()));

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
