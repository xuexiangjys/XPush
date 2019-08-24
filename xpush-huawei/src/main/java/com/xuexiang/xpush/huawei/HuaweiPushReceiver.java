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

package com.xuexiang.xpush.huawei;

import android.content.Context;

import com.huawei.hms.support.api.push.PushReceiver;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.logs.PushLog;
import com.xuexiang.xpush.util.PushUtils;

import java.nio.charset.Charset;

import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_REGISTER;
import static com.xuexiang.xpush.core.annotation.ConnectStatus.CONNECTED;
import static com.xuexiang.xpush.core.annotation.ConnectStatus.DISCONNECT;
import static com.xuexiang.xpush.core.annotation.ResultCode.RESULT_OK;
import static com.xuexiang.xpush.huawei.HuaweiPushClient.HUAWEI_PUSH_PLATFORM_NAME;

/**
 * 消息推送接收器
 *
 * @author xuexiang
 * @since 2019-08-23 15:21
 */
public class HuaweiPushReceiver extends PushReceiver {

    private static final String TAG = "HuaweiPush-";

    @Override
    public void onToken(Context context, String token) {
        PushLog.d(TAG + "[onToken]:" + token);
        PushUtils.savePushToken(HUAWEI_PUSH_PLATFORM_NAME, token);
        XPush.transmitCommandResult(context, TYPE_REGISTER, RESULT_OK, token, null, null);
    }

    @Override
    public void onPushState(Context context, boolean pushState) {
        PushLog.d(TAG + "[onPushState]:" + pushState);
        XPush.transmitConnectStatusChanged(context, pushState ? CONNECTED : DISCONNECT);
    }

    @Override
    public void onPushMsg(Context context, byte[] bytes, String token) {
        String msg = new String(bytes, Charset.forName("UTF-8"));
        PushLog.d(TAG + "[onPushMsg]:" + msg);
        XPush.transmitMessage(context, msg, null, null);
    }

}
