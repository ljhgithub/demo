package com.ljh.www.saysayim.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljh.www.saysayim.R;

/**
 * Created by ljh on 2016/5/27.
 */
public class FriendsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private LayoutInflater mInflatar;

    public FriendsAdapter(Context context) {
        mContext = context;
        mInflatar = LayoutInflater.from(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendHolder(mInflatar.inflate(R.layout.item_friend, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    protected static class FriendHolder extends RecyclerView.ViewHolder {

        public FriendHolder(View itemView) {
            super(itemView);
        }
    }
}
