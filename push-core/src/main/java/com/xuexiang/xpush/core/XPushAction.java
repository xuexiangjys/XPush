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

/**
 * 推送广播动作
 *
 * @author xuexiang
 * @since 2019-08-15 18:08
 */
public interface XPushAction {

    /**
     *
     */
    String RECEIVE_NOTIFICATION = "com.xuexiang.xpush.core.ACTION_RECEIVE_NOTIFICATION";
    /**
     *
     */
    String RECEIVE_NOTIFICATION_CLICK = "com.xuexiang.xpush.core.ACTION_RECEIVE_NOTIFICATION_CLICK";
    /**
     *
     */
    String RECEIVE_MESSAGE = "com.xuexiang.xpush.core.ACTION_RECEIVE_MESSAGE";
    /**
     *
     */
    String RECEIVE_COMMAND_RESULT = "com.xuexiang.xpush.core.ACTION_RECEIVE_COMMAND_RESULT";

}
