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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.app.Fragment;

import com.xuexiang.keeplive.KeepLive;

import java.util.List;

/**
 * 加入系统白名单的意图信息
 *
 * @author xuexiang
 * @since 2019-09-02 14:43
 */
public final class WhiteListIntentWrapper {

    /**
     * 跳转意图
     */
    private Intent mIntent;
    /**
     * 类型
     */
    private int mType;

    public WhiteListIntentWrapper(Intent intent, int type) {
        mIntent = intent;
        mType = type;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public WhiteListIntentWrapper setIntent(Intent intent) {
        mIntent = intent;
        return this;
    }

    public int getType() {
        return mType;
    }

    public WhiteListIntentWrapper setType(int type) {
        mType = type;
        return this;
    }

    /**
     * 判断本机上是否有能处理当前Intent的Activity
     */
    public boolean doesActivityExists() {
        PackageManager pm = KeepLive.getApplication().getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mIntent, PackageManager.MATCH_DEFAULT_ONLY);
        return list != null && list.size() > 0;
    }

    /**
     * 安全地启动一个Activity
     */
    public void startActivitySafely(Activity activity) {
        try {
            activity.startActivity(mIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 安全地启动一个Activity
     */
    public void startActivitySafely(Fragment fragment) {
        try {
            fragment.startActivity(mIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 优先使用fragment进行跳转
     *
     * @param activity
     * @param fragment
     */
    public void startActivitySafely(Activity activity, Fragment fragment) {
        if (fragment != null) {
            startActivitySafely(fragment);
        } else {
            startActivitySafely(activity);
        }
    }

    @Override
    public String toString() {
        return "WhiteListIntentWrapper{" +
                "mIntent=" + mIntent +
                ", mType=" + mType +
                '}';
    }
}
