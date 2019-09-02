package com.xuexiang.keeplive.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xuexiang.keeplive.KeepLive;

/**
 * 前台通知点击
 *
 * @author xuexiang
 * @since 2019-08-14 15:25
 */
public final class NotificationClickReceiver extends BroadcastReceiver {

    public final static String ACTION_CLICK_NOTIFICATION = "com.xuexiang.keeplive.receiver.ACTION_CLICK_NOTIFICATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_CLICK_NOTIFICATION.equals(intent.getAction())) {
            if (KeepLive.sForegroundNotification != null) {
                if (KeepLive.sForegroundNotification.getNotificationClickListener() != null) {
                    KeepLive.sForegroundNotification.getNotificationClickListener().onNotificationClick(context, intent);
                }
            }
        }
    }
}
