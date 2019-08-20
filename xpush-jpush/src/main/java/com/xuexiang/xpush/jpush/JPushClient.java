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

package com.xuexiang.xpush.jpush;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.core.IPushClient;
import com.xuexiang.xpush.logs.PushLog;
import com.xuexiang.xpush.util.PushUtils;

import cn.jpush.android.api.JPushInterface;

import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_ADD_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_BIND_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_DEL_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_GET_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_GET_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_REGISTER;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_UNBIND_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_UNREGISTER;
import static com.xuexiang.xpush.core.annotation.ResultCode.RESULT_OK;

/**
 * 极光推送客户端
 *
 * @author xuexiang
 * @since 2019-08-16 17:05
 */
public class JPushClient implements IPushClient {

    public static final String JPUSH_PLATFORM_NAME = "JPush";
    public static final int JPUSH_PLATFORM_CODE = 1000;

    private Context mContext;

    private Handler mHandler = new Handler();

    @Override
    public void init(Context context) {
        mContext = context.getApplicationContext();
        JPushInterface.setDebugMode(PushLog.isDebug());
        JPushInterface.init(context);
    }

    @Override
    public void register() {
        if (JPushInterface.isPushStopped(mContext)) {
            JPushInterface.resumePush(mContext);
        }
        String token = JPushInterface.getRegistrationID(mContext);
        if (!TextUtils.isEmpty(token)) {
            XPush.transmitCommandResult(mContext, TYPE_REGISTER, RESULT_OK, token, null, null);
        }
    }

    @Override
    public void unRegister() {
        JPushInterface.stopPush(mContext);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (JPushInterface.isPushStopped(mContext)) {
                    XPush.transmitCommandResult(mContext, TYPE_UNREGISTER, RESULT_OK, null, null, null);
                }
            }
        }, 200);
    }

    @Override
    public void bindAlias(String alias) {
        JPushInterface.setAlias(mContext, TYPE_BIND_ALIAS, alias);
    }

    @Override
    public void unBindAlias(String alias) {
        JPushInterface.deleteAlias(mContext, TYPE_UNBIND_ALIAS);
    }

    /**
     * 获取别名
     */
    @Override
    public void getAlias() {
        JPushInterface.getAlias(mContext, TYPE_GET_ALIAS);
    }

    @Override
    public void addTags(String... tag) {
        JPushInterface.addTags(mContext, TYPE_ADD_TAG, PushUtils.array2Set(tag));
    }

    @Override
    public void deleteTags(String... tag) {
        JPushInterface.deleteTags(mContext, TYPE_DEL_TAG, PushUtils.array2Set(tag));
    }

    /**
     * 获取标签
     */
    @Override
    public void getTags() {
        JPushInterface.getAllTags(mContext, TYPE_GET_TAG);
    }

    /**
     * @return 获取推送令牌
     */
    @Override
    public String getPushToken() {
        return JPushInterface.getRegistrationID(mContext);
    }

    @Override
    public int getPlatformCode() {
        return JPUSH_PLATFORM_CODE;
    }

    @Override
    public String getPlatformName() {
        return JPUSH_PLATFORM_NAME;
    }
}
