package com.xuexiang.keeplive.service;

import android.app.Notification;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.xuexiang.keeplive.KeepLive;
import com.xuexiang.keeplive.receiver.NotificationClickReceiver;
import com.xuexiang.keeplive.utils.NotificationUtils;
import com.xuexiang.keeplive.utils.ServiceUtils;

import static com.xuexiang.keeplive.service.LocalService.KEY_LOCAL_SERVICE_NAME;
import static com.xuexiang.keeplive.utils.NotificationUtils.KEY_NOTIFICATION_ID;

/**
 * 定时器保活
 * 安卓5.0及以上
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public final class JobHandlerService extends JobService {

    private static final int JOB_SERVICE_ID = 11000;

    /**
     * 停止保活服务
     *
     * @param context
     */
    public static void stop(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.cancel(JOB_SERVICE_ID);
        }
        context.stopService(new Intent(context, JobHandlerService.class));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startService(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(JOB_SERVICE_ID, new ComponentName(getPackageName(), JobHandlerService.class.getName()))
                        .setPersisted(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //执行的最小延迟时间
                builder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
                //执行的最长延时时间
                builder.setOverrideDeadline(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
                //线性重试方案
                builder.setBackoffCriteria(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS, JobInfo.BACKOFF_POLICY_LINEAR);
            } else {
                builder.setPeriodic(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
                builder.setRequiresDeviceIdle(true);
            }
            //需要网络
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            // 当插入充电器，执行该任务
            builder.setRequiresCharging(true);
            jobScheduler.schedule(builder.build());
        }
        return START_STICKY;
    }

    private void startService(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (KeepLive.sForegroundNotification != null) {
                Intent intent2 = new Intent(getApplicationContext(), NotificationClickReceiver.class);
                intent2.setAction(NotificationClickReceiver.ACTION_CLICK_NOTIFICATION);
                Notification notification = NotificationUtils.createNotification(context, KeepLive.sForegroundNotification.getTitle(), KeepLive.sForegroundNotification.getDescription(), KeepLive.sForegroundNotification.getIconRes(), intent2);
                startForeground(KEY_NOTIFICATION_ID, notification);
            }
        }

        KeepLive.startDoubleProcessService(context);
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        if (!ServiceUtils.isServiceRunning(getApplicationContext(), KEY_LOCAL_SERVICE_NAME) || !ServiceUtils.isRunningTaskExist(getApplicationContext(), getPackageName() + ":remote")) {
            startService(this);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (!ServiceUtils.isServiceRunning(getApplicationContext(), KEY_LOCAL_SERVICE_NAME) || !ServiceUtils.isRunningTaskExist(getApplicationContext(), getPackageName() + ":remote")) {
            startService(this);
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        KeepLive.stopDoubleProcessService(this);
    }
}
