package com.ljh.www.saysayim.common.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.imkit.util.sys.ReflectionUtil;
import com.ljh.www.saysayim.R;
import com.ljh.www.saysayim.common.fragment.BaseFragment;
import com.ljh.www.saysayim.common.viewmode.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljh on 2016/5/25.
 */
public abstract class BaseActivity<VM extends ViewModel, B extends ViewDataBinding> extends AppCompatActivity {

    private static final String TAG = LogUtils.makeLogTag(BaseActivity.class.getSimpleName());
    public static Handler mHandler;
    private boolean destroyed = false;
    private B binding;
    private VM viewModel;
    private TextView tvTitle;
    private TextView tvOption;
    protected boolean hasTitleBar = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        destroyed = false;
//        LogUtils.LOGD(TAG, "activity: " + getClass().getSimpleName() + " onCreate()");
    }

    public void setHasTitleBar(boolean hasTitleBar) {
        this.hasTitleBar = hasTitleBar;
    }

    public boolean getHasTitleBar() {
        return this.hasTitleBar;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getTvOption() {
        return tvOption;
    }

    public void setBinding(B b) {
        this.binding = b;
        if (hasTitleBar) {
            setTitleLayout();
        }
    }

    public B getBinging() {
        if (binding == null) {
            throw new NullPointerException("You should setBinding first!");
        }
        return binding;
    }

    public void setTitleLayout() {
        binding.getRoot().findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        tvTitle = (TextView) binding.getRoot().findViewById(R.id.tv_title);
        tvOption = (TextView) binding.getRoot().findViewById(R.id.tv_option);
        tvOption.setText("");
        tvOption.setVisibility(View.GONE);
        tvTitle.setText("");
        tvOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOption();
            }
        });
    }

    public void goBack() {
        if (!isDestroyed()) {
            finish();
        }

    }

    public void doOption() {

    }

    public void setViewModel(VM viewModel) {
        this.viewModel = viewModel;
    }

    public VM getViewModel() {
        if (viewModel == null) {
            throw new NullPointerException("You should setViewModel first!");
        }
        return viewModel;
    }

    public boolean isDestroyed() {
        return this.destroyed;
    }

    public void setTitleName(String titleName) {
        tvTitle.setText(titleName);
    }

    public void setTitleName(int id) {
        tvTitle.setText(id);
    }

    public String getTitleName() {
        return tvTitle.getText().toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyed = true;
//        LogUtils.LOGD(TAG, "activity: " + getClass().getSimpleName() + " onDestroy()");
    }

    protected final Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(getMainLooper());
        }
        return mHandler;
    }

    protected void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (null == getCurrentFocus()) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (null != getCurrentFocus()) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

    }

    protected void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(
                view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 延时弹出键盘
     *
     * @param focus 键盘的焦点项
     */
    protected void showKeyboardDelayed(View focus) {
        final View viewToFocus = focus;
        if (null != focus) {
            focus.requestFocus();
        }
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (null == viewToFocus || viewToFocus.isFocused()) {
                    showKeyboard(true);
                }
            }
        }, 200);
    }

    public boolean isDestroyedCompatible() {
        if (Build.VERSION.SDK_INT >= 17) {
            return isDestroyedCompatible17();
        } else {
            return destroyed || super.isFinishing();
        }
    }

    @TargetApi(17)
    private boolean isDestroyedCompatible17() {
        return super.isDestroyed();
    }

    @Override
    public void onBackPressed() {
        invokeFragmentManagerNoteStateNotSaved();
        super.onBackPressed();
    }

    private void invokeFragmentManagerNoteStateNotSaved() {
        FragmentManager fm = getSupportFragmentManager();
        ReflectionUtil.invokeMethod(fm, "noteStateNotSaved", null);
    }

    protected BaseFragment addFragment(BaseFragment fragment) {

        List<BaseFragment> fragments = new ArrayList<>(1);
        fragments.add(fragment);
        addFragments(fragments);
        return fragment;
    }

    public List<BaseFragment> addFragments(List<BaseFragment> fragments) {
        List<BaseFragment> fragments2 = new ArrayList<>(fragments.size());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        boolean commit = false;
        for (int i = 0; i < fragments.size(); i++) {
            BaseFragment fg = fragments.get(i);
            int id = fg.getContainerId();
            BaseFragment fg2 = (BaseFragment) fm.findFragmentById(id);

            if (null == fg2) {
                fg2 = fg;
                ft.add(id, fg2);
                commit = true;
            }
            fragments2.add(i, fg2);
        }

        if (commit) {
            try {
                ft.commitAllowingStateLoss();
            } catch (Exception e) {

            }
        }
        return fragments2;
    }

    public BaseFragment switchContent(BaseFragment fragment) {
        return switchContent(fragment, false);
    }

    protected BaseFragment switchContent(BaseFragment fragment, boolean needAddToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(fragment.getContainerId(), fragment);
        if (needAddToBackStack) {
            ft.addToBackStack(null);
        }
        try {
            ft.commitAllowingStateLoss();
        } catch (Exception e) {

        }
        return fragment;
    }
}
