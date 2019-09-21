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

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.logs.PushLog;
import com.xuexiang.xpush.util.PushUtils;

import org.json.JSONObject;

import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_ADD_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_DEL_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_REGISTER;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_UNREGISTER;
import static com.xuexiang.xpush.core.annotation.ResultCode.RESULT_ERROR;
import static com.xuexiang.xpush.core.annotation.ResultCode.RESULT_OK;

/**
 * 信鸽消息接收服务
 *
 * @author xuexiang
 * @since 2019-09-20 9:20
 */
public class XGMessageReceiver extends XGPushBaseReceiver {
    private static final String TAG = "XGPush-";

    //注册的回调
    @Override
    public void onRegisterResult(Context context, int errorCode, XGPushRegisterResult message) {
        if (context == null || message == null) {
            return;
        }
        PushLog.d(TAG + "[onRegisterResult]  " + getOperationResult(errorCode) + ", XGPushRegisterResult:" + message);
        String errorMessage = errorCode == XGPushBaseReceiver.SUCCESS ? null : errorCodeHandle(errorCode);
        XPush.transmitCommandResult(context, TYPE_REGISTER,
                errorCode == XGPushBaseReceiver.SUCCESS ? RESULT_OK : RESULT_ERROR,
                message.getToken(), message.toString(), errorMessage);
    }

