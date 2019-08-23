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

package com.xuexiang.xpush.huawei;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.peng.one.push.OneRepeater;
import com.xuexiang.xpush.XPush;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author xuexiang
 * @since 2019-08-23 17:59
 */
public class NotificationClickActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        if (uri != null) {
            String title = uri.getQueryParameter("title");
            String content = uri.getQueryParameter("content");
            String extraMsg = uri.getQueryParameter("extraMsg");
            String keyValue = uri.getQueryParameter("keyValue");
            XPush.transmitNotificationClick(this, -1, title, content, extraMsg, json2Map(keyValue));
        }
        finish();
    }

    /**
     * json转换map
     *
     * @param json
     * @return
     */
    private Map<String, String> json2Map(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            Map<String, String> map = new HashMap<>();
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = jsonObject.getString(key);
                map.put(key, value);
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
