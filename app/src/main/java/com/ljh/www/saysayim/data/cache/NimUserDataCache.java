package com.ljh.www.saysayim.data.cache;

import android.text.TextUtils;
import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.UserServiceObserve;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.net.Authenticator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ljh on 2016/6/6.
 */
public class NimUserDataCache {


    private Map<String, NimUserInfo> userInfoMap = new ConcurrentHashMap<>();
    private Map<String, List<RequestCallback<NimUserInfo>>> requestUserInfoMap = new ConcurrentHashMap<>(); // 重复请求处理

    private NimUserDataCache() {

    }

    public static NimUserDataCache getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void buildUserCache() {
        List<NimUserInfo> userInfos = NIMClient.getService(UserService.class).getAllUserInfo();
        addOrUpdateUsers(userInfos, false);

    }

    public void clearUserCache() {
        userInfoMap.clear();
    }

    public void getUserInfoFromRemote(final String account, final RequestCallback<NimUserInfo> callback) {
        if (TextUtils.isEmpty(account)) {
            return;
        }
        if (userInfoMap.containsKey(account)) {
            if (null != callback) {
                requestUserInfoMap.get(account).add(callback);
            }
            return;//已有数据，不必重新请求
        } else {
            List<RequestCallback<NimUserInfo>> rcs = new ArrayList<>();
            if (null != callback) {
                rcs.add(callback);

            }

            requestUserInfoMap.put(account, rcs);
        }
        List<String> accounts = new ArrayList<>(1);
        accounts.add(account);
        NIMClient.getService(UserService.class).fetchUserInfo(accounts).setCallback(new RequestCallbackWrapper<List<NimUserInfo>>() {
            @Override
            public void onResult(int i, List<NimUserInfo> userInfos, Throwable throwable) {
                NimUserInfo userInfo = null;
                boolean hasCallback = requestUserInfoMap.get(account).size() > 0;
                if (ResponseCode.RES_SUCCESS == i && null != userInfos && !userInfos.isEmpty()) {
                    userInfo = userInfos.get(0);
                    // 这里不需要更新缓存，由监听用户资料变更（添加）来更新缓存
                }
                // 处理回调
                if (hasCallback) {
                    List<RequestCallback<NimUserInfo>> cbs = requestUserInfoMap.get(account);
                    for (RequestCallback<NimUserInfo> cb : cbs) {
                        if (i == ResponseCode.RES_SUCCESS) {
                            cb.onSuccess(userInfo);
                        } else {
                            cb.onFailed(i);
                        }
                    }
                }
            }
        });
    }

    /**
     * 从云信服务器获取批量用户信息[异步]
     */
    public void getUserInfoFromRemote(List<String> accounts, final RequestCallback<List<NimUserInfo>> callback) {
        NIMClient.getService(UserService.class).fetchUserInfo(accounts).setCallback(new RequestCallback<List<NimUserInfo>>() {
            @Override
            public void onSuccess(List<NimUserInfo> users) {
                // 这里不需要更新缓存，由监听用户资料变更（添加）来更新缓存
                if (callback != null) {
                    callback.onSuccess(users);
                }
            }

            @Override
            public void onFailed(int code) {
                if (callback != null) {
                    callback.onFailed(code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                if (callback != null) {
                    callback.onException(exception);
                }
            }
        });
    }

    /**
     * *************************************** User缓存管理与变更通知 ********************************************
     */
    private void addOrUpdateUsers(List<NimUserInfo> users, boolean notify) {

        if (null == users || users.isEmpty()) {
            return;
        }
        for (NimUserInfo u : users) {
            userInfoMap.put(u.getAccount(), u);
        }
        List<String> accouts = getUserAccounts(users);
        if (notify && null != accouts && !accouts.isEmpty()) {
//TODO  通知用户变更
        }
    }

    private List<String> getUserAccounts(List<NimUserInfo> users) {
        if (null == users || users.isEmpty()) {
            return null;
        }
        List<String> accounts = new ArrayList<>(users.size());
        for (NimUserInfo user : users) {
            accounts.add(user.getAccount());
        }

        return accounts;
    }


    /**
     * ************************************ 用户资料变更监听(监听SDK) *****************************************
     */

    /**
     * 在Application的onCreate中向SDK注册用户资料变更观察者
     */
    public void registerObservers(boolean register) {
        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(userInfoUpdateObserver, register);
    }

    private Observer<List<NimUserInfo>> userInfoUpdateObserver = new Observer<List<NimUserInfo>>() {
        @Override
        public void onEvent(List<NimUserInfo> userInfos) {
            addOrUpdateUsers(userInfos, true);
        }
    };

    /**
     * 单例
     */
    static class InstanceHolder {
        final static NimUserDataCache INSTANCE = new NimUserDataCache();
    }

}
