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

package com.xuexiang.xpush;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.xuexiang.xpush.core.IPushClient;
import com.xuexiang.xpush.core.IPushInitCallback;
import com.xuexiang.xpush.core.annotation.CommandType;
import com.xuexiang.xpush.core.annotation.ConnectStatus;
import com.xuexiang.xpush.core.annotation.PushAction;
import com.xuexiang.xpush.core.annotation.ResultCode;
import com.xuexiang.xpush.core.dispatcher.IPushDispatcher;
import com.xuexiang.xpush.core.dispatcher.impl.DefaultPushDispatcherImpl;
import com.xuexiang.xpush.entity.XPushCommand;
import com.xuexiang.xpush.entity.XPushMsg;
import com.xuexiang.xpush.logs.PushLog;
import com.xuexiang.xpush.util.PushUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * XPush消息推送API管理
 *
 * @author xuexiang
 * @since 2019-08-16 9:44
 */
public final class _XPush implements IPushClient{

    private static volatile _XPush sInstance = null;
    /**
     * the meta-data header
     */
    private static final String META_DATA_PUSH_HEADER = "XPush_";
    /**
     * the meta_data split symbol
     */
    private static final String METE_DATA_SPLIT_SYMBOL = "_";

    private Application mApplication;

    /**
     * 推送客户端
     */
    private IPushClient mIPushClient;
    /**
     * 消息推送事件转发器
     */
    private IPushDispatcher mIPushDispatcher;

