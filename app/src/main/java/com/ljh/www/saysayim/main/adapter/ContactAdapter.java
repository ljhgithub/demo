package com.ljh.www.saysayim.main.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ljh.www.saysayim.ContactItemBinding;
import com.ljh.www.saysayim.R;
import com.ljh.www.saysayim.main.viewmodel.ContactItemVM;

/**
 * Created by ljh on 2016/6/2.
 */
public class ContactAdapter extends RecyclerView.Adapter {
    private LayoutInflater mInflater;
    private Context mContext;

    public ContactAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ContactItemBinding binding = DataBindingUtil.inflate(mInflater, R.layout.item_contact, null, false);
        return new ContactHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ContactHolder contactHolder= (ContactHolder) holder;
        contactHolder.binding.setContact(new ContactItemVM(mContext));
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    protected static class ContactHolder extends RecyclerView.ViewHolder {
        public ContactItemBinding binding;
        public ContactHolder(ContactItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
