package com.ljh.www.saysayim.launch;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.saysayim.AppCache;
import com.ljh.www.saysayim.R;
import com.ljh.www.saysayim.common.activity.BaseActivity;
import com.ljh.www.saysayim.login.LoginActivity;
import com.ljh.www.saysayim.main.activity.MainActivity;
import com.ljh.www.saysayim.preference.Preferences;

import java.util.List;

/**
 * Created by ljh on 2016/5/26.
 */
public class WelcomeActivity extends BaseActivity {

    private static boolean isFirstEnter = true;
    private boolean customSplash = false;
    private static final String TAG = LogUtils.makeLogTag(WelcomeActivity.class.getSimpleName());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (null != savedInstanceState) {
            setIntent(new Intent());
        }
        if (isFirstEnter) {
            showSplashView();
        } else {
            parseIntent();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstEnter) {
            isFirstEnter = false;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (canAutoLogin()){
                        parseIntent();
                    }else {
                        LoginActivity.start(WelcomeActivity.this);
                        finish();
                    }

                }
            };
            if (customSplash) {
                getHandler().postDelayed(runnable, 1000);
            } else {
                runnable.run();
            }
        }
    }

    /**
     * 已经登陆过，自动登陆
     */
    private boolean canAutoLogin() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        LogUtils.LOGD(TAG, "get local sdk token =" + token+account);
        return !TextUtils.isEmpty(account) && !TextUtils.isEmpty(token);
    }
    /**
     * 首次进入，打开欢迎界面
     */
    private void showSplashView() {
        getWindow().setBackgroundDrawableResource(R.mipmap.welcome_bg);
        customSplash = true;
    }

    private void parseIntent() {
        if (TextUtils.isEmpty(AppCache.getAccount())) {
            LoginActivity.start(WelcomeActivity.this);
            finish();
        } else {
            MainActivity.start(this);
            finish();
        }
    }

    public boolean stackResumed(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        LogUtils.LOGD(TAG, packageName);
        List<ActivityManager.RunningTaskInfo> recentTaskInfos = manager.getRunningTasks(1);
        if (recentTaskInfos != null && recentTaskInfos.size() > 0) {
            ActivityManager.RunningTaskInfo taskInfo = recentTaskInfos.get(0);
            LogUtils.LOGD(TAG, taskInfo.baseActivity.getClassName() + taskInfo.baseActivity.getPackageName() + taskInfo.numActivities);
            if (taskInfo.baseActivity.getPackageName().equals(packageName) && taskInfo.numActivities > 1) {
                return true;
            }
        }

        return false;
    }
}
