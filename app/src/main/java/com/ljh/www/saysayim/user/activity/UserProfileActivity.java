package com.ljh.www.saysayim.user.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.saysayim.R;
import com.ljh.www.saysayim.UserProfileBinding;
import com.ljh.www.saysayim.common.activity.BaseActivity;
import com.ljh.www.saysayim.constant.Extras;
import com.ljh.www.saysayim.user.viewmodel.UserProfileVM;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

/**
 * Created by ljh on 2016/6/5.
 */
public class UserProfileActivity extends BaseActivity<UserProfileVM, UserProfileBinding> {

    private static final String TAG = LogUtils.makeLogTag(UserProfileActivity.class.getSimpleName());
    private NimUserInfo nimUserInfo;

    public static void start(Context context, NimUserInfo nimUserInfo) {
        Intent it = new Intent(context, UserProfileActivity.class);
        it.putExtra(Extras.NIM_USER_INFO, nimUserInfo);
        context.startActivity(it);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBinding(DataBindingUtil.<UserProfileBinding>setContentView(this, R.layout.activity_user_profile));
        parseExtras();
        UserProfileVM userProfileVM = new UserProfileVM();
        setViewModel(userProfileVM);
        getBinging().setUserProfileVM(userProfileVM);
        getViewModel().account = nimUserInfo.getAccount();
    }

    private void parseExtras() {
        nimUserInfo = (NimUserInfo) getIntent().getSerializableExtra(Extras.NIM_USER_INFO);
        LogUtils.LOGD(TAG, nimUserInfo.getName() + nimUserInfo.getAccount());
    }

}
