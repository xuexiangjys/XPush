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

package com.xuexiang.xpush.core.queue.impl;

import com.xuexiang.xpush.core.annotation.ConnectStatus;
import com.xuexiang.xpush.core.queue.IMessageObserver;
import com.xuexiang.xpush.entity.Notification;
import com.xuexiang.xpush.entity.XPushCommand;

/**
 * 推送消息的订阅者
 *
 * @author xuexiang
 * @since 2019-08-17 14:51
 */
public abstract class MessageSubscriber implements IMessageObserver {

    /**
     * 消息推送连接状态发生变化
     *
     * @param connectStatus 连接状态
     */
    @Override
    public void onConnectStatusChanged(@ConnectStatus int connectStatus) {

    }

    /**
     * 收到通知
     *
     * @param notification 通知
     */
    @Override
    public void onNotification(Notification notification) {

    }

    /**
     * 收到通知点击事件
     *
     * @param notification 通知
     */
    @Override
    public void onNotificationClick(Notification notification) {

    }

    /**
     * 收到命令执行的结果
     *
     * @param command 命令
     */
    @Override
    public void onCommandResult(XPushCommand command) {

    }
}
