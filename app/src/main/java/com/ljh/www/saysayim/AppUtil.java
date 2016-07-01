package com.ljh.www.saysayim;

import android.content.Context;
import android.text.TextUtils;

import com.ljh.www.saysayim.common.util.im.LoginSyncDataStatusObserver;
import com.ljh.www.saysayim.data.cache.DataCacheManager;

/**
 * Created by ljh on 2016/6/27.
 */
public class AppUtil {
    public static Context context;
    private static String account;

    public static void init(Context context) {
        AppUtil.context = context.getApplicationContext();
        LoginSyncDataStatusObserver.getInstance().registerLoginSyncDataStatus(true);
        DataCacheManager.observeSDKDataChanged(true);
        if (!TextUtils.isEmpty(getAccount())) {
            DataCacheManager.buildDataCache(); // build data cache on auto login
        }

    }

    public static void setAccount(String account) {
        AppUtil.account = account;
    }

    public static String getAccount() {
        return account;
    }

    public static Context getContext() {
        return context;
    }
}
