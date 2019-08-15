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

import android.content.Context;

import com.xuexiang.xpush.entity.XPushCommand;
import com.xuexiang.xpush.entity.XPushMsg;

/**
 * 消息推送接收器
 *
 * @author xuexiang
 * @since 2019-08-15 13:58
 */
public interface IPushReceiver {

    /**
     * 收到通知
     *
     * @param context
     * @param msg
     */
    void onNotification(Context context, XPushMsg msg);

    /**
     * 收到通知点击事件
     *
     * @param context
     * @param msg
     */
    void onNotificationClick(Context context, XPushMsg msg);

    /**
     * 收到自定义消息
     *
     * @param context
     * @param msg
     */
    void onMessageReceived(Context context, XPushMsg msg);

    /**
     * IPushClient执行命令的结果返回
     *
     * @param context
     * @param command
     * @see IPushClient#register()
     * @see IPushClient#unRegister()
     * @see IPushClient#addTag(String)
     * @see IPushClient#deleteTag(String)
     * @see IPushClient#bindAlias(String)
     * @see IPushClient#unBindAlias(String)
     */
    void onCommandResult(Context context, XPushCommand command);

}
