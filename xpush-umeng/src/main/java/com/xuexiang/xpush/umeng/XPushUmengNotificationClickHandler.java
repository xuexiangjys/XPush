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

import android.content.Context;

import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.logs.PushLog;

/**
 * 友盟推送通知点击处理
 *
 * @author xuexiang
 * @since 2019-08-20 9:09
 */
public class XPushUmengNotificationClickHandler extends UmengNotificationClickHandler {

    private static final String TAG = "UmengPush-";

    @Override
    public void dealWithCustomAction(Context context, UMessage uMessage) {
        PushLog.d(TAG + "[dealWithCustomAction]:" + uMessage);
        XPush.transmitNotificationClick(context, 0, uMessage.title, uMessage.text, uMessage.custom, uMessage.extra);
    }

}
