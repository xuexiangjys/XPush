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

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.logs.PushLog;
import com.xuexiang.xpush.util.PushUtils;

import java.util.List;

import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_ADD_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_BIND_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_DEL_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_REGISTER;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_UNBIND_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_UNREGISTER;
import static com.xuexiang.xpush.core.annotation.ResultCode.RESULT_ERROR;
import static com.xuexiang.xpush.core.annotation.ResultCode.RESULT_OK;
import static com.xuexiang.xpush.xiaomi.XiaoMiPushClient.MIPUSH_PLATFORM_NAME;

/**
 * 小米推送消息接收器
 * 1、PushMessageReceiver 是个抽象类，该类继承了 BroadcastReceiver。
 * 2、需要将自定义的 XiaoMiPushReceiver 注册在 AndroidManifest.xml 文件中.
 * 3、XiaoMiPushReceiver 的 onReceivePassThroughMessage 方法用来接收服务器向客户端发送的透传消息。
 * 4、XiaoMiPushReceiver 的 onNotificationMessageClicked 方法用来接收服务器向客户端发送的通知消息，这个回调方法会在用户手动点击通知后触发。
 * 5、XiaoMiPushReceiver 的 onNotificationMessageArrived 方法用来接收服务器向客户端发送的通知消息，这个回调方法是在通知消息到达客户端时触发。另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数。
 * 6、XiaoMiPushReceiver 的 onCommandResult 方法用来接收客户端向服务器发送命令后的响应结果。
 * 7、XiaoMiPushReceiver 的 onReceiveRegisterResult 方法用来接收客户端向服务器发送注册命令后的响应结果。
 * 8、以上这些方法运行在非 UI 线程中。
 *
 * @author xuexiang
 * @since 2019-08-24 18:23
 */
public class XiaoMiPushReceiver extends PushMessageReceiver {

    private static final String TAG = "MiPush-";


    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        PushLog.d(TAG + "[onReceivePassThroughMessage]:" + miPushMessage);
        XPush.transmitMessage(context, miPushMessage.getContent(), miPushMessage.getDescription(), miPushMessage.getExtra());
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        PushLog.d(TAG + "[onNotificationMessageClicked]:" + miPushMessage);
        XPush.transmitNotificationClick(context, miPushMessage.getNotifyId(), miPushMessage.getTitle(), miPushMessage.getDescription(), miPushMessage.getContent(), miPushMessage.getExtra());
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        PushLog.d(TAG + "[onNotificationMessageArrived]:" + miPushMessage);
        XPush.transmitNotification(context, miPushMessage.getNotifyId(), miPushMessage.getTitle(), miPushMessage.getDescription(), miPushMessage.getContent(), miPushMessage.getExtra());
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage commandMessage) {
        String command = commandMessage.getCommand();
        List<String> arguments = commandMessage.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        String log;

        int commandType = -1;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            commandType = TYPE_REGISTER;
            if (commandMessage.getResultCode() == ErrorCode.SUCCESS) {
                //保存push token
                PushUtils.savePushToken(MIPUSH_PLATFORM_NAME, cmdArg1);
                log = context.getString(R.string.xiaomi_register_success);
            } else {
                log = context.getString(R.string.xiaomi_register_fail, commandMessage.getReason());
            }
        } else if (MiPushClient.COMMAND_UNREGISTER.equals(command)) {
            commandType = TYPE_UNREGISTER;
            if (commandMessage.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.xiaomi_unregister_success);
            } else {
                log = context.getString(R.string.xiaomi_unregister_fail, commandMessage.getReason());
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            commandType = TYPE_BIND_ALIAS;
            if (commandMessage.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.xiaomi_set_alias_success, cmdArg1);
            } else {
                log = context.getString(R.string.xiaomi_set_alias_fail, commandMessage.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            commandType = TYPE_UNBIND_ALIAS;
            if (commandMessage.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.xiaomi_unset_alias_success, cmdArg1);
            } else {
                log = context.getString(R.string.xiaomi_unset_alias_fail, commandMessage.getReason());
            }
        } else if (MiPushClient.COMMAND_SET_ACCOUNT.equals(command)) {
            if (commandMessage.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.xiaomi_set_account_success, cmdArg1);
            } else {
                log = context.getString(R.string.xiaomi_set_account_fail, commandMessage.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSET_ACCOUNT.equals(command)) {
            if (commandMessage.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.xiaomi_unset_account_success, cmdArg1);
            } else {
                log = context.getString(R.string.xiaomi_unset_account_fail, commandMessage.getReason());
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            commandType = TYPE_ADD_TAG;
            if (commandMessage.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.xiaomi_subscribe_topic_success, cmdArg1);
            } else {
                log = context.getString(R.string.xiaomi_subscribe_topic_fail, commandMessage.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            commandType = TYPE_DEL_TAG;
            if (commandMessage.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.xiaomi_unsubscribe_topic_success, cmdArg1);
            } else {
                log = context.getString(R.string.xiaomi_unsubscribe_topic_fail, commandMessage.getReason());
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (commandMessage.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.xiaomi_set_accept_time_success, cmdArg1, cmdArg2);
            } else {
                log = context.getString(R.string.xiaomi_set_accept_time_fail, commandMessage.getReason());
            }
        } else {
            log = commandMessage.getReason();
        }

        PushLog.d(TAG + "[onCommandResult] is called. " + commandMessage.toString() + " reason:" + log);
        if (commandType != -1) {
            XPush.transmitCommandResult(context, commandType,
                    commandMessage.getResultCode() == ErrorCode.SUCCESS ? RESULT_OK : RESULT_ERROR,
                    cmdArg1, null, commandMessage.getReason());
        }
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage commandMessage) {
        String command = commandMessage.getCommand();
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (commandMessage.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.xiaomi_register_success);
            } else {
                log = context.getString(R.string.xiaomi_register_fail, commandMessage.getReason());
            }
        } else if (MiPushClient.COMMAND_UNREGISTER.equals(command)) {
            if (commandMessage.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.xiaomi_unregister_success);
            } else {
                log = context.getString(R.string.xiaomi_unregister_fail, commandMessage.getReason());
            }
        } else {
            log = commandMessage.getReason();
        }
        PushLog.d(TAG + "[onReceiveRegisterResult] is called. " + " reason:" + log);
        //事件重复了，这里就不发了
//        if (commandType != -1) {
//            XPush.transmitCommandResult(context, commandType,
//                    commandMessage.getResultCode() == ErrorCode.SUCCESS ? RESULT_OK : RESULT_ERROR,
//                    cmdArg1, null, commandMessage.getReason());
//        }
    }

}
