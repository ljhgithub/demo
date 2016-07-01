package com.ljh.www.imkit.command;


import java.util.Calendar;

import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by ljh on 2016/6/29.
 */
public class RefreshCommand {
    Action0 action0;
    public RefreshCommand(Action0 action0) {
        this.action0 = action0;
    }

    public void execute() {
        action0.call();
    }
}
