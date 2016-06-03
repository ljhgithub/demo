package com.ljh.www.saysayim.main.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.saysayim.base.BaseActivity;
import com.ljh.www.saysayim.base.ViewModel;
import com.ljh.www.saysayim.MainBinding;
import com.ljh.www.saysayim.R;
import com.ljh.www.saysayim.main.adapter.MainSlidingPagerAdapter;
import com.ljh.www.saysayim.main.model.MainTab;
import com.ljh.www.saysayim.search.activity.FriendSearchActivity;
import com.ljh.www.saysayim.viewpager.FadeInOutPageTransformer;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.netease.nimlib.sdk.friend.model.AddFriendNotify;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.SystemMessage;

import java.lang.reflect.Field;
import java.util.List;

public class MainActivity extends BaseActivity<ViewModel, MainBinding> implements ViewPager.OnPageChangeListener {
    private static final String TAG = LogUtils.makeLogTag(MainActivity.class.getSimpleName());
    private MainSlidingPagerAdapter pagerAdapter;

    private int currentIndex = 0;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBinding(DataBindingUtil.<MainBinding>setContentView(this, R.layout.activity_main));
        setTitle("main");
        initView();
        subscribeReceiveSystemMsg();
        getFriends();
    }

    private void initView() {
        pagerAdapter = new MainSlidingPagerAdapter(getSupportFragmentManager(), new MainTab(), getBinging().pager);
        getBinging().pager.setAdapter(pagerAdapter);
        getBinging().tabPage.setSelectedIndicatorColors(getResources().getColor(R.color.tab_selected_strip));
        getBinging().tabPage.setCustomTabView(R.layout.tab_indicator, R.id.tab_title);
        getBinging().tabPage.setUnderlineColor(R.color.colorPrimary);
        getBinging().tabPage.setUnderlineThickness(1);
        getBinging().tabPage.setViewPager(getBinging().pager);
        getBinging().pager.setOffscreenPageLimit(pagerAdapter.getCount());
        getBinging().pager.setPageTransformer(true, new FadeInOutPageTransformer());
        getBinging().pager.addOnPageChangeListener(this);
        switchTab(0);


    }

    @Override
    public void doOption() {
        switch (currentIndex) {
            case 0:
                break;
            case 1:
                PopupMenu popupMenu = new PopupMenu(this, getTvOption(), Gravity.BOTTOM);
                popupMenu.inflate(R.menu.contact_menu);
//                try {
//                    Field mpopup=popupMenu.getClass().getDeclaredField("mPopup");
//                    mpopup.setAccessible(true);
//                    MenuPopupHelper mPopup = (MenuPopupHelper) mpopup.get(popupMenu);
//                    mPopup.setForceShowIcon(true);
//                    mPopup.setGravity(Gravity.BOTTOM);
//                    mPopup.
//
//                } catch (Exception e) {
//
//                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.add_friend) {
                            FriendSearchActivity.start(getApplicationContext());

                        }
                        return false;
                    }
                });
                popupMenu.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    public void switchOption(int index) {
        switch (index) {
            case 0:
                getTvOption().setVisibility(View.GONE);
                break;
            case 1:
                getTvOption().setVisibility(View.VISIBLE);
                getTvOption().setText("save");
                break;
            default:
                break;
        }
    }

    public void switchTab(int index) {
        if (index >= 0 && index < getBinging().pager.getOffscreenPageLimit()) {
            getBinging().pager.setCurrentItem(index);
            currentIndex = index;
            switchOption(index);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        pagerAdapter.onPageScrolled(position);

    }

    @Override
    public void onPageSelected(int position) {
        pagerAdapter.onPageSelected(position);
        currentIndex = position;
        switchOption(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



    public void getFriends() {
        List<Friend> friends = NIMClient.getService(FriendService.class).getFriends();
        if (null != friends) {
            for (Friend friend : friends) {
                LogUtils.LOGD(TAG, friend.getAccount());
            }
        }

    }

    private void subscribeReceiveSystemMsg() {
        NIMClient.getService(SystemMessageObserver.class).observeReceiveSystemMsg(new Observer<SystemMessage>() {
            @Override
            public void onEvent(SystemMessage systemMessage) {
                if (SystemMessageType.AddFriend == systemMessage.getType()) {
                    AddFriendNotify attachData = (AddFriendNotify) systemMessage.getAttachObject();
                    if (attachData != null) {
                        LogUtils.LOGD(TAG, "attachData" + attachData.getAccount() + attachData.getMsg());
                        // 针对不同的事件做处理
                        if (attachData.getEvent() == AddFriendNotify.Event.RECV_ADD_FRIEND_DIRECT) {
                            LogUtils.LOGD(TAG, "对方直接添加你为好友");
                            // 对方直接添加你为好友
                        } else if (attachData.getEvent() == AddFriendNotify.Event.RECV_AGREE_ADD_FRIEND) {
                            // 对方通过了你的好友验证请求
                            LogUtils.LOGD(TAG, "对方通过了你的好友验证请求");
                        } else if (attachData.getEvent() == AddFriendNotify.Event.RECV_REJECT_ADD_FRIEND) {
                            // 对方拒绝了你的好友验证请求
                            LogUtils.LOGD(TAG, "对方拒绝了你的好友验证请求");
                        } else if (attachData.getEvent() == AddFriendNotify.Event.RECV_ADD_FRIEND_VERIFY_REQUEST) {
                            // 对方请求添加好友，一般场景会让用户选择同意或拒绝对方的好友请求。
                            // 通过message.getContent()获取好友验证请求的附言
                            LogUtils.LOGD(TAG, "对方拒绝了你的好友验证请求" + systemMessage.getContent());
                            NIMClient.getService(FriendService.class).ackAddFriendRequest(attachData.getAccount(), true);
                        }
                    }
                }
            }
        }, true);
    }
}
