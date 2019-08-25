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

package com.xuexiang.xpush.core.queue;

import android.support.annotation.NonNull;

import com.xuexiang.xpush.entity.CustomMessage;
import com.xuexiang.xpush.entity.Notification;

/**
 * 推送消息过滤策略
 *
 * @author xuexiang
 * @since 2019-08-25 19:51
 */
public interface IMessageFilterStrategy {

    /**
     * 过滤通知
     *
     * @param notification 通知
     * @return {@code true}：过滤通知 <br> {@code false}：不过滤通知
     */
    boolean filterNotification(Notification notification);

    /**
     * 过滤自定义(透传)消息
     *
     * @param message 自定义消息
     * @return {@code true}：过滤自定义消息 <br> {@code false}：不过滤自定义消息
     */
    boolean filterCustomMessage(CustomMessage message);

    /**
     * 增加消息过滤器
     *
     * @param filter 消息过滤器
     */
    void addFilter(@NonNull IMessageFilter filter);

    /**
     * 增加消息过滤器
     *
     * @param index  索引
     * @param filter 消息过滤器
     */
    void addFilter(int index, @NonNull IMessageFilter filter);

    /**
     * 增加消息过滤器
     *
     * @param filters 消息过滤器
     */
    void addFilters(@NonNull IMessageFilter... filters);

    /**
     * 设置消息过滤器
     *
     * @param filters 消息过滤器
     */
    void setFilters(@NonNull IMessageFilter... filters);

    /**
     * 清除消息过滤器
     *
     * @param filter 消息过滤器
     * @return 是否清除成功
     */
    boolean removeFilter(@NonNull IMessageFilter filter);

    /**
     * 清除消息过滤器
     *
     * @param filters 消息过滤器
     */
    void removeFilters(@NonNull IMessageFilter... filters);

    /**
     * 清除所有的消息过滤器
     */
    void removeAll();

}
