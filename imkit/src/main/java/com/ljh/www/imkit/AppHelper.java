package com.ljh.www.imkit;

import android.content.Context;

/**
 * Created by ljh on 2016/6/30.
 */
public class AppHelper {
    public static Context context;
    public static void init(Context ctx){
        context=ctx;
    }
    public static Context getContext(){
        return context;
    }
}
