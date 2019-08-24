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

package com.xuexiang.xpush.xiaomi;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.xiaomi.mipush.sdk.MiPushClient;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.core.IPushClient;
import com.xuexiang.xpush.logs.PushLog;
import com.xuexiang.xpush.util.PushUtils;

import java.util.List;

import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_GET_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_GET_TAG;
import static com.xuexiang.xpush.core.annotation.ResultCode.RESULT_OK;

/**
 * 小米推送客户端
 * 1.小米的tag操作每次只支持一个
 *
 * @author xuexiang
 * @since 2019-08-24 19:22
 */
public class XiaoMiPushClient implements IPushClient {
    public static final String MIPUSH_PLATFORM_NAME = "MIPush";
    public static final int MIPUSH_PLATFORM_CODE = 1003;

    private static final String MIPUSH_APPID = "MIPUSH_APPID";
    private static final String MIPUSH_APPKEY = "MIPUSH_APPKEY";

    private Context mContext;
    private String mAppId;
    private String mAppKey;

    @Override
    public void init(Context context) {
        mContext = context.getApplicationContext();

        //读取小米对应的AppID和AppKey
        try {
            Bundle metaData = mContext.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            mAppId = metaData.getString(MIPUSH_APPID).trim();
            mAppKey = metaData.getString(MIPUSH_APPKEY).trim();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            PushLog.e("can't find MIPUSH_APPID or MIPUSH_APPKEY in AndroidManifest.xml");
        } catch (NullPointerException e) {
            e.printStackTrace();
            PushLog.e("can't find MIPUSH_APPID or MIPUSH_APPKEY in AndroidManifest.xml");
        }
    }

    @Override
    public void register() {
        if (TextUtils.isEmpty(mAppId) || TextUtils.isEmpty(mAppKey)) {
            throw new IllegalArgumentException("xiaomi push appId or appKey is not init," +
                    "check you AndroidManifest.xml is has MIPUSH_APPID or MIPUSH_APPKEY meta-data flag please");
        }
        MiPushClient.registerPush(mContext, mAppId, mAppKey);
    }

    @Override
    public void unRegister() {
        String token = getPushToken();
        if (!TextUtils.isEmpty(token)) {
            MiPushClient.unregisterPush(mContext);
            PushUtils.deletePushToken(MIPUSH_PLATFORM_NAME);
        }
    }

    @Override
    public void bindAlias(String alias) {
        MiPushClient.setAlias(mContext, alias, null);
    }

    @Override
    public void unBindAlias(String alias) {
        MiPushClient.unsetAlias(mContext, alias, null);
    }

    @Override
    public void getAlias() {
        List<String> alias = MiPushClient.getAllAlias(mContext);
        XPush.transmitCommandResult(mContext, TYPE_GET_ALIAS,
                RESULT_OK,
                PushUtils.collection2String(alias), null, null);

    }

    @Override
    public void addTags(String... tag) {
        MiPushClient.subscribe(mContext, tag[0], null);
    }

    @Override
    public void deleteTags(String... tag) {
        MiPushClient.unsubscribe(mContext, tag[0], null);
    }

    @Override
    public void getTags() {
        List<String> tags = MiPushClient.getAllTopic(mContext);
        XPush.transmitCommandResult(mContext, TYPE_GET_TAG,
                RESULT_OK,
                PushUtils.collection2String(tags), null, null);
    }

    @Override
    public String getPushToken() {
        return PushUtils.getPushToken(MIPUSH_PLATFORM_NAME);
    }

    @Override
    public int getPlatformCode() {
        return MIPUSH_PLATFORM_CODE;
    }

    @Override
    public String getPlatformName() {
        return MIPUSH_PLATFORM_NAME;
    }

}
