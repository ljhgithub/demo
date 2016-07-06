package com.ljh.www.saysayim.main.model;

import com.ljh.www.saysayim.common.fragment.TabBaseFragment;
import com.ljh.www.saysayim.main.fragment.ContactFragment;
import com.ljh.www.saysayim.main.fragment.SessionFragment;
import com.ljh.www.saysayim.main.fragment.SharePercentFragment;

/**
 * Created by ljh on 2016/5/30.
 */
public class MainTab {

    private TabBaseFragment[] fragments;
    private String[] titles;

    public void setTabs(TabBaseFragment[]fragments, String[] titles) {
        this.fragments=fragments;
        this.titles = titles;
    }

    public MainTab() {
        defaultTabs();
    }

    private void defaultTabs() {
        this.fragments = new TabBaseFragment[3];
        this.fragments[0]=new SharePercentFragment();
        this.fragments[1]=new SessionFragment();
        this.fragments[2]=new ContactFragment();
        this.titles = new String[]{"首页","会话", "联系人"};

    }

    public int getTabCount() {
        return fragments.length;
    }

    public TabBaseFragment getTabPager(int position) {
        return fragments[position];
    }
    public CharSequence getTabTitle(int position){
        return titles[position];
    }

    public TabBaseFragment[] getTabFragments(){
        return fragments;
    }
}
