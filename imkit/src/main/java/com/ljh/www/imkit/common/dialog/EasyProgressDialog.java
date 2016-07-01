package com.ljh.www.imkit.common.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ljh.www.imkit.R;

/**
 * Created by ljh on 2016/6/29.
 */
public class EasyProgressDialog extends DialogFragment {

    private TextView tvMessage;
    private ProgressBar mProgressBar;

    private String message = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.easy_progress_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mProgressBar = (ProgressBar) view.findViewById(R.id.easy_progress_bar);
        tvMessage = (TextView) view.findViewById(R.id.easy_progress_dialog_message);
        tvMessage.setText(message);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getContext(), R.style.easy_dialog_style);
    }

    public DialogFragment show(FragmentTransaction ft, String tag, String msg) {
        message = msg;
        show(ft, tag);
        return this;

    }

}
