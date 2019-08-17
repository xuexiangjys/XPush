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

package com.xuexiang.xpush.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.xuexiang.xpush.XPush;

import java.io.Serializable;
import java.util.Map;

/**
 * @author xuexiang
 * @since 2019-08-17 16:22
 */
public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取Activity跳转显式意图
     *
     * @param cls Activity类
     * @return
     */
    @NonNull
    public static Intent getActivityIntent(final Class<? extends Activity> cls) {
        return getIntent(XPush.getContext(), cls, null, true);
    }

    /**
     * 获取Activity跳转意图
     *
     * @param cls   Activity类
     * @param key
     * @param param
     * @return
     */
    public static Intent getActivityIntent(final Class<? extends Activity> cls, final String key, final Object param) {
        Intent intent = getActivityIntent(cls);
        return putExtra(intent, key, param);
    }

    /**
     * 获取Activity跳转意图
     *
     * @param cls Activity类
     * @param map 携带的数据
     * @return
     */
    public static Intent getActivityIntent(final Class<? extends Activity> cls, final Map<String, Object> map) {
        Intent intent = getActivityIntent(cls);
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                intent = putExtra(intent, entry.getKey(), entry.getValue());
            }
        }
        return intent;
    }

    /**
     * 传递数据
     *
     * @param intent
     * @param key    关键字
     * @param param  数据
     * @return
     */
    public static Intent putExtra(Intent intent, String key, Object param) {
        if (param instanceof Serializable) {
            intent.putExtra(key, (Serializable) param);
        } else if (param instanceof String) {
            intent.putExtra(key, (String) param);
        } else if (param instanceof String[]) {
            intent.putExtra(key, (String[]) param);
        } else if (param instanceof boolean[]) {
            intent.putExtra(key, (boolean[]) param);
        } else if (param instanceof short[]) {
            intent.putExtra(key, (short[]) param);
        } else if (param instanceof int[]) {
            intent.putExtra(key, (int[]) param);
        } else if (param instanceof long[]) {
            intent.putExtra(key, (long[]) param);
        } else if (param instanceof float[]) {
            intent.putExtra(key, (float[]) param);
        } else if (param instanceof double[]) {
            intent.putExtra(key, (double[]) param);
        } else if (param instanceof Bundle) {
            intent.putExtra(key, (Bundle) param);
        } else if (param instanceof byte[]) {
            intent.putExtra(key, (byte[]) param);
        } else if (param instanceof char[]) {
            intent.putExtra(key, (char[]) param);
        } else if (param instanceof Parcelable) {
            intent.putExtra(key, (Parcelable) param);
        } else if (param instanceof Parcelable[]) {
            intent.putExtra(key, (Parcelable[]) param);
        } else if (param instanceof CharSequence) {
            intent.putExtra(key, (CharSequence) param);
        } else if (param instanceof CharSequence[]) {
            intent.putExtra(key, (CharSequence[]) param);
        }
        return intent;
    }

    /**
     * 获取Intent意图
     *
     * @param context
     * @param cls     类名
     * @param action  动作
     * @return
     */
    @NonNull
    public static Intent getIntent(Context context, Class<?> cls, String action, boolean isNewTask) {
        Intent intent = new Intent();
        if (cls != null) {
            intent.setClass(context, cls);
        }
        if (action != null) {
            intent.setAction(action);
        }
        if (isNewTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }


    /**
     * 获取广播意图
     *
     * @param action 广播动作
     * @return
     */
    @NonNull
    public static Intent getBroadCastIntent(String action) {
        return getBroadCastIntent(XPush.getContext(), null, action);
    }

    /**
     * 获取广播意图
     *
     * @param context
     * @param cls     广播接收器
     * @param action  广播动作
     * @return
     */
    @NonNull
    public static Intent getBroadCastIntent(Context context, Class<? extends BroadcastReceiver> cls, String action) {
        return getIntent(context, cls, action);
    }

    /**
     * 获取广播意图
     *
     * @param cls 广播接收器
     * @return
     */
    @NonNull
    public static Intent getBroadCastIntent(Class<? extends BroadcastReceiver> cls) {
        return getBroadCastIntent(XPush.getContext(), cls, null);
    }

    /**
     * 获取广播意图
     *
     * @param cls    广播接收器
     * @param action 广播动作
     * @return
     */
    @NonNull
    public static Intent getBroadCastIntent(Class<? extends BroadcastReceiver> cls, String action) {
        return getIntent(XPush.getContext(), cls, action);
    }


    /**
     * 获取广播意图
     *
     * @param cls    广播接收器
     * @param action 广播动作
     * @param map    携带的数据
     * @return
     */
    public static Intent getBroadCastIntent(Class<? extends BroadcastReceiver> cls, String action, Map<String, Object> map) {
        Intent intent = getBroadCastIntent(cls, action);
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                intent = putExtra(intent, entry.getKey(), entry.getValue());
            }
        }
        return intent;
    }

    /**
     * 获取Intent意图
     *
     * @param context
     * @param cls     类名
     * @param action  动作
     * @return
     */
    @NonNull
    public static Intent getIntent(Context context, Class<?> cls, String action) {
        return getIntent(context, cls, action, false);
    }


}
