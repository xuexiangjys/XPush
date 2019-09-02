package com.xuexiang.keeplive.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;

import com.xuexiang.keeplive.KeepLive;
import com.xuexiang.keeplive.R;
import com.xuexiang.keeplive.receiver.NotificationClickReceiver;
import com.xuexiang.keeplive.receiver.OnePxReceiver;
import com.xuexiang.keeplive.utils.NotificationUtils;
import com.xuexiang.keeplive.utils.ServiceUtils;

import static com.xuexiang.keeplive.receiver.OnePxReceiver.KEEP_ACTION_SCREEN_OFF;
import static com.xuexiang.keeplive.receiver.OnePxReceiver.KEEP_ACTION_SCREEN_ON;
import static com.xuexiang.keeplive.utils.NotificationUtils.KEY_NOTIFICATION_ID;

/**
 * 本地服务（双进程守护之本地进程)
 *
 * @author xuexiang
 * @since 2019-08-18 23:23
 */
public final class LocalService extends Service {
    public static final String KEY_LOCAL_SERVICE_NAME = "com.xuexiang.keeplive.service.LocalService";

    private OnePxReceiver mOnePxReceiver;
    private ScreenStateReceiver mScreenStateReceiver;
    /**
     * 控制暂停
     */
    private boolean mIsPause = true;
    /**
     * 无声音乐保活
     */
    private MediaPlayer mMediaPlayer;
    private GuardBinder mBinder;
    private Handler mHandler;
    private boolean mIsBoundRemoteService;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mBinder == null) {
            mBinder = new GuardBinder();
        }
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mIsPause = pm != null && pm.isScreenOn();
        if (mHandler == null) {
            mHandler = new Handler();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (KeepLive.sUseSilenceMusic) {
            //播放无声音乐
            if (mMediaPlayer == null) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.novioce);
                if (mMediaPlayer != null) {
                    mMediaPlayer.setVolume(0f, 0f);
                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            if (!mIsPause) {
                                if (KeepLive.sRunMode == KeepLive.RunMode.ROGUE) {
                                    play();
                                } else {
                                    if (mHandler != null) {
                                        mHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                play();
                                            }
                                        }, 5000);
                                    }
                                }
                            }
                        }
                    });
                    play();
                }
            }
        }
        //像素保活
        if (mOnePxReceiver == null) {
            mOnePxReceiver = new OnePxReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mOnePxReceiver, intentFilter);
        //屏幕点亮状态监听，用于单独控制音乐播放
        if (mScreenStateReceiver == null) {
            mScreenStateReceiver = new ScreenStateReceiver();
        }
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(KEEP_ACTION_SCREEN_OFF);
        intentFilter2.addAction(KEEP_ACTION_SCREEN_ON);
        registerReceiver(mScreenStateReceiver, intentFilter2);
        //启用前台服务，提升优先级
        if (KeepLive.sForegroundNotification != null) {
            Intent intent2 = new Intent(getApplicationContext(), NotificationClickReceiver.class);
            intent2.setAction(NotificationClickReceiver.ACTION_CLICK_NOTIFICATION);
            Notification notification = NotificationUtils.createNotification(this, KeepLive.sForegroundNotification.getTitle(), KeepLive.sForegroundNotification.getDescription(), KeepLive.sForegroundNotification.getIconRes(), intent2);
            startForeground(KEY_NOTIFICATION_ID, notification);
        }
        //绑定守护进程
        try {
            Intent intent3 = new Intent(this, RemoteService.class);
            mIsBoundRemoteService = this.bindService(intent3, mConnection, Context.BIND_ABOVE_CLIENT);
        } catch (Exception e) {
        }
        //隐藏服务通知
        try {
            if (KeepLive.sForegroundNotification == null || !KeepLive.sForegroundNotification.isShow()) {
                startService(new Intent(this, HideForegroundService.class));
            }
        } catch (Exception e) {
        }
        if (KeepLive.sKeepLiveService != null) {
            KeepLive.sKeepLiveService.onWorking();
        }
        return START_STICKY;
    }

    private void play() {
        if (KeepLive.sUseSilenceMusic) {
            if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
        }
    }

    private void pause() {
        if (KeepLive.sUseSilenceMusic) {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        }
    }

    private class ScreenStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            if (KEEP_ACTION_SCREEN_OFF.equals(action)) {
                mIsPause = false;
                play();
            } else if (KEEP_ACTION_SCREEN_ON.equals(action)) {
                mIsPause = true;
                pause();
            }
        }
    }

    private final class GuardBinder extends GuardAidl.Stub {

        @Override
        public void wakeUp(String title, String description, int iconRes) throws RemoteException {

        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (ServiceUtils.isServiceRunning(getApplicationContext(), KEY_LOCAL_SERVICE_NAME)) {
                Intent remoteService = new Intent(LocalService.this,
                        RemoteService.class);
                LocalService.this.startService(remoteService);
                Intent intent = new Intent(LocalService.this, RemoteService.class);
                mIsBoundRemoteService = LocalService.this.bindService(intent, mConnection,
                        Context.BIND_ABOVE_CLIENT);
            }
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm != null && pm.isScreenOn();
            if (isScreenOn) {
                sendBroadcast(new Intent(KEEP_ACTION_SCREEN_ON));
            } else {
                sendBroadcast(new Intent(KEEP_ACTION_SCREEN_OFF));
            }
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                if (mBinder != null && KeepLive.sForegroundNotification != null) {
                    GuardAidl guardAidl = GuardAidl.Stub.asInterface(service);
                    guardAidl.wakeUp(KeepLive.sForegroundNotification.getTitle(), KeepLive.sForegroundNotification.getDescription(), KeepLive.sForegroundNotification.getIconRes());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mConnection != null) {
            try {
                if (mIsBoundRemoteService) {
                    unbindService(mConnection);
                }
            } catch (Exception e) {
            }
        }
        try {
            unregisterReceiver(mOnePxReceiver);
            unregisterReceiver(mScreenStateReceiver);
        } catch (Exception e) {
        }
        if (KeepLive.sKeepLiveService != null) {
            KeepLive.sKeepLiveService.onStop();
        }
    }
}
