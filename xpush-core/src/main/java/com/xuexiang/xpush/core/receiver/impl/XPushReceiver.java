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

import android.content.Context;

import com.xuexiang.xpush.core.IPushClient;
import com.xuexiang.xpush.core.XPushManager;
import com.xuexiang.xpush.entity.XPushCommand;
import com.xuexiang.xpush.entity.XPushMsg;

/**
 * 消息推送统一接收管理
 *
 * @author xuexiang
 * @since 2019-08-17 15:58
 */
public class XPushReceiver extends AbstractPushReceiver {

    @Override
    public void onNotification(Context context, XPushMsg msg) {
        if (msg == null) {
            return;
        }

        XPushManager.get().notifyNotification(msg.toNotification());
    }

    /**
     * 收到通知点击事件
     *
     * @param context
     * @param msg     消息
     */
    @Override
    public void onNotificationClick(Context context, XPushMsg msg) {
        if (msg == null) {
            return;
        }

        XPushManager.get().notifyNotificationClick(msg.toNotification());
    }

    /**
     * 收到自定义消息
     *
     * @param context
     * @param msg     消息
     */
    @Override
    public void onMessageReceived(Context context, XPushMsg msg) {
        if (msg == null) {
            return;
        }

        XPushManager.get().notifyMessageReceived(msg.toCustomMessage());
    }

    /**
     * IPushClient执行命令的结果返回
     *
     * @param context
     * @param command 命令实体
     * @see IPushClient#register()
     * @see IPushClient#unRegister()
     * @see IPushClient#addTags(String...)
     * @see IPushClient#deleteTags(String...)
     * @see IPushClient#getTags()
     * @see IPushClient#bindAlias(String)
     * @see IPushClient#unBindAlias(String)
     * @see IPushClient#getAlias()
     */
    @Override
    public void onCommandResult(Context context, XPushCommand command) {
        if (command == null) {
            return;
        }

        XPushManager.get().notifyCommandResult(command);
    }
}
