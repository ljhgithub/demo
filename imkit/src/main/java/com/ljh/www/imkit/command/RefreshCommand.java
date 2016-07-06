package com.ljh.www.imkit.command;


import rx.functions.Action0;

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