    //反注册的回调
    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        if (context == null) {
            return;
        }
        PushLog.d(TAG + "[onUnregisterResult]  " + getOperationResult(errorCode));
        String errorMessage = errorCode == XGPushBaseReceiver.SUCCESS ? null : errorCodeHandle(errorCode);
        XPush.transmitCommandResult(context, TYPE_UNREGISTER,
                errorCode == XGPushBaseReceiver.SUCCESS ? RESULT_OK : RESULT_ERROR,
                null, null, errorMessage);
    }

    // 消息透传的回调
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        if (context == null || message == null) {
            return;
        }
        PushLog.d(TAG + "[消息透传]:" + message);
        try {
            XPush.transmitMessage(context, message.getContent(), message.getCustomContent(), PushUtils.toMap(new JSONObject(message.getCustomContent())));
        } catch (Exception e) {
            e.printStackTrace();
            XPush.transmitMessage(context, message.getContent(), message.getCustomContent(), null);
        }
    }

    // 通知展示
    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult showedResult) {
        if (context == null || showedResult == null) {
            return;
        }
        PushLog.d(TAG + "[通知展示]:" + showedResult);
        XPush.transmitNotification(context, showedResult.getNotifactionId(), showedResult.getTitle(), showedResult.getContent(), showedResult.getCustomContent(), null);
    }

    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击。此处不能做点击消息跳转，详细方法请参照官网的Android常见问题文档
    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult message) {
        if (context == null || message == null) {
            return;
        }
        PushLog.d(TAG + "[通知点击]:" + message);
        try {
            XPush.transmitNotificationClick(context, (int) message.getActionType(), message.getTitle(), message.getContent(), message.getCustomContent(), PushUtils.toMap(new JSONObject(message.getCustomContent())));
        } catch (Exception e) {
            e.printStackTrace();
            XPush.transmitNotificationClick(context, (int) message.getActionType(), message.getTitle(), message.getContent(), message.getCustomContent(), null);
        }
    }

    //设置tag的回调
    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        PushLog.d(TAG + "[onSetTagResult]  " + getOperationResult(errorCode) + ", tagName:" + tagName);
        String errorMessage = errorCode == XGPushBaseReceiver.SUCCESS ? null : errorCodeHandle(errorCode);
        XPush.transmitCommandResult(context, TYPE_ADD_TAG,
                errorCode == XGPushBaseReceiver.SUCCESS ? RESULT_OK : RESULT_ERROR,
                tagName, null, errorMessage);
    }

    //删除tag的回调
    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }

        PushLog.d(TAG + "[onDeleteTagResult]  " + getOperationResult(errorCode) + ", tagName:" + tagName);
        String errorMessage = errorCode == XGPushBaseReceiver.SUCCESS ? null : errorCodeHandle(errorCode);
        XPush.transmitCommandResult(context, TYPE_DEL_TAG,
                errorCode == XGPushBaseReceiver.SUCCESS ? RESULT_OK : RESULT_ERROR,
                tagName, null, errorMessage);
    }

    public static String getOperationResult(int errorCode) {
        return "Result:" + errorCode + "[" + errorCodeHandle(errorCode) + "]";
    }

    /**
     * 转化errorCode
     *
     * @param errCode
     * @return
     */
    private static String errorCodeHandle(int errCode) {
        switch (errCode) {
            case -1:
                return "参数错误,请检查各字段是否合法!";
            case -2:
                return "请求时间戳不在有效期内!";
            case -3:
                return "sign校验无效，检查access id和secret key（注意不是access key）!";
            case XGPushBaseReceiver.SUCCESS:
                return "操作成功";
            case 7:
                return "别名/账号绑定的终端数满了（10个）!";
            case 14:
                return "收到非法token，例如ios终端没能拿到正确的token!";
            case 15:
                return "信鸽逻辑服务器繁忙!";
            case 19:
                return "操作时序错误\r\n例如:进行tag操作前未获取到deviceToken 没有获取到deviceToken的原因: 1.没有注册信鸽或者苹果推送。 2.provisioning profile制作不正确。!";
            case 20:
                return "鉴权错误，可能是由于Access ID和Access Key不匹配,请检查1.AndroidMenifest.xml是否配置正确;2.云端环境是否是正式环境!";
            case 40:
                return "推送的token没有在信鸽中注册,请重新执行注册流程,或增加注册失败重试逻辑!并检查反注册逻辑是否使用不当?";
            case 48:
                return "推送的账号没有在信鸽中注册,请重新执行注册流程,或增加注册失败重试逻辑!";
            case 63:
                return "标签系统忙,请重新执行设置标签流程,或增加设置标签失败重试逻辑!";
            case 71:
                return "APNS服务器繁忙!";
            case 73:
                return "消息字符数超限,通知不超过75个字符,消息不超过4096字节!";
            case 76:
                return "请求过于频繁，请稍后再试!";
            case 100:
                return "APNS证书错误。请重新提交正确的证书!";
            case 2:
                return "参数错误，例如绑定了单字符的别名，或是ios的token长度不对，应为64个字符!";
            case 10000:
                return "起始错误!";
            case 10001:
                return "操作类型错误码，例如参数错误时将会发生该错误!";
            case 10002:
                return "正在执行注册操作时，又有一个注册操作到来，则回调此错误码!";
            case 10003:
                return "权限出错,请检查AndroidMenifest.xml权限是否配置齐全，详见http://developer.xg.qq.com/index.php/Android_SDK%E5%BF%AB%E9%80%9F%E6%8C%87%E5%8D%97!";
            case 10004:
                return "so出错,请检查工程是否导入libtpnsSecurity.so和libtpnsWatchdog.so,然后重新安装!";
            case 10100:
                return "当前网络不可用,请检查wifi或移动网络是否打开!";
            case 10101:
                return "创建链路失败,请重启应用!";
            case 10102:
                return "请求处理过程中， 链路被主动关闭!";
            case 10103:
                return "请求处理过程中，服务器关闭链接!";
            case 10104:
                return "请求处理过程中，客户端产生异常!";
            case 10105:
                return "请求处理过程中，发送或接收报文超时!";
            case 10106:
                return "请求处理过程中， 等待发送请求超时!";
            case 10107:
                return "请求处理过程中， 等待接收请求超时!";
            case 10108:
                return "服务器返回异常报文!";
            case 10109:
                return "未知异常，请在QQ群中直接联系管理员，或前往论坛发帖留言!";
            case 10110:
                return "创建链路的handler为null!";
            default:
                return "未知错误";
        }
    }
}
