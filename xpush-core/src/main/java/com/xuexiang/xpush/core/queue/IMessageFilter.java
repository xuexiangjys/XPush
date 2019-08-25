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

import com.xuexiang.xpush.entity.CustomMessage;
import com.xuexiang.xpush.entity.Notification;

/**
 * 消息过滤器
 *
 * @author xuexiang
 * @since 2019-08-18 23:44
 */
public interface IMessageFilter {

    /**
     * 过滤通知
     *
     * @param notification 通知
     * @return {@code true}：过滤通知 <br> {@code false}：不过滤通知
     */
    boolean filter(Notification notification);

    /**
     * 过滤自定义消息
     *
     * @param message 自定义消息
     * @return {@code true}：过滤自定义消息 <br> {@code false}：不过滤自定义消息
     */
    boolean filter(CustomMessage message);

}
