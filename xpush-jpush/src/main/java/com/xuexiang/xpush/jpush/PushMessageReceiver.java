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

package com.xuexiang.xpush.jpush;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.logs.PushLog;
import com.xuexiang.xpush.util.PushUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_REGISTER;
import static com.xuexiang.xpush.core.annotation.ConnectStatus.CONNECTED;
import static com.xuexiang.xpush.core.annotation.ConnectStatus.DISCONNECT;
import static com.xuexiang.xpush.core.annotation.ResultCode.RESULT_ERROR;
import static com.xuexiang.xpush.core.annotation.ResultCode.RESULT_OK;

/**
 * 消息推送接收器
 *
 * @author xuexiang
 * @since 2019-08-16 16:54
 */
public class PushMessageReceiver extends JPushMessageReceiver {

    private static final String TAG = "JPush-";

    @Override
    public void onMessage(Context context, CustomMessage message) {
        PushLog.d(TAG + "[onMessage]:" + message);
        XPush.transmitMessage(context, message.message, message.extra, null);
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage message) {
        PushLog.d(TAG + "[onNotifyMessageOpened]:" + message);
        try {
            XPush.transmitNotificationClick(context, message.notificationId, message.notificationTitle, message.notificationContent, null,
                    PushUtils.toMap(new JSONObject(message.notificationExtras)));
        } catch (Exception e) {
            e.printStackTrace();
            XPush.transmitNotificationClick(context, message.notificationId, message.notificationTitle, message.notificationContent, message.notificationExtras, null);
        }
    }

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        if (intent.getExtras() == null) {
            return;
        }

        PushLog.d(TAG + "[onMultiActionClicked] 用户点击了通知栏按钮:" + intent.getExtras().getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA));
    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
        PushLog.d(TAG + "[onNotifyMessageArrived]:" + message);
        try {
            XPush.transmitNotification(context, message.notificationId, message.notificationTitle, message.notificationContent, null,
                    PushUtils.toMap(new JSONObject(message.notificationExtras)));
        } catch (Exception e) {
            e.printStackTrace();
            XPush.transmitNotification(context, message.notificationId, message.notificationTitle, message.notificationContent, message.notificationExtras, null);
        }
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
        PushLog.d(TAG + "[onNotifyMessageDismiss]:" + message);
    }

    @Override
    public void onRegister(Context context, String registrationId) {
        PushLog.d(TAG + "[onRegister]:" + registrationId);
        XPush.transmitCommandResult(context, TYPE_REGISTER,
                TextUtils.isEmpty(registrationId) ? RESULT_ERROR : RESULT_OK,
                registrationId, null, null);
    }

    @Override
    public void onConnected(Context context, boolean isConnected) {
        PushLog.d(TAG + "[onConnected] " + isConnected);
        XPush.transmitConnectStatusChanged(context, isConnected ? CONNECTED : DISCONNECT);
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        PushLog.d(TAG + "[onCommandResult] " + cmdMessage);
    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        XPush.transmitCommandResult(context, jPushMessage.getSequence(),
                jPushMessage.getErrorCode() == 0 ? RESULT_OK : jPushMessage.getErrorCode(),
                PushUtils.collection2String(jPushMessage.getTags()), null, null);
        super.onTagOperatorResult(context, jPushMessage);
    }


    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        XPush.transmitCommandResult(context, jPushMessage.getSequence(),
                jPushMessage.getErrorCode() == 0 ? RESULT_OK : jPushMessage.getErrorCode(),
                jPushMessage.getAlias(), null, null);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }

}
