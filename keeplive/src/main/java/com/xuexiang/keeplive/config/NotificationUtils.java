package com.xuexiang.keeplive.config;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

public class NotificationUtils extends ContextWrapper {
    public static final int KEY_NOTIFICATION_ID = 13691;

    private NotificationManager manager;
    private String id;
    private String name;
    private Context context;
    private NotificationChannel channel;

    private NotificationUtils(Context context) {
        super(context);
        this.context = context;
        id = context.getPackageName();
        name = context.getPackageName();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        if (channel == null) {
            channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(false);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0});
            channel.setSound(null, null);
            getManager().createNotificationChannel(channel);
        }
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content, int icon, Intent intent) {
        //PendingIntent.FLAG_UPDATE_CURRENT 这个类型才能传值
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new Notification.Builder(context, id)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(icon)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
    }

    public NotificationCompat.Builder getNotification_25(String title, String content, int icon, Intent intent) {
        //PendingIntent.FLAG_UPDATE_CURRENT 这个类型才能传值
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(context, id)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(icon)
                .setAutoCancel(true)
                .setVibrate(new long[]{0})
                .setContentIntent(pendingIntent);
    }

    public static void sendNotification(@NonNull Context context, @NonNull String title, @NonNull String content, @NonNull int icon, @NonNull Intent intent) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        Notification notification;
        if (Build.VERSION.SDK_INT >= 26) {
            notificationUtils.createNotificationChannel();
            notification = notificationUtils.getChannelNotification(title, content, icon, intent).build();
        } else {
            notification = notificationUtils.getNotification_25(title, content, icon, intent).build();
        }
        notificationUtils.getManager().notify(new Random().nextInt(10000), notification);
    }

    public static Notification createNotification(@NonNull Context context, @NonNull String title, @NonNull String content, @NonNull int icon, @NonNull Intent intent) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        Notification notification;
        if (Build.VERSION.SDK_INT >= 26) {
            notificationUtils.createNotificationChannel();
            notification = notificationUtils.getChannelNotification(title, content, icon, intent).build();
        } else {
            notification = notificationUtils.getNotification_25(title, content, icon, intent).build();
        }
        return notification;
    }
}
