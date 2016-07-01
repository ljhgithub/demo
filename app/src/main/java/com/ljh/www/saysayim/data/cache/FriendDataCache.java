package com.ljh.www.saysayim.data.cache;

import android.text.TextUtils;

import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.saysayim.AppCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.FriendServiceObserve;
import com.netease.nimlib.sdk.friend.model.BlackListChangedNotify;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.friend.model.FriendChangedNotify;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by ljh on 2016/6/6.
 */
public class FriendDataCache {

    private static final String TAG = LogUtils.makeLogTag(FriendDataCache.class.getSimpleName());

    public static FriendDataCache getInstance() {
        return InstanceHolder.instance;
    }

    //我的好友 移除拉黑的好友
    private Set<String> friendAccountSet = new CopyOnWriteArraySet<>();
    //所有好友 包含拉黑的好友
    private Map<String, Friend> friendMap = new ConcurrentHashMap<>();
    private List<FriendDataChangedObserver> friendObservers = new ArrayList<>();

    /**
     * 初始化&清理
     */
    public void buildCache() {
        List<Friend> friends = NIMClient.getService(FriendService.class).getFriends();
        if (null == friends) return;
        for (Friend friend : friends) {
            friendMap.put(friend.getAccount(), friend);
        }
        List<String> accounts = NIMClient.getService(FriendService.class).getFriendAccounts();

        if (accounts.isEmpty()) {
            return;
        }
        //排除黑名单
        List<String> blacks = NIMClient.getService(FriendService.class).getBlackList();
        blacks.removeAll(blacks);

        //排除自己
        blacks.remove(AppCache.getAccount());

        // 确定缓存
        friendAccountSet.addAll(accounts);

        LogUtils.LOGD(TAG, "build FriendDataCache completed, friends count = " + friendAccountSet.size());
    }

    public void clearCache() {
        friendMap.clear();
        friendAccountSet.clear();
    }

    /**
     * ****************************** 好友查询接口 ******************************
     */
    public Friend getFriendByAccount(String account) {

        if (TextUtils.isEmpty(account)) {
            return null;
        }

        return friendMap.get(account);
    }

    public List<String> getMyFriendAccount() {
        List<String> accounts = new ArrayList<>(friendAccountSet.size());
        accounts.addAll(friendAccountSet);
        return accounts;
    }

    public boolean isMyFriend(String account) {
        return friendAccountSet.contains(account);
    }


    /**
     * ****************************** 缓存好友关系变更监听&通知 ******************************
     */

    /**
     * 缓存监听SDK
     */


    public void registerObservers(boolean register) {
        NIMClient.getService(FriendServiceObserve.class).observeFriendChangedNotify(friendChangedNotifyObserver, true);
        NIMClient.getService(FriendServiceObserve.class).observeBlackListChangedNotify(blackListChangedNotifyObserver, true);

    }

    /**
     * APP监听缓存
     */
    public void registerFriendDataChangedObserver(FriendDataChangedObserver o, boolean register) {
        if (o == null) {
            return;
        }

        if (register) {
            if (!friendObservers.contains(o)) {
                friendObservers.add(o);
            }
        } else {
            friendObservers.remove(o);
        }
    }

    /**
     * 监听好友关系变化
     */
    private Observer<FriendChangedNotify> friendChangedNotifyObserver = new Observer<FriendChangedNotify>() {
        @Override
        public void onEvent(FriendChangedNotify friendChangedNotify) {
            List<Friend> addedOrUpdateFriends = friendChangedNotify.getAddedOrUpdatedFriends();
            List<String> addedOrUpdateAccounts = new ArrayList<>(addedOrUpdateFriends.size());
            List<String> myFriendAccounts = new ArrayList<>(addedOrUpdateFriends.size());

            List<String> deletedFriendAccounts = friendChangedNotify.getDeletedFriends();

            //如果在黑名单中，那就不加到好友列表中
            String account;
            for (Friend friend : addedOrUpdateFriends) {
                account = friend.getAccount();
                friendMap.put(account, friend);
                addedOrUpdateAccounts.add(account);
                if (NIMClient.getService(FriendService.class).isInBlackList(account)) {
                    continue;
                }
                myFriendAccounts.add(account);
            }

            //更新我的好友关系
            if (!myFriendAccounts.isEmpty()) {
                friendAccountSet.addAll(myFriendAccounts);
            }

            //通知好友关系更新
            if (!addedOrUpdateAccounts.isEmpty()) {
                for (FriendDataChangedObserver o : friendObservers) {
                    o.onAddedOrUpdatedFriends(addedOrUpdateAccounts);
                }
            }

            // 处理被删除的好友关系
            if (!deletedFriendAccounts.isEmpty()) {
                friendAccountSet.removeAll(deletedFriendAccounts);
                for (String a : deletedFriendAccounts) {
                    friendMap.remove(a);
                }

                //通知好友关系更新
                if (!addedOrUpdateAccounts.isEmpty()) {
                    for (FriendDataChangedObserver o : friendObservers) {
                        o.onDeletedFriends(deletedFriendAccounts);
                    }
                }
            }

        }
    };

    /**
     * 监听黑名单变化(决定是否加入或者移出好友列表)
     */
    private Observer<BlackListChangedNotify> blackListChangedNotifyObserver = new Observer<BlackListChangedNotify>() {
        @Override
        public void onEvent(BlackListChangedNotify blackListChangedNotify) {
            List<String> addedAccounts = blackListChangedNotify.getAddedAccounts();
            List<String> removedAccounts = blackListChangedNotify.getRemovedAccounts();
            if (!addedAccounts.isEmpty()) {
                //拉黑移除好友列表
                friendAccountSet.remove(addedAccounts);
                // notify
                for (FriendDataChangedObserver o : friendObservers) {
                    o.onAddUserToBlackList(addedAccounts);
                }
                // 拉黑，要从最近联系人列表中删除该好友
                for (String account : addedAccounts) {
                    NIMClient.getService(MsgService.class).deleteRecentContact2(account, SessionTypeEnum.P2P);
                }
            }

            if (!removedAccounts.isEmpty()) {
                // 移出黑名单，判断是否加入好友名单
                for (String account : removedAccounts) {
                    if (NIMClient.getService(FriendService.class).isMyFriend(account)) {
                        friendAccountSet.add(account);
                    }
                }
                // notify
                for (FriendDataChangedObserver o : friendObservers) {
                    o.onRemoveUserFromBlackList(addedAccounts);
                }
            }
        }
    };

    public interface FriendDataChangedObserver {
        void onAddedOrUpdatedFriends(List<String> accounts);

        void onDeletedFriends(List<String> accounts);

        void onAddUserToBlackList(List<String> account);

        void onRemoveUserFromBlackList(List<String> account);
    }

    /**
     * ************************************ 单例 **********************************************
     */
    static class InstanceHolder {
        final static FriendDataCache instance = new FriendDataCache();
    }
}
