package com.xuexiang.keeplive.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;

import com.xuexiang.keeplive.receiver.NotificationClickReceiver;
import com.xuexiang.keeplive.utils.NotificationUtils;
import com.xuexiang.keeplive.utils.ServiceUtils;

import static com.xuexiang.keeplive.receiver.DeviceStatusReceiver.KEEP_ACTION_OPEN_MUSIC;
import static com.xuexiang.keeplive.receiver.DeviceStatusReceiver.KEEP_ACTION_CLOSE_MUSIC;
import static com.xuexiang.keeplive.utils.NotificationUtils.KEY_NOTIFICATION_ID;

/**
 * 守护进程服务（双进程守护之守护进程)
 *
 * @author xuexiang
 * @since 2019-08-14 16:09
 */
public final class RemoteService extends Service {
    private GuardBinder mBinder;
    private boolean mIsBoundLocalService;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mBinder == null) {
            mBinder = new GuardBinder();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            mIsBoundLocalService = this.bindService(new Intent(RemoteService.this, LocalService.class), mConnection, Context.BIND_ABOVE_CLIENT);
        } catch (Exception e) {
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mConnection != null) {
            try {
                if (mIsBoundLocalService) {
                    unbindService(mConnection);
                }
            } catch (Exception e) {
            }
        }
    }

    private final class GuardBinder extends GuardAidl.Stub {

        @Override
        public void wakeUp(String title, String description, int iconRes) throws RemoteException {
            Intent intent = new Intent(getApplicationContext(), NotificationClickReceiver.class);
            intent.setAction(NotificationClickReceiver.ACTION_CLICK_NOTIFICATION);
            Notification notification = NotificationUtils.createNotification(RemoteService.this, title, description, iconRes, intent);
            RemoteService.this.startForeground(KEY_NOTIFICATION_ID, notification);
        }

    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (ServiceUtils.isRunningTaskExist(getApplicationContext(), getPackageName() + ":remote")) {
                Intent localService = new Intent(RemoteService.this, LocalService.class);
                RemoteService.this.startService(localService);
                mIsBoundLocalService = RemoteService.this.bindService(new Intent(RemoteService.this, LocalService.class), mConnection, Context.BIND_ABOVE_CLIENT);
            }
            PowerManager pm = (PowerManager) RemoteService.this.getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm != null && pm.isScreenOn();
            if (isScreenOn) {
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(KEEP_ACTION_CLOSE_MUSIC));
            } else {
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(KEEP_ACTION_OPEN_MUSIC));
            }
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }
    };

}
