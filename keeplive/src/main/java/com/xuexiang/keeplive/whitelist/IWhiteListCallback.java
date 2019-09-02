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

package com.xuexiang.keeplive.whitelist;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * 白名单跳转回调
 *
 * @author xuexiang
 * @since 2019-09-02 15:01
 */
public interface IWhiteListCallback {

    /**
     * 初始化
     *
     * @param target  需要加入白名单的目标
     * @param appName 需要处理白名单的应用名
     */
    void init(@NonNull String target, @NonNull String appName);

    /**
     * 显示白名单
     *
     * @param activity
     * @param intentWrapper
     */
    void showWhiteList(@NonNull Activity activity, @NonNull WhiteListIntentWrapper intentWrapper);

    /**
     * 显示白名单
     *
     * @param fragment
     * @param intentWrapper
     */
    void showWhiteList(@NonNull Fragment fragment, @NonNull WhiteListIntentWrapper intentWrapper);

}
