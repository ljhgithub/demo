package com.ljh.www.saysayim.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.saysayim.R;

/**
 * Created by ljh on 2016/5/26.
 */
public class ClearableEditViewWithIcon extends EditText implements View.OnTouchListener, TextWatcher {

    Drawable delIcon = getResources().getDrawable(R.mipmap.ic_edit_delete);
    Drawable icon;

    public ClearableEditViewWithIcon(Context context) {
        super(context);
        init();
    }

    public ClearableEditViewWithIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearableEditViewWithIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ClearableEditViewWithIcon.this.setOnTouchListener(this);
        ClearableEditViewWithIcon.this.addTextChangedListener(this);
        delIcon.setBounds(0, 0, delIcon.getIntrinsicWidth(), delIcon.getIntrinsicHeight());
        manageClearIcon();
    }

    public void setIcon(int id) {
        icon = getResources().getDrawable(id);
        icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
        manageClearIcon();

    }

    public void setClearIcon(int id) {
        delIcon = getResources().getDrawable(id);
        delIcon.setBounds(0, 0, delIcon.getIntrinsicWidth(), delIcon.getIntrinsicHeight());
        manageClearIcon();

    }

    private void manageClearIcon() {
        if (TextUtils.isEmpty(getText())) {
            removeClearIcon();
        } else {
            addClearIcon();
        }
    }

    private void addClearIcon() {
        Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(icon, compoundDrawables[1], delIcon, compoundDrawables[3]);
    }

    private void removeClearIcon() {
        Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(icon, compoundDrawables[1], null, compoundDrawables[3]);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ClearableEditViewWithIcon et = ClearableEditViewWithIcon.this;
        if (null != getCompoundDrawables()[2] && MotionEvent.ACTION_DOWN == event.getAction()) {
            if (event.getX() > et.getWidth() - et.getPaddingRight() - delIcon.getIntrinsicWidth()) {
                et.setText("");
                removeClearIcon();
            }

        }
        return false;
    }


    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        manageClearIcon();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
