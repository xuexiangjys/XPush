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

package com.xuexiang.xpush.umeng;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.core.IPushClient;
import com.xuexiang.xpush.core.XPushManager;
import com.xuexiang.xpush.util.PushUtils;

import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_ADD_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_BIND_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_DEL_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_REGISTER;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_UNBIND_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_UNREGISTER;
import static com.xuexiang.xpush.core.annotation.ConnectStatus.CONNECTED;
import static com.xuexiang.xpush.core.annotation.ConnectStatus.DISCONNECT;
import static com.xuexiang.xpush.core.annotation.ResultCode.RESULT_ERROR;
import static com.xuexiang.xpush.core.annotation.ResultCode.RESULT_OK;

/**
 * 友盟推送客户端
 * 1.获取tag和alias不支持
 * 2.推送连接状态不支持
 *
 * @author xuexiang
 * @since 2019-08-19 15:11
 */
public class UMengPushClient implements IPushClient {

    public static final String UMENG_PUSH_PLATFORM_NAME = "UMengPush";
    public static final int UMENG_PUSH_PLATFORM_CODE = 1001;

    private static final String XPUSH_ALIAS = "XPUSH_ALIAS";
    private static final String KEY_UMENG_APPKEY = "UMENG_APPKEY";
    private static final String KEY_UMENG_MESSAGE_SECRET = "UMENG_MESSAGE_SECRET";

    private PushAgent mPushAgent;

    private Application mApplication;

    private Application.ActivityLifecycleCallbacks mLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            mPushAgent.onAppStart();
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };

    /**
     * 初始化
     *
     * @param context
     */
    @Override
    public void init(Context context) {
        if (context instanceof Application) {
            mApplication = (Application) context;
        } else {
            mApplication = (Application) context.getApplicationContext();
        }
        UMConfigure.init(context,
                PushUtils.getStringValueInMetaData(context, KEY_UMENG_APPKEY),
                "umeng", UMConfigure.DEVICE_TYPE_PHONE,
                PushUtils.getStringValueInMetaData(context, KEY_UMENG_MESSAGE_SECRET));
        mPushAgent = PushAgent.getInstance(context);
        mPushAgent.setNotificationClickHandler(new XPushUmengNotificationClickHandler());
        mPushAgent.setMessageHandler(new XPushUmengMessageHandler());
        mPushAgent.setDisplayNotificationNumber(0);
    }

    /**
     * 注册推送
     */
    @Override
    public void register() {
        mApplication.registerActivityLifecycleCallbacks(mLifecycleCallbacks);
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                XPush.transmitCommandResult(mApplication, TYPE_REGISTER, RESULT_OK, deviceToken, null, "");
                mPushAgent.enable(null);
                XPushManager.get().notifyConnectStatusChanged(CONNECTED);
            }

            @Override
            public void onFailure(String s, String s1) {
                XPush.transmitCommandResult(mApplication, TYPE_REGISTER, RESULT_ERROR, null, s, s1);
                XPushManager.get().notifyConnectStatusChanged(DISCONNECT);
            }
        });
    }

    /**
     * 注销推送
     */
    @Override
    public void unRegister() {
        mApplication.unregisterActivityLifecycleCallbacks(mLifecycleCallbacks);
        mPushAgent.disable(new IUmengCallback() {
            @Override
            public void onSuccess() {
                XPush.transmitCommandResult(mApplication, TYPE_UNREGISTER, RESULT_OK, null, null, null);
                XPushManager.get().notifyConnectStatusChanged(DISCONNECT);
            }

            @Override
            public void onFailure(String s, String s1) {
                XPush.transmitCommandResult(mApplication, TYPE_UNREGISTER, RESULT_ERROR, null, s, s1);
            }
        });
    }

    /**
     * 绑定别名【别名是唯一的】
     *
     * @param alias 别名
     */
    @Override
    public void bindAlias(final String alias) {
        mPushAgent.setAlias(alias, XPUSH_ALIAS, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean isSuccess, String message) {
                XPush.transmitCommandResult(mApplication, TYPE_BIND_ALIAS, isSuccess ? RESULT_OK : RESULT_ERROR, alias, null, message);
            }
        });
    }

    /**
     * 解绑别名
     *
     * @param alias 别名
     */
    @Override
    public void unBindAlias(final String alias) {
        mPushAgent.deleteAlias(alias, XPUSH_ALIAS, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean isSuccess, String message) {
                XPush.transmitCommandResult(mApplication, TYPE_UNBIND_ALIAS, isSuccess ? RESULT_OK : RESULT_ERROR, alias, null, message);
            }
        });
    }

    /**
     * 获取别名
     */
    @Override
    public void getAlias() {

    }

    /**
     * 增加标签
     *
     * @param tag 标签
     */
    @Override
    public void addTags(final String... tag) {
        mPushAgent.getTagManager().addTags(new TagManager.TCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
                //isSuccess表示操作是否成功
                XPush.transmitCommandResult(mApplication, TYPE_ADD_TAG, isSuccess ? RESULT_OK : RESULT_ERROR, PushUtils.array2String(tag), null, result == null ? "" : result.errors);
            }
        }, tag);
    }

    /**
     * 删除标签
     *
     * @param tag 标签
     */
    @Override
    public void deleteTags(final String... tag) {
        mPushAgent.getTagManager().deleteTags(new TagManager.TCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
                //isSuccess表示操作是否成功
                XPush.transmitCommandResult(mApplication, TYPE_DEL_TAG, isSuccess ? RESULT_OK : RESULT_ERROR, PushUtils.array2String(tag), null, result == null ? "" : result.errors);
            }
        }, tag);
    }

    /**
     * 获取标签
     */
    @Override
    public void getTags() {

    }

    /**
     * @return 获取推送令牌
     */
    @Override
    public String getPushToken() {
        return mPushAgent.getRegistrationId();
    }

    /**
     * @return 获取平台码
     */
    @Override
    public int getPlatformCode() {
        return UMENG_PUSH_PLATFORM_CODE;
    }

    /**
     * @return 获取平台名
     */
    @Override
    public String getPlatformName() {
        return UMENG_PUSH_PLATFORM_NAME;
    }


}
