package com.ljh.www.saysayim.common.util.im;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.ljh.www.imkit.util.log.LogUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.model.AddFriendNotify;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.SystemMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljh on 2016/6/6.
 */
public class NimKit {
    private static final String TAG = LogUtils.makeLogTag(NimKit.class.getSimpleName());

    private void queryAddFreindSystemMsg() {
        List<SystemMessageType> msgType = new ArrayList<>();
        msgType.add(SystemMessageType.AddFriend);
        List<SystemMessage> msgs = NIMClient.getService(SystemMessageService.class).querySystemMessageByTypeBlock(msgType, 0, 20);
        if (null != msgs && msgs.size() > 0) {
            for (SystemMessage systemMessage : msgs) {
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
                        LogUtils.LOGD(TAG, "对方请求添加好友" + systemMessage.getContent());
                        NIMClient.getService(FriendService.class).ackAddFriendRequest(attachData.getAccount(), true);
                    }
                }
            }
        }
    }
}
