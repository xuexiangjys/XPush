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

package com.xuexiang.xpush.core.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.xuexiang.xpush.core.annotation.ConnectStatus.CONNECTED;
import static com.xuexiang.xpush.core.annotation.ConnectStatus.CONNECTING;
import static com.xuexiang.xpush.core.annotation.ConnectStatus.DISCONNECT;

/**
 * 推送连接状态
 *
 * @author xuexiang
 * @since 2019-08-17 11:01
 */
@IntDef({DISCONNECT, CONNECTING, CONNECTED})
@Retention(RetentionPolicy.SOURCE)
public @interface ConnectStatus {
    /**
     * 已断开
     */
    int DISCONNECT = 10;
    /**
     * 连接中
     */
    int CONNECTING = 11;
    /**
     * 已连接
     */
    int CONNECTED = 12;

}
