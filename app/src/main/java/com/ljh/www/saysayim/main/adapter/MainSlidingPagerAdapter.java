package com.ljh.www.saysayim.main.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import com.ljh.www.saysayim.base.TabBaseFragment;
import com.ljh.www.saysayim.main.model.MainTab;
import com.ljh.www.saysayim.viewpager.SlidingPagerAdapter;


/**
 * Created by ljh on 2016/5/30.
 */
public class MainSlidingPagerAdapter extends SlidingPagerAdapter {

    private MainTab mainTab;

    public MainSlidingPagerAdapter(FragmentManager fm, MainTab mainTab, ViewPager viewPager) {
        super(fm, mainTab.getTabCount(), viewPager);
        this.mainTab = mainTab;
        for (int i = 0; i < mainTab.getTabCount(); i++) {
            TabBaseFragment fragment = mainTab.getTabPager(i);
            fragment.setState(this);
            fragments[i] = fragment;
        }

    }


    @Override
    public int getCount() {
        return mainTab.getTabCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mainTab.getTabTitle(position);
    }

}
