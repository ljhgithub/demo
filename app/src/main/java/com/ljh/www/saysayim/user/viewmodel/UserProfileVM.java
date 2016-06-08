package com.ljh.www.saysayim.user.viewmodel;

import android.databinding.ObservableField;
import android.text.TextUtils;
import android.view.View;

import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.saysayim.common.viewmode.ViewModel;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;

/**
 * Created by ljh on 2016/6/5.
 */
public class UserProfileVM extends ViewModel {
    private static final String TAG = LogUtils.makeLogTag(UserProfileVM.class.getSimpleName());
    public String account;

    public void onClickAddFriend(View view) {
        LogUtils.LOGD(TAG, "onClickAddFriend" + account);
        if (!TextUtils.isEmpty(account))
            NIMClient.getService(FriendService.class).addFriend(new AddFriendData(account, VerifyType.VERIFY_REQUEST)).setCallback(new RequestCallbackWrapper<Void>() {
                @Override
                public void onResult(int i, Void aVoid, Throwable throwable) {
                    LogUtils.LOGD(TAG, "onResult" + i);
                }
            });
    }
}
