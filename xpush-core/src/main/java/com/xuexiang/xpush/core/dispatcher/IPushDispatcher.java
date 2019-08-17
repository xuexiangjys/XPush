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

package com.xuexiang.xpush.core.dispatcher;

import android.content.Context;
import android.os.Parcelable;

import com.xuexiang.xpush.core.annotation.PushAction;

/**
 * 消息推送事件转发器
 *
 * @author xuexiang
 * @since 2019-08-16 9:07
 */
public interface IPushDispatcher {

    /**
     * 转译消息
     *
     * @param context
     * @param action  动作
     * @param data    数据
     */
    void transmit(Context context, @PushAction String action, Parcelable data);

}
