package com.ljh.www.saysayim.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.imkit.util.string.MD5;
import com.ljh.www.saysayim.AppCache;

import com.ljh.www.saysayim.LoginBinding;
import com.ljh.www.saysayim.common.activity.BaseActivity;
import com.ljh.www.saysayim.common.viewmode.ViewModel;
import com.ljh.www.saysayim.main.activity.MainActivity;
import com.ljh.www.saysayim.R;
import com.ljh.www.saysayim.preference.Preferences;
import com.ljh.www.saysayim.widget.ClearableEditViewWithIcon;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

/**
 * Created by ljh on 2016/5/26.
 */
public class LoginActivity extends BaseActivity<ViewModel,LoginBinding>{
    private static final String TAG = LogUtils.makeLogTag(LoginActivity.class.getSimpleName());
    ClearableEditViewWithIcon etAccount;
    ClearableEditViewWithIcon etPassword;
    private AbortableFuture<LoginInfo> loginRequest;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginBinding  loginBinding = DataBindingUtil.<LoginBinding>setContentView(this, R.layout.activity_login);
        setBinding(loginBinding);
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        drawable.setBounds(1, 1, 80, 80);
        getTvBack().setCompoundDrawables(drawable, null, null, null);
        getTvBack().setEnabled(false);
        setTitleName("登录");
        setOption("完成");
        etAccount = (ClearableEditViewWithIcon) findViewById(R.id.et_account);
        etPassword = (ClearableEditViewWithIcon) findViewById(R.id.et_password);
        etAccount.setIcon(R.mipmap.user_account_icon);
        etPassword.setIcon(R.mipmap.user_pwd_lock_icon);
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_DONE == actionId) {
                    LogUtils.LOGD(TAG, MD5.getStringMD5(etPassword.getText().toString()));
                    login();
                }
                return false;
            }
        });

    }

    @Override
    public void doOption() {
        login();
    }

    public void login() {
        LoginInfo loginInfo = new LoginInfo(etAccount.getText().toString(), etPassword.getText().toString());
        loginRequest = NIMClient.getService(AuthService.class).login(loginInfo);
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo info) {
                LogUtils.LOGD(TAG, "onSuccess " + info.getAccount() + info.getToken());
                AppCache.setAccount(info.getAccount());
                saveLoginInfo(info.getAccount(), info.getToken());
                // 进入主界面
                MainActivity.start(LoginActivity.this);
                finish();
            }

            @Override
            public void onFailed(int i) {
                LogUtils.LOGD(TAG, "onFailed " + i);
            }

            @Override
            public void onException(Throwable throwable) {
                LogUtils.LOGD(TAG, "onException " + throwable.getMessage());
            }
        });
    }

    private void saveLoginInfo(String account, String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }
}
