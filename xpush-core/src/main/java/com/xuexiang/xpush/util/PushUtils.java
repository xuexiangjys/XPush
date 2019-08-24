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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.core.annotation.ConnectStatus;
import com.xuexiang.xpush.logs.PushLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.xuexiang.xpush.core.annotation.ConnectStatus.CONNECTED;
import static com.xuexiang.xpush.core.annotation.ConnectStatus.CONNECTING;
import static com.xuexiang.xpush.core.annotation.ConnectStatus.DISCONNECT;

/**
 * 推送工具类
 *
 * @author xuexiang
 * @since 2019-08-18 23:03
 */
public final class PushUtils {

    private PushUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static final String PREF_NAME = "XPush";
    private static final String KEY_CONNECT_STATUS = "key_connect_status";
    private static final String KEY_PUSH_TOKEN = "key_push_token_";


    /**
     * 格式化推送连接状态
     *
     * @return 推送连接状态
     */
    public static String formatConnectStatus(@ConnectStatus int connectStatus) {
        switch (connectStatus) {
            case DISCONNECT:
                return "已断开";
            case CONNECTING:
                return "连接中";
            case CONNECTED:
                return "已连接";
            default:
                return "未知状态";
        }
    }

    //=================SharedPreferences==================//

    public static SharedPreferences getPushSP() {
        return XPush.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 保存连接状态
     *
     * @param connectStatus 连接状态
     */
    public static void saveConnectStatus(@ConnectStatus int connectStatus) {
        getPushSP().edit().putInt(KEY_CONNECT_STATUS, connectStatus).apply();
    }

    /**
     * 获取连接状态
     */
    public static int getConnectStatus() {
        return getPushSP().getInt(KEY_CONNECT_STATUS, DISCONNECT);
    }

    /**
     * 保存推送token
     */
    public static void savePushToken(String platform, String token) {
        getPushSP().edit().putString(KEY_PUSH_TOKEN + platform, token).apply();
    }

    /**
     * 获取推送token
     */
    public static String getPushToken(String platform) {
        return getPushSP().getString(KEY_PUSH_TOKEN + platform, "");
    }

    /**
     * 清除推送token
     */
    public static void deletePushToken(String platform) {
        getPushSP().edit().remove(KEY_PUSH_TOKEN + platform).apply();
    }

    //=================数组、集合处理==================//

    /**
     * Set集合转String，以“,”隔开
     *
     * @param set
     * @return
     */
    public static String collection2String(Collection<String> set) {
        Iterator<String> iterator = set.iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            sb.append(iterator.next()).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 数组集合转String，以“,”隔开
     *
     * @param values
     * @return
     */
    public static String array2String(String... values) {
        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            sb.append(value).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 以“,”隔开的字符串转数组
     *
     * @param values
     * @return
     */
    public static String[] string2Array(String values) {
        if (!TextUtils.isEmpty(values)) {
            return values.split(",");
        }
        return new String[0];
    }

    /**
     * String数组转Set
     *
     * @param values
     * @return
     */
    public static Set<String> array2Set(String... values) {
        return new HashSet<>(Arrays.asList(values));
    }

    /**
     * json转换map
     *
     * @param json
     * @return
     */
    public static Map<String, String> json2Map(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject(json);
            Map<String, String> map = new HashMap<>();
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = jsonObject.get(key);
                map.put(key, String.valueOf(value));
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换成Map
     *
     * @param jsonObject
     * @return
     */
    public static HashMap<String, String> toMap(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        Iterator<String> keys = jsonObject.keys();
        HashMap<String, String> map = new HashMap<>();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                Object o = jsonObject.get(key);
                map.put(key, String.valueOf(o));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    //=================meta-data==================//

    /**
     * 获取manifest里注册的meta-data值集合
     *
     * @return meta-data值集合
     */
    @Nullable
    public static Bundle getMetaDatas(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
        } catch (PackageManager.NameNotFoundException e) {
            PushLog.e(e);
        }
        return null;
    }

    /**
     * 获取meta-data中的String类型的值
     *
     * @param key
     * @return String类型的值
     */
    @Nullable
    public static String getStringValueInMetaData(Context context, String key) {
        Bundle metaData = getMetaDatas(context);
        return metaData != null ? metaData.getString(key) : null;
    }

}
