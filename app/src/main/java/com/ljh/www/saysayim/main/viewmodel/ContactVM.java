package com.ljh.www.saysayim.main.viewmodel;

import android.content.Context;

import com.ljh.www.imkit.command.RefreshCommand;

import java.util.Calendar;

import rx.Observable;
import rx.functions.Action0;

/**
 * Created by ljh on 2016/6/29.
 */
public class ContactVM {

    public RefreshCommand refreshCommand = new RefreshCommand(new Action0() {
        @Override
        public void call() {
        }
    });


    public ContactVM(Context context) {

    }
}
