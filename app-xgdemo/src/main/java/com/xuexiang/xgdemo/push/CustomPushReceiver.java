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

package com.xuexiang.xgdemo.push;

import android.content.Context;
import android.content.Intent;

import com.xuexiang.xgdemo.activity.TestActivity;
import com.xuexiang.xpush.core.receiver.impl.XPushReceiver;
import com.xuexiang.xpush.entity.XPushCommand;
import com.xuexiang.xpush.entity.XPushMsg;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.app.IntentUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import static com.xuexiang.xgdemo.activity.TestActivity.KEY_PARAM_INT;
import static com.xuexiang.xgdemo.activity.TestActivity.KEY_PARAM_STRING;

/**
 * @author xuexiang
 * @since 2019-08-16 17:50
 */
public class CustomPushReceiver extends XPushReceiver {

    @Override
    public void onNotificationClick(Context context, XPushMsg msg) {
        super.onNotificationClick(context, msg);
        //打开自定义的Activity
        Intent intent = IntentUtils.getIntent(context, TestActivity.class, null, true);
        intent.putExtra(KEY_PARAM_STRING, msg.getContent());
        intent.putExtra(KEY_PARAM_INT, msg.getId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityUtils.startActivity(intent);
    }

    @Override
    public void onCommandResult(Context context, XPushCommand command) {
        super.onCommandResult(context, command);
        ToastUtils.toast(command.getDescription());
    }

}
