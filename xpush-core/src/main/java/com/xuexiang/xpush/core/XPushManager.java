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

import com.xuexiang.xpush.core.queue.IMessageFilter;
import com.xuexiang.xpush.core.queue.IMessageFilterStrategy;
import com.xuexiang.xpush.core.queue.IMessageObservable;
import com.xuexiang.xpush.core.queue.IMessageObserver;
import com.xuexiang.xpush.core.queue.impl.DefaultMessageFilterStrategyImpl;
import com.xuexiang.xpush.core.queue.impl.DefaultMessageObservableImpl;
import com.xuexiang.xpush.entity.CustomMessage;
import com.xuexiang.xpush.entity.Notification;
import com.xuexiang.xpush.entity.XPushCommand;
import com.xuexiang.xpush.util.PushUtils;

/**
 * 推送核心管理类
 *
 * @author xuexiang
 * @since 2019-08-17 15:39
 */
public class XPushManager implements IMessageObservable {

    private static volatile XPushManager sInstance = null;

    /**
     * 消息被观察者
     */
    private IMessageObservable mObservable;
    /**
     * 消息过滤策略
     */
    private IMessageFilterStrategy mMessageFilterStrategy;

    /**
     * 推送连接状态
     */
    private int mConnectStatus;


    private XPushManager() {
        mObservable = new DefaultMessageObservableImpl();
        mMessageFilterStrategy = new DefaultMessageFilterStrategyImpl();
        mConnectStatus = PushUtils.getConnectStatus();
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

    //=======================消息被观察者==============================//

    /**
     * @return 推送连接状态
     */
    public int getConnectStatus() {
        return mConnectStatus;
    }

    /**
     * 设置推送消息的被观察者
     *
     * @param observable
     * @return
     */
    public XPushManager setIMessageObservable(@NonNull IMessageObservable observable) {
        mObservable = observable;
        return this;
    }

    /**
     * 消息推送连接状态发生变化
     *
     * @param connectStatus 推送连接状态
     */
    @Override
    public void notifyConnectStatusChanged(int connectStatus) {
        mConnectStatus = connectStatus;
        PushUtils.saveConnectStatus(mConnectStatus);
        if (mObservable != null) {
            mObservable.notifyConnectStatusChanged(connectStatus);
        }
    }

    /**
     * 收到通知
     *
     * @param notification 被处理过的推送消息
     */
    @Override
    public void notifyNotification(Notification notification) {
        boolean isFiltered = filterNotification(notification);
        if (!isFiltered) {
            if (mObservable != null) {
                mObservable.notifyNotification(notification);
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
        boolean isFiltered = filterNotification(notification);
        if (!isFiltered) {
            if (mObservable != null) {
                mObservable.notifyNotificationClick(notification);
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
        boolean isFiltered = filterCustomMessage(message);
        if (!isFiltered) {
            if (mObservable != null) {
                mObservable.notifyMessageReceived(message);
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
        if (mObservable != null) {
            mObservable.notifyCommandResult(command);
        }
    }

    /**
     * 注册推送消息的订阅者
     *
     * @param subscriber 消息订阅者
     */
    @Override
    public boolean register(IMessageObserver subscriber) {
        if (mObservable != null) {
            return mObservable.register(subscriber);
        }
        return false;
    }

    /**
     * 注销推送消息的订阅者
     *
     * @param observer 消息订阅者
     */
    @Override
    public boolean unregister(IMessageObserver observer) {
        if (mObservable != null) {
            return mObservable.unregister(observer);
        }
        return false;
    }

    /**
     * 注销所有的推送消息的订阅者
     */
    @Override
    public void unregisterAll() {
        if (mObservable != null) {
            mObservable.unregisterAll();
        }
    }

    //=======================消息过滤策略==============================//

    /**
     * 设置消息过滤策略
     *
     * @param messageFilterStrategy
     * @return
     */
    public XPushManager setIMessageFilterStrategy(@NonNull IMessageFilterStrategy messageFilterStrategy) {
        mMessageFilterStrategy = messageFilterStrategy;
        return this;
    }

    /**
     * 过滤通知
     *
     * @param notification 通知
     * @return {@code true}：过滤通知 <br> {@code false}：不过滤通知
     */
    private boolean filterNotification(Notification notification) {
        return mMessageFilterStrategy != null && mMessageFilterStrategy.filterNotification(notification);
    }

    /**
     * 过滤自定义(透传)消息
     *
     * @param message 自定义消息
     * @return {@code true}：过滤自定义消息 <br> {@code false}：不过滤自定义消息
     */
    private boolean filterCustomMessage(CustomMessage message) {
        return mMessageFilterStrategy != null && mMessageFilterStrategy.filterCustomMessage(message);
    }

    /**
     * 增加消息过滤器
     *
     * @param filter 消息过滤器
     */
    public XPushManager addFilter(@NonNull IMessageFilter filter) {
        if (mMessageFilterStrategy != null) {
            mMessageFilterStrategy.addFilter(filter);
        }
        return this;
    }

    /**
     * 增加消息过滤器
     *
     * @param index  索引
     * @param filter 消息过滤器
     */
    public XPushManager addFilter(int index, @NonNull IMessageFilter filter) {
        if (mMessageFilterStrategy != null) {
            mMessageFilterStrategy.addFilter(index, filter);
        }
        return this;
    }

    /**
     * 增加消息过滤器
     *
     * @param filters 消息过滤器
     */
    public XPushManager addFilters(@NonNull IMessageFilter... filters) {
        if (mMessageFilterStrategy != null) {
            mMessageFilterStrategy.addFilters(filters);
        }
        return this;
    }

    /**
     * 设置消息过滤器
     *
     * @param filters 消息过滤器
     */
    public XPushManager setFilters(@NonNull IMessageFilter... filters) {
        if (mMessageFilterStrategy != null) {
            mMessageFilterStrategy.setFilters(filters);
        }
        return this;
    }

    /**
     * 清除消息过滤器
     *
     * @param filter 消息过滤器
     * @return 是否清除成功
     */
    public boolean removeFilter(@NonNull IMessageFilter filter) {
        return mMessageFilterStrategy != null && mMessageFilterStrategy.removeFilter(filter);
    }

    /**
     * 清除消息过滤器
     *
     * @param filters 消息过滤器
     */
    public void removeFilters(@NonNull IMessageFilter... filters) {
        if (mMessageFilterStrategy != null) {
            mMessageFilterStrategy.removeFilters(filters);
        }
    }

    /**
     * 清除所有的消息过滤器
     */
    public void removeAll() {
        if (mMessageFilterStrategy != null) {
            mMessageFilterStrategy.removeAll();
        }
    }

}
