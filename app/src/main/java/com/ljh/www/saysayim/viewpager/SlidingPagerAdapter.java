package com.ljh.www.saysayim.viewpager;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ljh.www.saysayim.common.fragment.BaseFragment;
import com.ljh.www.saysayim.common.fragment.TabBaseFragment;


/**
 * Created by ljh on 2016/5/30.
 */
public abstract class SlidingPagerAdapter extends FragmentPagerAdapter implements TabBaseFragment.State {


    protected final TabBaseFragment[] fragments;
    private final ViewPager pager;
    private int lastPosition = 0;

    public SlidingPagerAdapter(FragmentManager fm, int count, ViewPager pager) {
        super(fm);
        fragments = new TabBaseFragment[count];
        this.pager = pager;
        lastPosition = 0;
    }

    @Override
    public BaseFragment getItem(int position) {
        return fragments[position];
    }


    @Override
    public boolean isCurrent(TabBaseFragment tabFragment) {
        int current = pager.getCurrentItem();
        if (current >= 0 && current < fragments.length) {
            if (tabFragment == fragments[current]) {
                return true;
            }
        }

        return false;
    }


    public void onPageSelected(int position) {

        TabBaseFragment fragment = getFragmentByPosition(position);
        if (null == fragment) {
            return;
        }
        fragment.onCurrent();
        onLeave(position);
    }


    public void onPageScrolled(int position) {
        TabBaseFragment fragment = getFragmentByPosition(position);
        if (null == fragment) {
            return;
        }
        fragment.onCurrentScrolled();
    }

    private void onLeave(int position) {
        TabBaseFragment fragment = getFragmentByPosition(lastPosition);
        lastPosition = position;
        if (null == fragment) {
            return;
        } else {
            fragment.onLeave();
        }

    }

    private TabBaseFragment getFragmentByPosition(int position) {
        if (position >= 0 && position < fragments.length) {
            return fragments[position];
        }
        return null;
    }


}
