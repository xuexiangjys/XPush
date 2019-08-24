/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xpush.core.receiver.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.xuexiang.xpush.core.XPushManager;
import com.xuexiang.xpush.core.receiver.IPushReceiver;
import com.xuexiang.xpush.entity.XPushCommand;
import com.xuexiang.xpush.entity.XPushMsg;
import com.xuexiang.xpush.logs.PushLog;
import com.xuexiang.xpush.util.TransmitDataUtils;

import static com.xuexiang.xpush.core.annotation.PushAction.RECEIVE_COMMAND_RESULT;
import static com.xuexiang.xpush.core.annotation.PushAction.RECEIVE_CONNECT_STATUS_CHANGED;
import static com.xuexiang.xpush.core.annotation.PushAction.RECEIVE_MESSAGE;
import static com.xuexiang.xpush.core.annotation.PushAction.RECEIVE_NOTIFICATION;
import static com.xuexiang.xpush.core.annotation.PushAction.RECEIVE_NOTIFICATION_CLICK;


/**
 * 抽象的消息推送接收器
 *
 * @author xuexiang
 * @since 2019-08-15 18:04
 */
public abstract class AbstractPushReceiver extends BroadcastReceiver implements IPushReceiver {

    @Override
    public final void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        String action = intent.getAction();
        Parcelable parcelable = parsePushData(intent);

        if (RECEIVE_COMMAND_RESULT.equals(action)) {
            onCommandResult(context, (XPushCommand) parcelable);
        } else if (RECEIVE_NOTIFICATION.equals(action)) {
            onNotification(context, (XPushMsg) parcelable);
        } else if (RECEIVE_NOTIFICATION_CLICK.equals(action)) {
            onNotificationClick(context, (XPushMsg) parcelable);
        } else if (RECEIVE_MESSAGE.equals(action)) {
            onMessageReceived(context, (XPushMsg) parcelable);
        } else if (RECEIVE_CONNECT_STATUS_CHANGED.equals(action)) {
            onConnectStatusChanged(context, ((XPushMsg) parcelable).getId());
        }

        PushLog.i(String.format("%s--%s", action, String.valueOf(parcelable)));
    }

    @Override
    public <T extends Parcelable> T parsePushData(Intent intent) {
        return TransmitDataUtils.parsePushData(intent);
    }

    @Override
    public void onNotification(Context context, XPushMsg msg) {
        //处理收到通知的事件
    }

    @Override
    public void onCommandResult(Context context, XPushCommand command) {
        //处理命令执行的结果
    }

    @Override
    public void onConnectStatusChanged(Context context, int connectStatus) {
        XPushManager.get().notifyConnectStatusChanged(connectStatus);
    }

}
