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
 * 推送通知
 *
 * @author xuexiang
 * @since 2019-08-17 14:37
 */
public class Notification {

    /**
     * 消息ID / 状态
     */
    private int mId;
    /**
     * 通知标题
     */
    private String mTitle;
    /**
     * 通知内容
     */
    private String mContent;
    /**
     * 消息拓展字段
     */
    private String mExtraMsg;
    /**
     * 消息键值对(初始化值，防止序列化出错)
     */
    private Map<String, String> mKeyValue;

    public Notification() {

    }

    public Notification(int id, String title, String content, String extraMsg, Map<String, String> keyValue) {
        mId = id;
        mTitle = title;
        mContent = content;
        mExtraMsg = extraMsg;
        mKeyValue = keyValue;
    }

    public int getId() {
        return mId;
    }

    public Notification setId(int id) {
        mId = id;
        return this;
    }

    public String getTitle() {
        return mTitle;
    }

    public Notification setTitle(String title) {
        mTitle = title;
        return this;
    }

    public String getContent() {
        return mContent;
    }

    public Notification setContent(String content) {
        mContent = content;
        return this;
    }

    public String getExtraMsg() {
        return mExtraMsg;
    }

    public Notification setExtraMsg(String extraMsg) {
        mExtraMsg = extraMsg;
        return this;
    }

    public Map<String, String> getKeyValue() {
        return mKeyValue;
    }

    public Notification setKeyValue(Map<String, String> keyValue) {
        mKeyValue = keyValue;
        return this;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mContent='" + mContent + '\'' +
                ", mExtraMsg='" + mExtraMsg + '\'' +
                ", mKeyValue=" + mKeyValue +
                '}';
    }
}
