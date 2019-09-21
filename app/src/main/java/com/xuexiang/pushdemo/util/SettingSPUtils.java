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

package com.xuexiang.pushdemo.util;

import android.content.Context;

import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.data.BaseSPUtil;

/**
 * @author xuexiang
 * @since 2019-09-20 16:00
 */
public class SettingSPUtils extends BaseSPUtil {

    private static volatile SettingSPUtils sInstance = null;

    public SettingSPUtils(Context context) {
        super(context);
    }

    private final String KEY_SELECT_PUSH_PLATFORM = "key_select_push_platform";

    /**
     * 获取单例
     */
    public static SettingSPUtils getInstance() {
        if (sInstance == null) {
            synchronized (SettingSPUtils.class) {
                if (sInstance == null) {
                    sInstance = new SettingSPUtils(XUtil.getContext());
                }
            }
        }
        return sInstance;
    }


    public int getPushPlatformCode() {
        return getInt(KEY_SELECT_PUSH_PLATFORM, 0);
    }

    public void setPushPlatformCode(int platformCode) {
        putInt(KEY_SELECT_PUSH_PLATFORM, platformCode);
    }

}
