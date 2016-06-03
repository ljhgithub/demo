package com.ljh.www.saysayim.search.activity;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.saysayim.FriendSearchBinding;
import com.ljh.www.saysayim.R;
import com.ljh.www.saysayim.base.BaseActivity;
import com.ljh.www.saysayim.base.ViewModel;
import com.ljh.www.saysayim.search.viewmodel.FriendSearchVM;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;

/**
 * Created by ljh on 2016/6/3.
 */
public class FriendSearchActivity extends BaseActivity<ViewModel, FriendSearchBinding> {

    private static final String TAG = LogUtils.makeLogTag(FriendSearchActivity.class.getSimpleName());

    public static void start(Context context) {
        Intent it = new Intent(context, FriendSearchActivity.class);
        context.startActivity(it);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBinding(DataBindingUtil.<FriendSearchBinding>setContentView(this, R.layout.activity_search_friend));

        setTitleName("添加好友");
        getBinging().etSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_GO == actionId) {
                    addFriend(getBinging().etSearchContent.getText().toString(), "我是" + getBinging().etSearchContent.getText().toString());
                }
                return false;
            }
        });
    }

    private void addFriend(String account, String postscript) {
        NIMClient.getService(FriendService.class).addFriend(new AddFriendData(account, VerifyType.DIRECT_ADD, postscript)).setCallback(new RequestCallbackWrapper<Void>() {
            @Override
            public void onResult(int i, Void aVoid, Throwable throwable) {
                LogUtils.LOGD(TAG, i + "");
            }
        });
    }
}
