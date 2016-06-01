package com.ljh.www.imkit;

/**
 * Created by ljh on 2016/5/31.
 */
public class TabBaseFragment extends BaseFragment {

    public interface State {
        boolean isCurrent(TabBaseFragment tabFragment);
    }

    private State state;

    public void setState(State state) {
        this.state = state;
    }

    protected final boolean isCurrent() {
        return this.state.isCurrent(this);
    }

    //位于当前页
    public void onCurrent() {

    }

    //离开当前页
    public void onLeave() {

    }

    //当前页在滑动
    public void onCurrentScrolled() {

    }
}
