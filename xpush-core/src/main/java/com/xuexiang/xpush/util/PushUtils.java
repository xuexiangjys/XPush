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

import com.xuexiang.xpush.core.annotation.ConnectStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

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
            String next = keys.next();
            try {
                Object o = jsonObject.get(next);
                map.put(next, String.valueOf(o));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    
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
}
