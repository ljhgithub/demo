package com.ljh.www.saysayim.main.model;

import com.ljh.www.imkit.TabBaseFragment;
import com.ljh.www.saysayim.main.fragment.ContactFragment;
import com.ljh.www.saysayim.main.fragment.RecentContactFragment;

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
        this.fragments = new TabBaseFragment[2];
        this.fragments[0]=new RecentContactFragment();
        this.fragments[1]=new ContactFragment();
        this.titles = new String[]{"会话", "联系人"};

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
