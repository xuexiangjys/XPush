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

package com.xuexiang.xpush.core.dispatcher.impl;

import android.content.ComponentName;
import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.core.dispatcher.IPushDispatcher;
import com.xuexiang.xpush.core.receiver.impl.AbstractPushReceiver;
import com.xuexiang.xpush.util.TransmitDataUtils;

/**
 * 解决Android 8.0（26)之后静态广播注册失效的问题方案二，推荐使用
 *
 * @author xuexiang
 * @since 2019-08-24 23:53
 */
public class Android26PushDispatcherImpl implements IPushDispatcher {

    private ComponentName mComponent;

    public Android26PushDispatcherImpl(@NonNull Class<? extends AbstractPushReceiver> cls) {
        mComponent = new ComponentName(XPush.getContext(), cls);
    }

    @Override
    public void transmit(Context context, String action, Parcelable data) {
        TransmitDataUtils.sendPushData(context, action, mComponent, data);
    }
}
