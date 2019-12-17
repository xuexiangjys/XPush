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

import com.xuexiang.xpush.core.annotation.ResultCode;

import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_ADD_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_AND_OR_DEL_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_BIND_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_DEL_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_GET_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_GET_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_REGISTER;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_UNBIND_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_UNREGISTER;

/**
 * IPushClient推送执行命令结果
 *
 * @author xuexiang
 * @since 2019-08-15 15:09
 */
public class XPushCommand implements Parcelable {

    /**
     * 命令类型
     */
    private int mType;
    /**
     * 结果码
     */
    private int mResultCode;
    /**
     * 命令内容
     */
    private String mContent;
    /**
     * 拓展字段
     */
    private String mExtraMsg;
    /**
     * 错误信息
     */
    private String mError;

    public XPushCommand() {
    }


    /**
     * 构造方法
     *
     * @param type       命令类型
     * @param resultCode 结果码
     * @param content    内容
     * @param extraMsg   额外信息
     * @param error      错误信息
     */
    public XPushCommand(int type, int resultCode, String content, String extraMsg, String error) {
        mType = type;
        mResultCode = resultCode;
        mContent = content;
        mExtraMsg = extraMsg;
        mError = error;
    }

    public int getType() {
        return mType;
    }

    public XPushCommand setType(int type) {
        mType = type;
        return this;
    }

    public int getResultCode() {
        return mResultCode;
    }

    public XPushCommand setResultCode(int resultCode) {
        mResultCode = resultCode;
        return this;
    }

    public String getContent() {
        return mContent;
    }

    public XPushCommand setContent(String content) {
        mContent = content;
        return this;
    }

    public String getExtraMsg() {
        return mExtraMsg;
    }

    public XPushCommand setExtraMsg(String extraMsg) {
        mExtraMsg = extraMsg;
        return this;
    }

    public String getError() {
        return mError;
    }

    public XPushCommand setError(String error) {
        mError = error;
        return this;
    }

    public boolean isSuccess() {
        return mResultCode == ResultCode.RESULT_OK;
    }

    public String getTypeText(int type) {
        switch (type) {
            case TYPE_REGISTER:
                return "TYPE_REGISTER";
            case TYPE_UNREGISTER:
                return "TYPE_UNREGISTER";
            case TYPE_ADD_TAG:
                return "TYPE_ADD_TAG";
            case TYPE_DEL_TAG:
                return "TYPE_DEL_TAG";
            case TYPE_BIND_ALIAS:
                return "TYPE_BIND_ALIAS";
            case TYPE_UNBIND_ALIAS:
                return "TYPE_UNBIND_ALIAS";
            case TYPE_AND_OR_DEL_TAG:
                return "TYPE_AND_OR_DEL_TAG";
            default:
                return "";
        }
    }

    /**
     * 获取执行命令的类型名
     *
     * @param type
     * @return
     */
    public String getTypeName(int type) {
        switch (type) {
            case TYPE_REGISTER:
                return "注册推送";
            case TYPE_UNREGISTER:
                return "取消注册推送";
            case TYPE_ADD_TAG:
                return "添加标签";
            case TYPE_DEL_TAG:
                return "删除标签";
            case TYPE_GET_TAG:
                return "获取标签";
            case TYPE_BIND_ALIAS:
                return "绑定别名";
            case TYPE_UNBIND_ALIAS:
                return "解绑别名";
            case TYPE_GET_ALIAS:
                return "获取别名";
            case TYPE_AND_OR_DEL_TAG:
                return "添加或删除标签";
            default:
                return "未知操作";
        }
    }

    /**
     * @return 获取命令的描述信息
     */
    public String getDescription() {
        return getTypeName(mType) + (isSuccess() ? "成功：" + mContent : "失败:" + mError);
    }

    protected XPushCommand(Parcel in) {
        mType = in.readInt();
        mResultCode = in.readInt();
        mContent = in.readString();
        mExtraMsg = in.readString();
        mError = in.readString();
    }

    public static final Creator<XPushCommand> CREATOR = new Creator<XPushCommand>() {
        @Override
        public XPushCommand createFromParcel(Parcel in) {
            return new XPushCommand(in);
        }

        @Override
        public XPushCommand[] newArray(int size) {
            return new XPushCommand[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mType);
        dest.writeInt(mResultCode);
        dest.writeString(mContent);
        dest.writeString(mExtraMsg);
        dest.writeString(mError);
    }

    @Override
    public String toString() {
        return "XPushCommand{" +
                "mType=" + mType +
                ", mResultCode=" + mResultCode +
                ", mContent='" + mContent + '\'' +
                ", mExtraMsg='" + mExtraMsg + '\'' +
                ", mError='" + mError + '\'' +
                '}';
    }
}
