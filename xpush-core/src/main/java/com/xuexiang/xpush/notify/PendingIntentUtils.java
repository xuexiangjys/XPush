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

package com.xuexiang.xpush.notify;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;

import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.util.Utils;

import java.util.Map;

/**
 * 跳转意图
 *
 * @author xuexiang
 * @since 2019-08-17 16:20
 */
public final class PendingIntentUtils {

    //==================构建跳转Activity的意图======================//

    private PendingIntentUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 构建跳转Activity的意图
     *
     * @param clazz
     * @return
     */
    public static PendingIntent buildActivityIntent(Class<? extends Activity> clazz) {
        return buildActivityIntent(clazz, 0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 构建跳转Activity的意图
     *
     * @param clazz
     * @return
     */
    public static PendingIntent buildActivityIntent(Class<? extends Activity> clazz, String key, Object param) {
        return buildActivityIntent(clazz, key, param, 0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 构建跳转Activity的意图
     *
     * @param clazz
     * @return
     */
    public static PendingIntent buildActivityIntent(Class<? extends Activity> clazz, Map<String, Object> map) {
        return buildActivityIntent(clazz, map, 0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 构建跳转Activity的意图
     *
     * @param clazz       Activity类
     * @param requestCode 请求码
     * @param flags
     * @return
     */
    public static PendingIntent buildActivityIntent(Class<? extends Activity> clazz, int requestCode, int flags) {
        Intent intent = Utils.getActivityIntent(clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(XPush.getContext(), requestCode, intent, flags);
    }

    /**
     * 构建跳转Activity的意图
     *
     * @param clazz       Activity类
     * @param key
     * @param param
     * @param requestCode 请求码
     * @param flags
     * @return
     */
    public static PendingIntent buildActivityIntent(Class<? extends Activity> clazz, String key, Object param, int requestCode, int flags) {
        Intent intent = Utils.getActivityIntent(clazz, key, param);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(XPush.getContext(), requestCode, intent, flags);
    }

    /**
     * 构建跳转Activity的意图
     *
     * @param clazz       Activity类
     * @param map         携带的数据
     * @param requestCode 请求码
     * @param flags
     * @return
     */
    public static PendingIntent buildActivityIntent(Class<? extends Activity> clazz, Map<String, Object> map, int requestCode, int flags) {
        Intent intent = Utils.getActivityIntent(clazz, map);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(XPush.getContext(), requestCode, intent, flags);
    }

    //==================构建广播的意图======================//

    /**
     * 构建广播的意图
     *
     * @param action      广播动作
     * @param requestCode 请求码
     * @return
     */
    public static PendingIntent buildBroadcastIntent(String action, int requestCode) {
        Intent intent = Utils.getBroadCastIntent(action);
        return PendingIntent.getBroadcast(XPush.getContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 构建广播的意图
     *
     * @param cls         广播接收器
     * @param requestCode 请求码
     * @return
     */
    public static PendingIntent buildBroadcastIntent(Class<? extends BroadcastReceiver> cls, int requestCode) {
        Intent intent = Utils.getBroadCastIntent(cls);
        return PendingIntent.getBroadcast(XPush.getContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 构建广播的意图
     *
     * @param action      广播动作
     * @param key
     * @param param
     * @param requestCode 请求码
     * @return
     */
    public static PendingIntent buildBroadcastIntent(String action, String key, Object param, int requestCode) {
        Intent intent = Utils.getBroadCastIntent(action);
        intent = Utils.putExtra(intent, key, param);
        return PendingIntent.getBroadcast(XPush.getContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 构建广播的意图
     *
     * @param cls         广播接收器
     * @param key
     * @param param
     * @param requestCode 请求码
     * @return
     */
    public static PendingIntent buildBroadcastIntent(Class<? extends BroadcastReceiver> cls, String key, Object param, int requestCode) {
        Intent intent = Utils.getBroadCastIntent(cls);
        intent = Utils.putExtra(intent, key, param);
        return PendingIntent.getBroadcast(XPush.getContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 构建广播的意图
     *
     * @param cls         广播接收器
     * @param action      广播动作
     * @param requestCode 请求码
     * @return
     */
    public static PendingIntent buildBroadcastIntent(Class<? extends BroadcastReceiver> cls, String action, int requestCode) {
        Intent intent = Utils.getBroadCastIntent(cls, action);
        return PendingIntent.getBroadcast(XPush.getContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 构建广播的意图
     *
     * @param cls         广播接收器
     * @param action      广播动作
     * @param key
     * @param param
     * @param requestCode 请求码
     * @return
     */
    public static PendingIntent buildBroadcastIntent(Class<? extends BroadcastReceiver> cls, String action, String key, Object param, int requestCode) {
        Intent intent = Utils.getBroadCastIntent(cls, action);
        intent = Utils.putExtra(intent, key, param);
        return PendingIntent.getBroadcast(XPush.getContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 构建广播的意图
     *
     * @param cls         广播接收器
     * @param action      广播动作
     * @param map         携带的数据
     * @param requestCode 请求码
     * @return
     */
    public static PendingIntent buildBroadcastIntent(Class<? extends BroadcastReceiver> cls, String action, Map<String, Object> map, int requestCode) {
        Intent intent = Utils.getBroadCastIntent(cls, action, map);
        return PendingIntent.getBroadcast(XPush.getContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
