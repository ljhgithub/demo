package com.ljh.www.saysayim;

import android.content.Context;

/**
 * Created by ljh on 2016/5/26.
 */
public class AppCache {
    private static Context context;
    private static String account;


    public static void setAccount(String account) {
        AppCache.account = account;

    }

    public static String getAccount() {
        return account;
    }

    public static void setContext(Context context) {
        AppCache.context = context;
    }

    public static Context getContext() {
        return context;
    }

    public static void clear() {
        account = null;
    }
}
