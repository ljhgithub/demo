package com.ljh.www.saysayim.common.util.crash;

import android.content.Context;

/**
 * Created by ljh on 2016/5/25.
 */
public class AppCrashHandler {
    private Context context;

    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    private static AppCrashHandler instance;

    private AppCrashHandler(Context context) {
        this.context = context;

        // get default
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        // install
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, final Throwable ex) {
                // save log
                saveException(ex, true);

                // uncaught
                uncaughtExceptionHandler.uncaughtException(thread, ex);
            }
        });
    }
    public static AppCrashHandler getInstance(Context mContext) {
        if (instance == null) {
            instance = new AppCrashHandler(mContext);
        }

        return instance;
    }

    public final void saveException(Throwable ex, boolean uncaught) {
//        CrashSaver.save(context, ex, uncaught);
    }

    public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
        if (handler != null) {
            this.uncaughtExceptionHandler = handler;
        }
    }
}
