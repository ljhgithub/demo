package com.ljh.www.saysayim.main.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.saysayim.R;
import com.ljh.www.saysayim.databinding.ActivityMainBinding;
import com.ljh.www.saysayim.main.adapter.MainSlidingPagerAdapter;
import com.ljh.www.saysayim.main.model.MainTab;
import com.ljh.www.saysayim.viewpager.FadeInOutPageTransformer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String TAG = LogUtils.makeLogTag(MainActivity.class.getSimpleName());
    private MainSlidingPagerAdapter pagerAdapter;
    private ActivityMainBinding binding;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();
    }

    private void initView() {
        pagerAdapter = new MainSlidingPagerAdapter(getSupportFragmentManager(), new MainTab(), binding.pager);
        binding.pager.setAdapter(pagerAdapter);
        binding.tabPage.setSelectedIndicatorColors(getResources().getColor(R.color.tab_selected_strip));
        binding.tabPage.setCustomTabView(R.layout.tab_indicator, R.id.tab_title);
        binding.tabPage.setUnderlineColor(R.color.colorPrimary);
        binding.tabPage.setUnderlineThickness(1);
        binding.tabPage.setViewPager(binding.pager);
        binding.pager.setOffscreenPageLimit(pagerAdapter.getCount());
        binding.pager.setPageTransformer(true, new FadeInOutPageTransformer());
        binding.pager.addOnPageChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


    @Override
    public void onStart() {
        super.onStart();
//        switchTab(1);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public void switchTab(int index) {
        if (index >= 0 && index < binding.pager.getOffscreenPageLimit()) {
            binding.pager.setCurrentItem(index);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        pagerAdapter.onPageScrolled(position);
    }

    @Override
    public void onPageSelected(int position) {
        pagerAdapter.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
