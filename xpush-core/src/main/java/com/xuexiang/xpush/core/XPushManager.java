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

package com.xuexiang.xpush.core;

import android.support.annotation.NonNull;

import com.xuexiang.xpush.core.queue.IMessageObservable;
import com.xuexiang.xpush.core.queue.impl.DefaultMessageObservableImpl;
import com.xuexiang.xpush.core.queue.impl.MessageSubscriber;
import com.xuexiang.xpush.entity.CustomMessage;
import com.xuexiang.xpush.entity.Notification;

/**
 * 推送核心管理类
 *
 * @author xuexiang
 * @since 2019-08-17 15:39
 */
public class XPushManager implements IMessageObservable {

    private static volatile XPushManager sInstance = null;

    private IMessageObservable mMessageObservable;

    private XPushManager() {
        mMessageObservable = new DefaultMessageObservableImpl();
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static XPushManager get() {
        if (sInstance == null) {
            synchronized (XPushManager.class) {
                if (sInstance == null) {
                    sInstance = new XPushManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 设置推送消息的被观察者
     *
     * @param observable
     * @return
     */
    public XPushManager setIMessageObservable(@NonNull IMessageObservable observable) {
        mMessageObservable = observable;
        return this;
    }

    /**
     * 消息推送连接状态发生变化
     *
     * @param connectStatus 推送连接状态
     */
    @Override
    public void notifyConnectStatusChanged(int connectStatus) {
        if (mMessageObservable != null) {
            mMessageObservable.notifyConnectStatusChanged(connectStatus);
        }
    }

    /**
     * 收到通知
     *
     * @param notification 被处理过的推送消息
     */
    @Override
    public void notifyNotification(Notification notification) {
        if (mMessageObservable != null) {
            mMessageObservable.notifyNotification(notification);
        }
    }

    /**
     * 收到通知点击事件
     *
     * @param notification 被处理过的推送消息
     */
    @Override
    public void notifyNotificationClick(Notification notification) {
        if (mMessageObservable != null) {
            mMessageObservable.notifyNotificationClick(notification);
        }
    }

    /**
     * 收到自定义消息
     *
     * @param message 自定义消息
     */
    @Override
    public void notifyMessageReceived(CustomMessage message) {
        if (mMessageObservable != null) {
            mMessageObservable.notifyMessageReceived(message);
        }
    }

    /**
     * 注册推送消息的订阅者
     *
     * @param subscriber 消息订阅者
     */
    @Override
    public boolean register(MessageSubscriber subscriber) {
        if (mMessageObservable != null) {
            return mMessageObservable.register(subscriber);
        }
        return false;
    }

    /**
     * 注销推送消息的订阅者
     *
     * @param subscriber 消息订阅者
     */
    @Override
    public boolean unregister(MessageSubscriber subscriber) {
        if (mMessageObservable != null) {
            return mMessageObservable.unregister(subscriber);
        }
        return false;
    }

    /**
     * 注销所有的推送消息的订阅者
     */
    @Override
    public void unregisterAll() {
        if (mMessageObservable != null) {
            mMessageObservable.unregisterAll();
        }
    }


}
