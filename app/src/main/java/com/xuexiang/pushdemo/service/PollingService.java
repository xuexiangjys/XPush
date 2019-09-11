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

package com.xuexiang.pushdemo.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xuexiang.xutil.net.JsonUtil;

/**
 * 轮询处理事件的服务
 *
 * @author xuexiang
 * @since 2019-09-10 23:28
 */
public class PollingService extends IntentService {

    private static final String SERVICE_NAME = "PollingService";

    /**
     * 轮询任务
     */
    public static final String KEY_POLLING_TASK = "key_polling_task";


    /**
     * 执行定时轮询服务
     *
     * @param context
     * @param task 轮询事务
     */
    public static void start(Context context, PollingTask task) {
        Intent intent = new Intent(context, PollingService.class);
        //不可以使用Parcelable、Serializable，因为AlarmManager在发PendingIntent可能会丢失数据
        intent.putExtra(KEY_POLLING_TASK, task.toJson());
        context.startService(intent);
    }

    /**
     * 执行次数
     */
    private static int mCount;

    public PollingService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //在子线程中
        if (intent == null) {
            return;
        }
        mCount++;
        PollingTask pollingTask = JsonUtil.fromJson(intent.getStringExtra(KEY_POLLING_TASK), PollingTask.class);
        Log.e("xuexiang", "第" + mCount + "次定时处理事务:" + pollingTask);
    }
}
