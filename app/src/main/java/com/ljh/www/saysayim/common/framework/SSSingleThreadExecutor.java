package com.ljh.www.saysayim.common.framework;

import android.os.Handler;

import com.ljh.www.saysayim.AppCache;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by ljh on 2016/6/8.
 */
public class SSSingleThreadExecutor {

    private SSSingleThreadExecutor() {
        uiHandler = new Handler(AppCache.getContext().getMainLooper());
        executor = Executors.newSingleThreadExecutor();
    }

    public static SSSingleThreadExecutor getInstance() {
        return InstanceHolder.INSTANCE;
    }

    Handler uiHandler;
    Executor executor;


    public void execute(Runnable runnable) {
        if (null != executor) {
            executor.execute(runnable);
        }
    }

    public <T> void execute(SSTask<T> task) {
        if (null != executor) {
            execute(new SSRunnable<>(task));
        }
    }

    public interface SSTask<T> {
        T runInBackground();
        void onCompleted(T result);
    }

    private class SSRunnable<T> implements Runnable {

        public SSRunnable(SSTask<T> task) {
            this.task = task;

        }

        SSTask<T> task;

        @Override
        public void run() {
            final T result = task.runInBackground();
            if (null != uiHandler) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        task.onCompleted(result);
                    }
                });
            }
        }
    }

    static class InstanceHolder {
        final static SSSingleThreadExecutor INSTANCE = new SSSingleThreadExecutor();
    }
}
