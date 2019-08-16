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
 * 推送初始化监听器
 *
 * @author xuexiang
 * @since 2019-08-16 11:28
 */
public interface IPushInitCallback {

    /**
     * 推送初始化监听
     *
     * @param platformCode 平台码
     * @param platformName 平台名
     * @return {@code true: 选中} <br> {@code false: 不选中}
     */
    boolean onInitPush(int platformCode, String platformName);

}
