package com.xuexiang.keeplive.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.xuexiang.keeplive.activity.OnePixelActivity;

public final class OnePxReceiver extends BroadcastReceiver {
    public static final String KEEP_ACTION_SCREEN_OFF = "com.xuexiang.keeplive.receiver._ACTION_SCREEN_OFF";
    public static final String KEEP_ACTION_SCREEN_ON = "com.xuexiang.keeplive.receiver._ACTION_SCREEN_ON";

    private Handler mHandler;
    private boolean mScreenOn = true;

    public OnePxReceiver() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_OFF.equals(action)) {    //屏幕关闭的时候接受到广播
            mScreenOn = false;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!mScreenOn) {
                        Intent intent2 = new Intent(context, OnePixelActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);
                        try {
                            pendingIntent.send();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 1000);
            //通知屏幕已关闭，开始播放无声音乐
            context.sendBroadcast(new Intent(KEEP_ACTION_SCREEN_OFF));
        } else if (Intent.ACTION_SCREEN_ON.equals(action)) {   //屏幕打开的时候发送广播  结束一像素
            mScreenOn = true;
            //通知屏幕已点亮，停止播放无声音乐
            context.sendBroadcast(new Intent(KEEP_ACTION_SCREEN_ON));
        }
    }
}
