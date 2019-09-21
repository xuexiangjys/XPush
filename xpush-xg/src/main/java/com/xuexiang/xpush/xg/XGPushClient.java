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

package com.xuexiang.xpush.xg;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Keep;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.core.IPushClient;
import com.xuexiang.xpush.core.XPushManager;
import com.xuexiang.xpush.logs.PushLog;
import com.xuexiang.xpush.util.PushUtils;

import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_BIND_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_UNBIND_ALIAS;
import static com.xuexiang.xpush.core.annotation.ConnectStatus.CONNECTED;
import static com.xuexiang.xpush.core.annotation.ConnectStatus.DISCONNECT;
import static com.xuexiang.xpush.core.annotation.ResultCode.RESULT_ERROR;
import static com.xuexiang.xpush.core.annotation.ResultCode.RESULT_OK;

/**
 * 信鸽推送客户端
 * 1.别名和标签都无法获取
 * 2.推送连接状态不支持
 *
 * @author xuexiang
 * @since 2019-09-20 9:22
 */
public class XGPushClient implements IPushClient {

    public static final String XGPUSH_PLATFORM_NAME = "XGPush";
    public static final int XGPUSH_PLATFORM_CODE = 1004;

    public static final String XGPUSH_ACCESS_ID = "XGPUSH_ACCESS_ID";
    public static final String XGPUSH_ACCESS_KEY = "XGPUSH_ACCESS_KEY";

    private Context mContext;

    /**
     * 初始化【必须】
     *
     * @param context
     */
    @Override
    public void init(Context context) {
        mContext = context.getApplicationContext();
        XGPushConfig.enableDebug(mContext, PushLog.isDebug());

        //读取信鸽对应的ACCESS_ID和ACCESS_KEY
        try {
            Bundle metaData = mContext.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            int accessId = metaData.getInt(XGPUSH_ACCESS_ID);
            String accessKey = metaData.getString(XGPUSH_ACCESS_KEY);
            XGPushConfig.setAccessId(mContext, accessId);
            XGPushConfig.setAccessKey(mContext, accessKey);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            PushLog.e("can't find XGPUSH_ACCESS_ID or XGPUSH_ACCESS_KEY in AndroidManifest.xml");
        } catch (NullPointerException e) {
            e.printStackTrace();
            PushLog.e("can't find XGPUSH_ACCESS_ID or XGPUSH_ACCESS_KEY in AndroidManifest.xml");
        }
    }

    /**
     * 注册推送【必须】
     */
    @Override
    public void register() {
        XGPushManager.registerPush(mContext, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                XPushManager.get().notifyConnectStatusChanged(CONNECTED);
            }
            @Override
            public void onFail(Object o, int i, String s) {
                XPushManager.get().notifyConnectStatusChanged(DISCONNECT);
            }
        });
    }

    /**
     * 注销推送【必须】
     */
    @Override
    public void unRegister() {
        XGPushManager.unregisterPush(mContext, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                XPushManager.get().notifyConnectStatusChanged(DISCONNECT);
            }
            @Override
            public void onFail(Object o, int i, String s) {

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
        XGPushManager.bindAccount(mContext, alias, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                XPush.transmitCommandResult(mContext, TYPE_BIND_ALIAS, RESULT_OK, alias, null, null);
            }

            @Override
            public void onFail(Object o, int i, String s) {
                XPush.transmitCommandResult(mContext, TYPE_BIND_ALIAS, RESULT_ERROR, alias, null, s);
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
        XGPushManager.delAccount(mContext, alias, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                XPush.transmitCommandResult(mContext, TYPE_UNBIND_ALIAS, RESULT_OK, alias, null, null);
            }

            @Override
            public void onFail(Object o, int i, String s) {
                XPush.transmitCommandResult(mContext, TYPE_UNBIND_ALIAS, RESULT_ERROR, alias, null, s);
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
    public void addTags(String... tag) {
        XGPushManager.addTags(mContext, "TYPE_ADD_TAG", PushUtils.array2Set(tag));
    }

    /**
     * 删除标签
     *
     * @param tag 标签
     */
    @Override
    public void deleteTags(String... tag) {
        XGPushManager.deleteTags(mContext, "TYPE_DEL_TAG", PushUtils.array2Set(tag));
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
        return XGPushConfig.getToken(mContext);
    }

    /**
     * 注意千万不要重复【必须】
     *
     * @return 获取平台码
     */
    @Override
    public int getPlatformCode() {
        return XGPUSH_PLATFORM_CODE;
    }

    /**
     * 注意千万不要重复【必须】
     *
     * @return 获取平台名
     */
    @Override
    public String getPlatformName() {
        return XGPUSH_PLATFORM_NAME;
    }
}
