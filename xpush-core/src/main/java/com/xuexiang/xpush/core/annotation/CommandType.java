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

import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_ADD_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_AND_OR_DEL_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_BIND_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_DEL_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_GET_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_GET_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_REGISTER;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_UNBIND_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_UNREGISTER;


/**
 * 命令类型
 *
 * @author xuexiang
 * @since 2019-08-17 11:27
 */
@IntDef({TYPE_REGISTER, TYPE_UNREGISTER, TYPE_ADD_TAG, TYPE_DEL_TAG, TYPE_GET_TAG, TYPE_BIND_ALIAS, TYPE_UNBIND_ALIAS, TYPE_GET_ALIAS, TYPE_AND_OR_DEL_TAG})
@Retention(RetentionPolicy.SOURCE)
public @interface CommandType {

    /**
     * 注册推送
     */
    int TYPE_REGISTER = 2000;
    /**
     * 取消注册推送
     */
    int TYPE_UNREGISTER = 2001;

    /**
     * 添加标签
     */
    int TYPE_ADD_TAG = 2002;
    /**
     * 删除标签
     */
    int TYPE_DEL_TAG = 2003;
    /**
     * 获取标签
     */
    int TYPE_GET_TAG = 2004;

    /**
     * 绑定别名
     */
    int TYPE_BIND_ALIAS = 2005;
    /**
     * 解绑别名
     */
    int TYPE_UNBIND_ALIAS = 2006;
    /**
     * 获取别名
     */
    int TYPE_GET_ALIAS = 2007;

    /**
     * 添加或删除标签
     */
    int TYPE_AND_OR_DEL_TAG = 2008;

}
