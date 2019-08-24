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

package com.xuexiang.xpush.umeng;

import android.app.Notification;
import android.content.Context;

import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.logs.PushLog;

/**
 * 友盟推送消息处理
 *
 * @author xuexiang
 * @since 2019-08-20 9:10
 */
public class XPushUmengMessageHandler extends UmengMessageHandler {

    private static final String TAG = "UmengPush-";

    @Override
    public void dealWithCustomMessage(Context context, UMessage uMessage) {
        PushLog.d(TAG + "[dealWithCustomMessage]:" + uMessage);
        XPush.transmitMessage(context, uMessage.custom, null, uMessage.extra);
    }

    @Override
    public Notification getNotification(Context context, UMessage uMessage) {
        PushLog.d(TAG + "[getNotification]:" + uMessage);
        XPush.transmitNotification(context, 0, uMessage.title, uMessage.text, uMessage.custom, uMessage.extra);
        return null;
    }
}
