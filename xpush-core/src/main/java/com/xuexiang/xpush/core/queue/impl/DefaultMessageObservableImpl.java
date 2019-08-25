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

import com.xuexiang.xpush.core.queue.IMessageObservable;
import com.xuexiang.xpush.core.queue.IMessageObserver;
import com.xuexiang.xpush.entity.CustomMessage;
import com.xuexiang.xpush.entity.Notification;
import com.xuexiang.xpush.entity.XPushCommand;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 推送消息的被观察者
 *
 * @author xuexiang
 * @since 2019-08-17 14:55
 */
public class DefaultMessageObservableImpl implements IMessageObservable {
    /**
     * 对象锁
     */
    private final Object mLock = new Object();
    /**
     * 存放推送消息的订阅者
     */
    private List<WeakReference<IMessageObserver>> mObservers = new ArrayList<>();

    /**
     * 消息推送连接状态发生变化
     *
     * @param connectStatus 推送连接状态
     */
    @Override
    public void notifyConnectStatusChanged(int connectStatus) {
        Iterator<WeakReference<IMessageObserver>> it = mObservers.iterator();
        synchronized (mLock) {
            while (it.hasNext()) {
                IMessageObserver observer = it.next().get();
                if (observer != null) {
                    observer.onConnectStatusChanged(connectStatus);
                } else {
                    it.remove();
                }
            }
        }
    }


    /**
     * 收到通知
     *
     * @param notification 被处理过的推送消息
     */
    @Override
    public void notifyNotification(Notification notification) {
        Iterator<WeakReference<IMessageObserver>> it = mObservers.iterator();
        synchronized (mLock) {
            while (it.hasNext()) {
                IMessageObserver observer = it.next().get();
                if (observer != null) {
                    observer.onNotification(notification);
                } else {
                    it.remove();
                }
            }
        }
    }


    /**
     * 收到通知点击事件
     *
     * @param notification 被处理过的推送消息
     */
    @Override
    public void notifyNotificationClick(Notification notification) {
        Iterator<WeakReference<IMessageObserver>> it = mObservers.iterator();
        synchronized (mLock) {
            while (it.hasNext()) {
                IMessageObserver observer = it.next().get();
                if (observer != null) {
                    observer.onNotificationClick(notification);
                } else {
                    it.remove();
                }
            }
        }
    }


    /**
     * 收到自定义消息
     *
     * @param message 自定义消息
     */
    @Override
    public void notifyMessageReceived(CustomMessage message) {
        Iterator<WeakReference<IMessageObserver>> it = mObservers.iterator();
        synchronized (mLock) {
            while (it.hasNext()) {
                IMessageObserver observer = it.next().get();
                if (observer != null) {
                    observer.onMessageReceived(message);
                } else {
                    it.remove();
                }
            }
        }
    }

    /**
     * 收到命令执行的结果
     *
     * @param command 命令
     */
    @Override
    public void notifyCommandResult(XPushCommand command) {
        Iterator<WeakReference<IMessageObserver>> it = mObservers.iterator();
        synchronized (mLock) {
            while (it.hasNext()) {
                IMessageObserver observer = it.next().get();
                if (observer != null) {
                    observer.onCommandResult(command);
                } else {
                    it.remove();
                }
            }
        }
    }

    /**
     * 注册推送消息的订阅者
     *
     * @param observer 消息订阅者
     */
    @Override
    public boolean register(IMessageObserver observer) {
        if (observer != null) {
            WeakReference<IMessageObserver> obs = new WeakReference<>(observer);
            return mObservers.add(obs);
        } else {
            return false;
        }
    }

    /**
     * 注销推送消息的订阅者
     *
     * @param observer 消息订阅者
     */
    @Override
    public boolean unregister(IMessageObserver observer) {
        if (observer != null) {
            Iterator<WeakReference<IMessageObserver>> it = mObservers.iterator();
            synchronized (mLock) {
                while (it.hasNext()) {
                    IMessageObserver sb = it.next().get();
                    if (sb == observer) {
                        it.remove();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 注销所有的推送消息的订阅者
     */
    @Override
    public void unregisterAll() {
        if (mObservers != null) {
            mObservers.clear();
        }
    }
}
