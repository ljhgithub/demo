package com.ljh.www.saysayim.main.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.common.collect.Lists;
import com.ljh.www.saysayim.ContactItemBinding;
import com.ljh.www.saysayim.R;
import com.ljh.www.saysayim.SharePercentItemBinding;
import com.ljh.www.saysayim.main.viewmodel.ContactItemVM;
import com.ljh.www.saysayim.main.viewmodel.SharePercentItemVM;

import java.util.List;

/**
 * Created by ljh on 2016/6/2.
 */
public class SharePercentAdapter extends RecyclerView.Adapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<SharePercentItemVM> sharePercentItemVMs;

    public SharePercentAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        sharePercentItemVMs = Lists.newArrayList();
    }

    public void setData(List<SharePercentItemVM> sharePercentItemVMs) {
        if (null != sharePercentItemVMs && sharePercentItemVMs.size() > 0) {
            this.sharePercentItemVMs.addAll(sharePercentItemVMs);
            notifyDataSetChanged();

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SharePercentItemBinding binding = DataBindingUtil.inflate(mInflater, R.layout.item_share_percent, null, false);
        return new SharePercentHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SharePercentHolder sharePercentHolder = (SharePercentHolder) holder;
        sharePercentHolder.binding.setSharePercentItemVm(sharePercentItemVMs.get(position));
    }

    @Override
    public int getItemCount() {
        return sharePercentItemVMs.size();
    }

    protected static class SharePercentHolder extends RecyclerView.ViewHolder {
        public SharePercentItemBinding binding;

        public SharePercentHolder(SharePercentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
