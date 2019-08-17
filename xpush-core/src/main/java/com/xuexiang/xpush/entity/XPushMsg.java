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

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * 推送消息实体
 *
 * @author xuexiang
 * @since 2019-08-15 11:52
 */
public class XPushMsg implements Parcelable {

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

    public XPushMsg() {

    }

    public XPushMsg(int id) {
        mId = id;
    }

    public XPushMsg(int id, String title, String content, String msg) {
        mId = id;
        mTitle = title;
        mContent = content;
        mMsg = msg;
    }

    public XPushMsg(int id, String title, String content, String msg, String extraMsg, Map<String, String> keyValue) {
        mId = id;
        mTitle = title;
        mContent = content;
        mMsg = msg;
        mExtraMsg = extraMsg;
        mKeyValue = keyValue;
    }

    public int getId() {
        return mId;
    }

    public XPushMsg setId(int id) {
        mId = id;
        return this;
    }

    public String getTitle() {
        return mTitle;
    }

    public XPushMsg setTitle(String title) {
        mTitle = title;
        return this;
    }

    public String getContent() {
        return mContent;
    }

    public XPushMsg setContent(String content) {
        mContent = content;
        return this;
    }

    public String getMsg() {
        return mMsg;
    }

    public XPushMsg setMsg(String msg) {
        mMsg = msg;
        return this;
    }

    public String getExtraMsg() {
        return mExtraMsg;
    }

    public XPushMsg setExtraMsg(String extraMsg) {
        mExtraMsg = extraMsg;
        return this;
    }

    public Map<String, String> getKeyValue() {
        return mKeyValue;
    }

    public XPushMsg setKeyValue(Map<String, String> keyValue) {
        mKeyValue = keyValue;
        return this;
    }

    /**
     * @return 转化为通知
     */
    public Notification toNotification() {
        return new Notification(mId, mTitle, mContent, mExtraMsg, mKeyValue);
    }

    /**
     * @return 转化为自定义消息
     */
    public CustomMessage toCustomMessage() {
        return new CustomMessage(mMsg, mExtraMsg, mKeyValue);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeString(mContent);
        dest.writeString(mMsg);
        dest.writeString(mExtraMsg);
        if (mKeyValue != null) {
            dest.writeInt(mKeyValue.size());
            for (Map.Entry<String, String> entry : mKeyValue.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeString(entry.getValue());
            }
        }
    }


    protected XPushMsg(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mContent = in.readString();
        mMsg = in.readString();
        mExtraMsg = in.readString();
        int size = in.readInt();
        mKeyValue = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            String value = in.readString();
            mKeyValue.put(key, value);
        }
    }

    public static final Creator<XPushMsg> CREATOR = new Creator<XPushMsg>() {
        @Override
        public XPushMsg createFromParcel(Parcel in) {
            return new XPushMsg(in);
        }

        @Override
        public XPushMsg[] newArray(int size) {
            return new XPushMsg[size];
        }
    };

    @Override
    public String toString() {
        return "XPushMsg{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mContent='" + mContent + '\'' +
                ", mMsg='" + mMsg + '\'' +
                ", mExtraMsg='" + mExtraMsg + '\'' +
                ", mKeyValue=" + mKeyValue +
                '}';
    }
}
