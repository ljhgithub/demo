package com.ljh.www.saysayim.base;

import android.app.Activity;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ljh.www.imkit.util.log.LogUtils;

/**
 * Created by ljh on 2016/5/25.
 */
public abstract class BaseFragment<VM extends ViewModel,B extends ViewDataBinding> extends Fragment {
    private static final String TAG = LogUtils.makeLogTag(BaseActivity.class.getSimpleName());
    private static Handler mHandler = new Handler();

    private B binding;
    private VM viewModel;
    private int containerId;
    private boolean destroy = false;

    public void setContainerId(int containerId){
        this.containerId=containerId;
    }

    public void setBinding(B b) {
        this.binding = b;
    }

    public B getBinging() {
        if (binding == null) {
            throw new NullPointerException("You should setBinding first!");
        }
        return binding;
    }

    public void setViewModel(VM viewModel){
        this.viewModel=viewModel;
    }

    public VM getViewModel(){
        if (null==viewModel){
            throw new NullPointerException("You should setViewModel first!");
        }
        return this.viewModel;
    }
    public int getContainerId(){
        return containerId;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        destroy = false;
//        LogUtils.LOGD(TAG, "Fragment: " + getClass().getSimpleName() + " onActivityCreated()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroy = true;
//        LogUtils.LOGD(TAG, "Fragment: " + getClass().getSimpleName() + " onCreate()");
    }

    protected Handler getHandler() {
        return mHandler;
    }

    protected final void postRunnable(final Runnable runnable) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!isAdded()) {
                    return;
                }
                runnable.run();
            }
        });
    }

    protected final void postDelayed(final Runnable runnable, long delayed) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isAdded()) {
                    return;
                }
                runnable.run();
            }
        }, delayed);
    }

    protected void showKeyboard(boolean isShow) {
        Activity activity = getActivity();
        if (null == activity) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        if (isShow) {
            if (null == activity.getCurrentFocus()) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(activity.getCurrentFocus(), 0);
            }
        } else {
            if (null!=activity.getCurrentFocus()){
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    protected void hideKeyboard(View view) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        imm.hideSoftInputFromWindow(
                view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
