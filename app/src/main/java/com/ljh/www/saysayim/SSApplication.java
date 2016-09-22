package com.ljh.www.saysayim;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;

import com.ljh.www.imkit.AppHelper;
import com.ljh.www.imkit.util.log.LogUtils;
import com.ljh.www.saysayim.common.util.crash.AppCrashHandler;
import com.ljh.www.saysayim.common.util.sys.SystemUtil;
import com.ljh.www.saysayim.launch.WelcomeActivity;
import com.ljh.www.saysayim.preference.Preferences;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.friend.FriendServiceObserve;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.friend.model.FriendChangedNotify;
import com.netease.nimlib.sdk.msg.MessageNotifierCustomization;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljh on 2016/5/25.
 */
public class SSApplication extends Application {

    private static final String TAG = LogUtils.makeLogTag(SSApplication.class.getSimpleName());

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.setDebug(BuildConfig.DEBUG);
        AppCache.setContext(this);
        // crash handler
//        / SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(this, loginInfo(), options());
        AppCrashHandler.getInstance(this);
        if (inMainProcess()) {
            AppUtil.init(this);
            AppHelper.init(this);
            NIMClient.toggleNotification(true);
            subscribeOnlineStatusObserver();
            subscribeFriendChangedNotify();
        }

    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        LogUtils.LOGD(TAG, packageName + "inMainProcess" + processName);
        return packageName.equals(processName);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    // 如果返回值为 null，则全部使用默认参数。
    private SDKOptions options() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
//        StatusBarNotificationConfig config = UserPreferences.getStatusConfig();
        StatusBarNotificationConfig config = null;
        if (config == null) {
            config = new StatusBarNotificationConfig();
        }
        // 点击通知需要跳转到的界面
        config.notificationEntrance = WelcomeActivity.class;
        config.notificationSmallIconId = R.mipmap.ic_stat_notify_msg;

        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.ljh.www.saysayim/raw/msg";
        options.statusBarNotificationConfig = config;
//        UserPreferences.setStatusConfig(config);

        // 配置保存图片，文件，log等数据的目录
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName();
        options.sdkStorageRootPath = sdkPath;

        // 配置数据库加密秘钥
        options.databaseEncryptKey = "NETEASE";

        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小，
//        options.thumbnailSize = MsgViewHolderThumbBase.getImageMaxEdge();

        // 用户信息提供者
        options.userInfoProvider = infoProvider;

        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
        options.messageNotifierCustomization = messageNotifierCustomization;

        return options;
    }

    private void subscribeOnlineStatusObserver() {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(new Observer<StatusCode>() {
            @Override
            public void onEvent(StatusCode statusCode) {

                LogUtils.LOGD(TAG, "observeOnlineStatus" + statusCode);
                if (statusCode.wontAutoLogin()) {
                    //TODO  被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
                }
            }
        }, true);
    }


    private void subscribeFriendChangedNotify() {

        NIMClient.getService(FriendServiceObserve.class).observeFriendChangedNotify(new Observer<FriendChangedNotify>() {
            @Override
            public void onEvent(FriendChangedNotify friendChangedNotify) {

                List<Friend> newFriends = friendChangedNotify.getAddedOrUpdatedFriends();
                List<String> delFriendAccounts = friendChangedNotify.getDeletedFriends();
                if (null != newFriends) {
                    LogUtils.LOGD(TAG, "newFriends" + newFriends.size());
                }
                if (null != delFriendAccounts) {
                    LogUtils.LOGD(TAG, "delFriendAccounts" + delFriendAccounts.size());
                }

            }
        }, true);
    }


    private UserInfoProvider infoProvider = new UserInfoProvider() {
        @Override
        public UserInfo getUserInfo(final String account) {
            final List<NimUserInfo> userInfos = new ArrayList<>(1);
//            UserInfo user = NimUserInfoCache.getInstance().getUserInfo(account);
//            if (user == null) {
//                NimUserInfoCache.getInstance().getUserInfoFromRemote(account, null);
//            }

            List<String> accounts = new ArrayList<>(1);
            accounts.add(account);
            NIMClient.getService(UserService.class).fetchUserInfo(accounts).setCallback(new RequestCallbackWrapper<List<NimUserInfo>>() {
                @Override
                public void onResult(int code, List<NimUserInfo> nimUserInfos, Throwable throwable) {
                    NimUserInfo user = null;
                    if (code == ResponseCode.RES_SUCCESS && nimUserInfos != null && !nimUserInfos.isEmpty()) {
                        user = nimUserInfos.get(0);
                        userInfos.add(user);
                        // 这里不需要更新缓存，由监听用户资料变更（添加）来更新缓存
                        LogUtils.LOGD(TAG, user.toString());
                    }

//                    // 处理回调
//                    if (hasCallback) {
//                        List<RequestCallback<NimUserInfo>> cbs = requestUserInfoMap.get(account);
//                        for (RequestCallback<NimUserInfo> cb : cbs) {
//                            if (code == ResponseCode.RES_SUCCESS) {
//                                cb.onSuccess(user);
//                            } else {
//                                cb.onFailed(code);
//                            }
//                        }
//                    }
                }
            });
            return userInfos.get(0);
        }

        @Override
        public int getDefaultIconResId() {
            return R.mipmap.avatar_def;
        }

        @Override
        public Bitmap getTeamIcon(String teamId) {
            Drawable drawable = getResources().getDrawable(R.mipmap.nim_avatar_group);
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }

            return null;
        }

        @Override
        public Bitmap getAvatarForMessageNotifier(String account) {
            UserInfo user = getUserInfo(account);
//            if (user != null && !TextUtils.isEmpty(user.getAvatar())) {
//                return ImageLoaderKit.getBitmapFromCache(user.getAvatar(), R.dimen.avatar_size_default, R.dimen
//                        .avatar_size_default);
//            }

            return null;
        }

        @Override
        public String getDisplayNameForMessageNotifier(String account, String sessionId, SessionTypeEnum sessionType) {
            String nick = null;
//            if (sessionType == SessionTypeEnum.P2P) {
//                nick = NimUserInfoCache.getInstance().getAlias(account);
//            } else if (sessionType == SessionTypeEnum.Team) {
//                nick = TeamDataCache.getInstance().getTeamNick(sessionId, account);
//                if (TextUtils.isEmpty(nick)) {
//                    nick = NimUserInfoCache.getInstance().getAlias(account);
//                }
//            }
            // 返回null，交给sdk处理。如果对方有设置nick，sdk会显示nick
            if (TextUtils.isEmpty(nick)) {
                return null;
            }

            return nick;
        }
    };

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private LoginInfo loginInfo() {

        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            LogUtils.LOGD(TAG, "account " + account + "token" + token);
            AppCache.setAccount(account);
            LoginInfo loginInfo = new LoginInfo(account, token);
            return loginInfo;
        }
        return null;
    }

    private MessageNotifierCustomization messageNotifierCustomization = new MessageNotifierCustomization() {
        @Override
        public String makeNotifyContent(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }

        @Override
        public String makeTicker(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }
    };
}
