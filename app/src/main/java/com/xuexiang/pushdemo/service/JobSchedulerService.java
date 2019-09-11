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

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * 定时任务[Android5.0以上],不是很精准
 *
 * @author xuexiang
 * @since 2019-09-10 23:25
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {

    /**
     * 开启JobService定时服务
     *
     * @param context
     * @param jobId
     * @param intervalMillis
     */
    public static void start(Context context, int jobId, long intervalMillis) {
        JobInfo.Builder builder = new JobInfo.Builder(jobId, new ComponentName(context, JobSchedulerService.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //执行的最小延迟时间
            builder.setMinimumLatency(intervalMillis);
            //执行的最长延时时间
            builder.setOverrideDeadline(intervalMillis);
            //线性重试方案
            builder.setBackoffCriteria(intervalMillis, JobInfo.BACKOFF_POLICY_LINEAR);
        } else {
            builder.setPeriodic(intervalMillis);
        }
        JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        if (scheduler != null) {
            scheduler.schedule(builder.build());
        }
    }

    /**
     * 停止JobService定时服务
     *
     * @param context
     * @param jobId
     */
    public static void stop(Context context, int jobId) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        if (scheduler != null) {
            scheduler.cancel(jobId);
        }
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        PollingService.start(this, new PollingTask(PollingTask.TYPE_JOB_SCHEDULER, "这是JobScheduler定时执行的事务"));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e("xuexiang", "JobScheduler定时服务停止");
        return false;
    }
}
