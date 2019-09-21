package com.xuexiang.keeplive.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.xuexiang.keeplive.KeepLive;
import com.xuexiang.keeplive.utils.NotificationUtils;
import com.xuexiang.keeplive.receiver.NotificationClickReceiver;

import static com.xuexiang.keeplive.utils.NotificationUtils.KEY_NOTIFICATION_ID;

/**
 * 通过相同的notification_id来隐藏前台服务通知
 *
 * @author xuexiang
 * @since 2019-08-27 9:33
 */
public class HideForegroundService extends Service {

    private Handler mHandler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();
        if (mHandler == null){
            mHandler = new Handler();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopForeground(true);
                stopSelf();
            }
        }, 2000);
        return START_NOT_STICKY;
    }


    private void startForeground() {
        if (KeepLive.sForegroundNotification != null) {
            Intent intent = new Intent(getApplicationContext(), NotificationClickReceiver.class);
            intent.setAction(NotificationClickReceiver.ACTION_CLICK_NOTIFICATION);
            Notification notification = NotificationUtils.createNotification(this, KeepLive.sForegroundNotification.getTitle(), KeepLive.sForegroundNotification.getDescription(), KeepLive.sForegroundNotification.getIconRes(), intent);
            startForeground(KEY_NOTIFICATION_ID, notification);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