    private _XPush() {
        mIPushDispatcher = new DefaultPushDispatcherImpl();
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static _XPush get() {
        if (sInstance == null) {
            synchronized (_XPush.class) {
                if (sInstance == null) {
                    sInstance = new _XPush();
                }
            }
        }
        return sInstance;
    }

    //=====================================================================//

    /**
     * 设置消息推送的事件转发器
     *
     * @param iPushDispatcher 消息推送的事件转发器
     * @return
     */
    public _XPush setIPushDispatcher(@NonNull IPushDispatcher iPushDispatcher) {
        mIPushDispatcher = iPushDispatcher;
        return this;
    }

    /**
     * 转发命令执行结果
     *
     * @param context
     * @param commandType 命令类型
     * @param resultCode  结果码
     * @param content     内容
     * @param extraMsg    额外信息
     * @param error       错误信息
     * @see CommandType#TYPE_ADD_TAG
     * @see CommandType#TYPE_DEL_TAG
     * @see CommandType#TYPE_GET_TAG
     * @see CommandType#TYPE_AND_OR_DEL_TAG
     * @see CommandType#TYPE_REGISTER
     * @see CommandType#TYPE_UNREGISTER
     * @see CommandType#TYPE_BIND_ALIAS
     * @see CommandType#TYPE_UNBIND_ALIAS
     * @see CommandType#TYPE_GET_ALIAS
     * @see ResultCode#RESULT_ERROR
     * @see ResultCode#RESULT_OK
     */
    public void transmitCommandResult(Context context, @CommandType int commandType, @ResultCode int resultCode, String content, String extraMsg, String error) {
        transmit(context, PushAction.RECEIVE_COMMAND_RESULT, new XPushCommand(commandType, resultCode, content, extraMsg, error));
    }

    /**
     * 转发连接状态发生改变
     *
     * @param context
     * @param connectStatus 推送连接状态
     */
    public void transmitConnectStatusChanged(Context context, @ConnectStatus int connectStatus) {
        transmit(context, PushAction.RECEIVE_CONNECT_STATUS_CHANGED, new XPushMsg(connectStatus));
    }

    /**
     * 转发通知信息
     *
     * @param context
     * @param notifyId 通知ID
     * @param title    通知标题
     * @param content  通知内容
     * @param extraMsg 额外消息
     */
    public void transmitNotification(Context context, int notifyId, String title, String content, String extraMsg, Map<String, String> keyValue) {
        transmit(context, PushAction.RECEIVE_NOTIFICATION, new XPushMsg(notifyId, title, content, null, extraMsg, keyValue));
    }

    /**
     * 转发通知点击事件
     *
     * @param context
     * @param notifyId 通知ID
     * @param title    通知标题
     * @param content  通知内容
     * @param extraMsg 额外消息
     */
    public void transmitNotificationClick(Context context, int notifyId, String title, String content, String extraMsg, Map<String, String> keyValue) {
        transmit(context, PushAction.RECEIVE_NOTIFICATION_CLICK, new XPushMsg(notifyId, title, content, null, extraMsg, keyValue));
    }

    /**
     * 转发自定义消息
     *
     * @param context
     * @param msg      自定义消息内容
     * @param extraMsg 拓展消息
     */
    public void transmitMessage(Context context, String msg, String extraMsg, Map<String, String> keyValue) {
        transmit(context, PushAction.RECEIVE_MESSAGE, new XPushMsg(0, null, null, msg, extraMsg, keyValue));
    }

    /**
     * 转发自定义消息
     *
     * @param context
     * @param pushMsg 推送消息
     */
    public void transmitMessage(Context context, XPushMsg pushMsg) {
        transmit(context, PushAction.RECEIVE_MESSAGE, pushMsg);
    }

    /**
     * 转移消息
     *
     * @param context
     * @param action  广播动作
     * @param data    消息数据
     */
    private void transmit(Context context, @PushAction String action, @NonNull Parcelable data) {
        if (mIPushDispatcher != null) {
            PushLog.d("[PushDispatcher] action:" + action + ", data:" + data);
            mIPushDispatcher.transmit(context, action, data);
        }
    }

    //===================初始化======================//

    /**
     * 初始化[不注册推送客户端]
     *
     * @param application
     */
    public void init(@NonNull Application application) {
        mApplication = application;
    }

    /**
     * 设置推送客户端
     *
     * @param pushClient
     */
    public void setIPushClient(@NonNull IPushClient pushClient) {
        mIPushClient = pushClient;
        mIPushClient.init(getContext());
    }

    /**
     * 初始化[自动注册]
     *
     * @param application
     * @param registerCallback 注册回调
     */
    public void init(@NonNull Application application, @NonNull IPushInitCallback registerCallback) {
        mApplication = application;
        //choose custom push platform
        mIPushClient = choosePushPlatform(findAllSupportPushPlatform(mApplication), registerCallback);
        if (mIPushClient != null) {
            mIPushClient.init(application);
        } else {
            throw new IllegalStateException("onInitPush must at least one of them returns to true");
        }
    }

    /**
     * 初始化[手动注册]
     *
     * @param application
     * @param pushClient  推送客户端
     */
    public void init(@NonNull Application application, @NonNull IPushClient pushClient) {
        mApplication = application;
        mIPushClient = pushClient;
        mIPushClient.init(application);
    }


    /**
     * 在AndroidManifest.xml中查找所有支持的平台
     */
    private LinkedHashMap<String, String> findAllSupportPushPlatform(Application application) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        try {
            //find all support push platform
            Bundle metaData = application.getPackageManager().getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA).metaData;
            if (metaData != null) {
                Set<String> allKeys = metaData.keySet();
                if (allKeys != null && !allKeys.isEmpty()) {
                    for (String key : allKeys) {
                        if (key.startsWith(META_DATA_PUSH_HEADER)) {
                            map.put(key, metaData.getString(key));
                        }
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 选择推送平台
     *
     * @param supportPushPlatformMap 支持的推送平台
     * @param registerCallback       注册回调
     */
    private IPushClient choosePushPlatform(@NonNull LinkedHashMap<String, String> supportPushPlatformMap, @NonNull IPushInitCallback registerCallback) {
        if (supportPushPlatformMap.isEmpty()) {
            throw new IllegalArgumentException("have none push platform,check AndroidManifest.xml is have meta-data name is start with " + META_DATA_PUSH_HEADER);
        }

        for (Map.Entry<String, String> next : supportPushPlatformMap.entrySet()) {
            String metaPlatformName = next.getKey();
            String metaPlatformClassName = next.getValue();
            StringBuilder stringBuilder = new StringBuilder(metaPlatformName).delete(0, META_DATA_PUSH_HEADER.length());
            int len = stringBuilder.length();
            int lastIndexSymbol = stringBuilder.lastIndexOf(METE_DATA_SPLIT_SYMBOL);
            int platformCode = Integer.parseInt(stringBuilder.substring(lastIndexSymbol + 1, len));
            String platformName = stringBuilder.substring(0, lastIndexSymbol);
            try {
                Class<?> currentClz = Class.forName(metaPlatformClassName);
                Class<?>[] interfaces = currentClz.getInterfaces();
                List<Class<?>> allInterfaces = Arrays.asList(interfaces);
                if (allInterfaces.contains(IPushClient.class)) {
                    IPushClient iPushClient = (IPushClient) currentClz.newInstance();
                    //接口回调，用于选择推送平台
                    if (registerCallback.onInitPush(platformCode, platformName)) {
                        PushLog.i("current select platform is " + metaPlatformName);
                        return iPushClient;
                    }
                } else {
                    throw new IllegalArgumentException(metaPlatformClassName + "is not implements " + IPushClient.class.getName());
                }
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("can not find class " + metaPlatformClassName);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //=================操作====================//

    private void testInitialize() {
        if (mIPushClient == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 XPush.init() 进行初始化！");
        }
    }

    /**
     * 初始化
     *
     * @param context
     */
    @Override
    public void init(Context context) {
        testInitialize();
        PushLog.i(String.format("%s--%s", getPlatformName(), "init()"));
        mIPushClient.init(context);
    }

    /**
     * 注册
     */
    @Override
    public void register() {
        testInitialize();
        PushLog.i(String.format("%s--%s", getPlatformName(), "register()"));
        mIPushClient.register();
    }

    /**
     * 注销
     */
    @Override
    public void unRegister() {
        testInitialize();
        PushLog.i(String.format("%s--%s", getPlatformName(), "unRegister()"));
        mIPushClient.unRegister();
    }

    /**
     * 绑定别名
     *
     * @param alias 别名
     */
    @Override
    public void bindAlias(String alias) {
        testInitialize();
        PushLog.i(String.format("%s--%s", getPlatformName(), "bindAlias(" + alias + ")"));
        mIPushClient.bindAlias(alias);
    }

    /**
     * 解绑别名
     *
     * @param alias 别名
     */
    @Override
    public void unBindAlias(String alias) {
        testInitialize();
        PushLog.i(String.format("%s--%s", getPlatformName(), "unBindAlias(" + alias + ")"));
        mIPushClient.unBindAlias(alias);
    }

    /**
     * 获取别名
     */
    @Override
    public void getAlias() {
        testInitialize();
        PushLog.i(String.format("%s--%s", getPlatformName(), "getAlias()"));
        mIPushClient.getAlias();
    }

    /**
     * 添加标签
     *
     * @param tag 标签
     */
    @Override
    public void addTags(String... tag) {
        testInitialize();
        PushLog.i(String.format("%s--%s", getPlatformName(), "addTags(" + PushUtils.array2String(tag) + ")"));
        mIPushClient.addTags(tag);
    }

    /**
     * 删除标签
     *
     * @param tag 标签
     */
    @Override
    public void deleteTags(String... tag) {
        testInitialize();
        PushLog.i(String.format("%s--%s", getPlatformName(), "deleteTags(" + PushUtils.array2String(tag) + ")"));
        mIPushClient.deleteTags(tag);
    }

    /**
     * 获取标签
     */
    @Override
    public void getTags() {
        testInitialize();
        PushLog.i(String.format("%s--%s", getPlatformName(), "getTags()"));
        mIPushClient.getTags();
    }

    /**
     * 获取标签
     */
    @Override
    public String getPushToken() {
        testInitialize();
        PushLog.i(String.format("%s--%s", getPlatformName(), "getPushToken()"));
        return mIPushClient.getPushToken();
    }

    /**
     * @return 推送平台码
     */
    @Override
    public int getPlatformCode() {
        testInitialize();
        return mIPushClient.getPlatformCode();
    }

    /**
     * @return 推送平台的名称
     */
    @Override
    public String getPlatformName() {
        testInitialize();
        return mIPushClient.getPlatformName();
    }

    public Context getContext() {
        if (mApplication == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 XPush.init() 进行初始化！");
        }
        return mApplication;
    }


}
