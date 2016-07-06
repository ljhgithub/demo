package com.ljh.www.saysayim.common.fragment;

import android.app.Activity;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.saysayim.common.viewmode.ViewModel;
import com.ljh.www.saysayim.common.activity.BaseActivity;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.FragmentLifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle.components.support.RxFragment;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by ljh on 2016/5/25.
 */
public abstract class BaseFragment<VM extends ViewModel, B extends ViewDataBinding> extends Fragment implements FragmentLifecycleProvider {
    private static final String TAG = LogUtils.makeLogTag(BaseActivity.class.getSimpleName());
    private static Handler mHandler = new Handler();

    private B binding;
    private VM viewModel;
    private int containerId;
    private boolean destroy = false;
    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();

    public void setContainerId(int containerId) {
        this.containerId = containerId;
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

    /*
    *使用databinding是忽略此方法
     */
    public void setViewModel(VM viewModel) {
        this.viewModel = viewModel;
    }

    /*
   *使用databinding是忽略此方法
    */
    public VM getViewModel() {
        if (null == viewModel) {
            throw new NullPointerException("You should setViewModel first!");
        }
        return this.viewModel;
    }

    public int getContainerId() {
        return containerId;
    }

    @Override
    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bindFragment(lifecycleSubject);
    }


    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        destroy = false;
//        LogUtils.LOGD(TAG, "Fragment: " + getClass().getSimpleName() + " onActivityCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
    }


    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
        destroy = true;
        //        LogUtils.LOGD(TAG, "Fragment: " + getClass().getSimpleName() + " onCreate()");

    }

    @Override
    public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
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
            if (null != activity.getCurrentFocus()) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
