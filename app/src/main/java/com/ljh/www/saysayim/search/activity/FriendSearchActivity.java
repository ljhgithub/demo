package com.ljh.www.saysayim.search.activity;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.saysayim.FriendSearchBinding;
import com.ljh.www.saysayim.R;
import com.ljh.www.saysayim.common.activity.BaseActivity;
import com.ljh.www.saysayim.common.viewmode.ViewModel;
import com.ljh.www.saysayim.user.activity.UserProfileActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

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
                    if (!TextUtils.isEmpty(getBinging().etSearchContent.getText().toString().trim())) {
                        searchAccount(getBinging().etSearchContent.getText().toString());
                        return true;
                    }

                }
                return false;
            }
        });
    }


    private void searchAccount(String account) {
        if (TextUtils.isEmpty(account)) {
            return;
        }
        List<String> accounts = new ArrayList<>(1);
        accounts.add(account);

        NIMClient.getService(UserService.class).fetchUserInfo(accounts).setCallback(new RequestCallbackWrapper<List<NimUserInfo>>() {
            @Override
            public void onResult(int code, List<NimUserInfo> users, Throwable throwable) {
                if (code == ResponseCode.RES_SUCCESS && users != null && !users.isEmpty()) {
//                    userInfo= users.get(0);
                    LogUtils.LOGD(TAG, users.get(0).getName() + users.get(0).getAccount());
                    UserProfileActivity.start(FriendSearchActivity.this, users.get(0));

                    //TODO
                    // 这里不需要更新缓存，由监听用户资料变更（添加）来更新缓存
                }
            }
        });
    }
}
