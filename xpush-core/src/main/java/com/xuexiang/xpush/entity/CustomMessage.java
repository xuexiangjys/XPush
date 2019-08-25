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

import java.util.Map;

/**
 * 自定义（透传）消息
 *
 * @author xuexiang
 * @since 2019-08-17 14:38
 */
public class CustomMessage {

    /**
     * 自定义消息
     */
    private String mMsg;
    /**
     * 消息拓展字段
     */
    private String mExtraMsg;
    /**
     * 消息键值对(初始化值，防止序列化出错)
     */
    private Map<String, String> mKeyValue;

    public CustomMessage() {
    }

    public CustomMessage(String msg, String extraMsg, Map<String, String> keyValue) {
        mMsg = msg;
        mExtraMsg = extraMsg;
        mKeyValue = keyValue;
    }

    public String getMsg() {
        return mMsg;
    }

    public CustomMessage setMsg(String msg) {
        mMsg = msg;
        return this;
    }

    public String getExtraMsg() {
        return mExtraMsg;
    }

    public CustomMessage setExtraMsg(String extraMsg) {
        mExtraMsg = extraMsg;
        return this;
    }

    public Map<String, String> getKeyValue() {
        return mKeyValue;
    }

    public CustomMessage setKeyValue(Map<String, String> keyValue) {
        mKeyValue = keyValue;
        return this;
    }

    @Override
    public String toString() {
        return "CustomMessage{" +
                "mMsg='" + mMsg + '\'' +
                ", mExtraMsg='" + mExtraMsg + '\'' +
                ", mKeyValue=" + mKeyValue +
                '}';
    }
}
