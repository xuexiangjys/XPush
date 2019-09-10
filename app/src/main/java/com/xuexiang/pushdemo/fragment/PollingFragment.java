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

package com.xuexiang.pushdemo.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.xuexiang.pushdemo.service.JobSchedulerService;
import com.xuexiang.pushdemo.service.PollingService;
import com.xuexiang.pushdemo.service.PollingTask;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageSimpleListFragment;

import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.JOB_SCHEDULER_SERVICE;
import static com.xuexiang.pushdemo.service.PollingService.KEY_POLLING_TASK;

/**
 * @author xuexiang
 * @since 2019-09-11 00:04
 */
@Page(name = "定时任务")
public class PollingFragment extends XPageSimpleListFragment {

    private static boolean sIsPollingStart;

    private static final int POLLING_TASK_ID = 1000;

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("执行定时任务");
        lists.add("停止定时任务");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                startPollingTask(getContext(), 5 * 1000);
                break;
            case 1:
                stopPollingTask(getContext());
                break;
            default:
                break;
        }
    }


    /**
     * 执行定时任务
     *
     * @param context
     * @param intervalMillis 间隔（毫秒）
     */
    private void startPollingTask(Context context, long intervalMillis) {
        if (sIsPollingStart) {
            return;
        }

        Log.e("xuexiang", "开始执行定时任务");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobInfo.Builder builder = new JobInfo.Builder(POLLING_TASK_ID, new ComponentName(context, JobSchedulerService.class));
            builder.setPeriodic(intervalMillis);
            //Android 7.0+ 增加了一项针对 JobScheduler 的新限制，最小间隔只能是下面设定的数字
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setPeriodic(JobInfo.getMinPeriodMillis(), JobInfo.getMinFlexMillis());
            }
            builder.setPersisted(true);
            JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
            if (scheduler != null) {
                scheduler.schedule(builder.build());
            }
        } else {
            //Android 4.4- 使用 AlarmManager
            AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            if (manager != null) {
                PendingIntent pendingIntent = getPendingIntent(context);
                //注意，这里设置的是非立即执行
                manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + intervalMillis, intervalMillis, pendingIntent);
            }
        }

        sIsPollingStart = true;
    }

    /**
     * 获取轮询意图
     * @param context
     * @return
     */
    private PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, PollingService.class);
        intent.putExtra(KEY_POLLING_TASK, new PollingTask(PollingTask.TYPE_ALARM_MANAGER, "这是AlarmManager定时执行的事务"));
        return PendingIntent.getService(context, POLLING_TASK_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    /**
     * 停止定时任务
     */
    private void stopPollingTask(Context context) {
        if (!sIsPollingStart) {
            return;
        }

        Log.e("xuexiang", "停止执行定时任务");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
            if (scheduler != null) {
                scheduler.cancel(POLLING_TASK_ID);
            }
        } else {
            AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            PendingIntent pendingIntent = getPendingIntent(context);
            if (manager != null && pendingIntent != null) {
                manager.cancel(pendingIntent);
            }
        }

        sIsPollingStart = false;
    }

}
