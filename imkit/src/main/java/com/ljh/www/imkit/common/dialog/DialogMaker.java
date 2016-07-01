package com.ljh.www.imkit.common.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.ljh.www.imkit.R;

/**
 * Created by ljh on 2016/6/29.
 */
public class DialogMaker {
    private static EasyProgressDialog easyProgressDialog;

    public static void dismissProgress() {
        if (null != easyProgressDialog) {
            easyProgressDialog.dismiss();
        }
    }

    public static DialogFragment showProgress(AppCompatActivity activity, String msg) {
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("dialog_progress");
        if (prev != null) {
            ft.remove(prev);
        }
        easyProgressDialog = new EasyProgressDialog();
        ft.addToBackStack(null);
       return easyProgressDialog.show(ft, "dialog_progress", msg);
    }

    public static void showEula(AppCompatActivity activity) {
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("dialog_eula");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        new EulaDialog().show(ft, "dialog_eula");
    }

    public static class EulaDialog extends DialogFragment {

        public EulaDialog() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int padding = getResources().getDimensionPixelSize(R.dimen.content_padding_normal);

            TextView eulaTextView = new TextView(getActivity());
            eulaTextView.setText(Html.fromHtml(getString(R.string.eula_legal_text)));
            eulaTextView.setMovementMethod(LinkMovementMethod.getInstance());
            eulaTextView.setPadding(padding, padding, padding, padding);

            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.about_eula)
                    .setView(eulaTextView)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }
                    )
                    .create();
        }
    }
}
