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

package com.xuexiang.xpush.entity;

/**
 * 推送码
 *
 * @author xuexiang
 * @since 2019-08-15 14:07
 */
public interface XPushCode {

    /**
     * 成功
     */
    int RESULT_OK = 0;
    /**
     * 失败
     */
    int RESULT_ERROR = 1;



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
     * 绑定别名
     */
    int TYPE_BIND_ALIAS = 2004;

    /**
     * 解绑别名
     */
    int TYPE_UNBIND_ALIAS = 2005;

    /**
     * 添加或删除标签
     */
    int TYPE_AND_OR_DEL_TAG = 2006;

}
