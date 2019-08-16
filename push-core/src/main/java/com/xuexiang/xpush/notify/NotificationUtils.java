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

package com.xuexiang.xpush.notify;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.notify.builder.BaseBuilder;
import com.xuexiang.xpush.notify.builder.BigPicBuilder;
import com.xuexiang.xpush.notify.builder.BigTextBuilder;
import com.xuexiang.xpush.notify.builder.CustomViewBuilder;
import com.xuexiang.xpush.notify.builder.MailboxBuilder;
import com.xuexiang.xpush.notify.builder.ProgressBuilder;

/**
 * 通知栏工具类
 *
 * @author xuexiang
 * @since 2019-08-16 10:19
 */
public final class NotificationUtils {

    private static NotificationManager sNotificationManager;

    private NotificationUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    //==================通知的api======================//

    /**
     * 构建简单的通知
     *
     * @param id            通知的ID
     * @param smallIcon     顶部状态栏的小图标
     * @param contentTitle  通知中心的标题
     * @param contentText   通知中心中的内容
     * @param contentIntent 通知点击的事件
     * @return
     */
    public static BaseBuilder buildSimple(int id, int smallIcon, CharSequence contentTitle, CharSequence contentText, PendingIntent contentIntent) {
        return new BaseBuilder()
                .setId(id)
                .setBaseInfo(smallIcon, contentTitle, contentText)
                .setContentIntent(contentIntent);
    }

    /**
     * 构建带图片的通知
     *
     * @param id           通知的ID
     * @param smallIcon    顶部状态栏的小图标
     * @param contentTitle 通知中心的标题
     * @param summaryText  图片的概要信息
     * @return
     */
    public static BigPicBuilder buildBigPic(int id, int smallIcon, CharSequence contentTitle, CharSequence summaryText) {
        return new BigPicBuilder()
                .setId(id)
                .setSmallIcon(smallIcon)
                .setContentTitle(contentTitle)
                .setSummaryText(summaryText);
    }

    /**
     * 构建多文本通知
     *
     * @param id           通知的ID
     * @param smallIcon    顶部状态栏的小图标
     * @param contentTitle 通知中心的标题
     * @param contentText  通知中心中的内容
     * @return
     */
    public static BigTextBuilder buildBigText(int id, int smallIcon, CharSequence contentTitle, CharSequence contentText) {
        return new BigTextBuilder()
                .setId(id)
                .setBaseInfo(smallIcon, contentTitle, contentText);
    }

    /**
     * 构建带多条消息合并的消息盒通知
     *
     * @param id           通知的ID
     * @param smallIcon    顶部状态栏的小图标
     * @param contentTitle 通知中心的标题
     * @return
     */
    public static MailboxBuilder buildMailBox(int id, int smallIcon, CharSequence contentTitle) {
        return new MailboxBuilder()
                .setId(id)
                .setSmallIcon(smallIcon)
                .setContentTitle(contentTitle);
    }


    /**
     * 构建带进度条的通知
     *
     * @param id           通知的ID
     * @param smallIcon    顶部状态栏的小图标
     * @param contentTitle 顶部状态栏的小图标
     * @param max          最大进度
     * @param progress     目前的进度
     * @return
     */
    public static ProgressBuilder buildProgress(int id, int smallIcon, CharSequence contentTitle, int max, int progress) {
        return new ProgressBuilder()
                .setProgress(max, progress)
                .setId(id)
                .setSmallIcon(smallIcon)
                .setContentTitle(contentTitle);

    }

    /**
     * 构建无精确进度的通知
     *
     * @param id           通知的ID
     * @param smallIcon    顶部状态栏的小图标
     * @param contentTitle 顶部状态栏的小图标
     * @return
     */
    public static ProgressBuilder buildProgress(int id, int smallIcon, CharSequence contentTitle) {
        return new ProgressBuilder()
                .setIndeterminate(true)
                .setId(id)
                .setSmallIcon(smallIcon)
                .setContentTitle(contentTitle);
    }

    /**
     * 构建自定义通知
     *
     * @param id           通知的ID
     * @param smallIcon    顶部状态栏的小图标
     * @param contentTitle 顶部状态栏的小图标
     * @param packageName  包名
     * @param layoutId     自定义布局资源id
     * @return
     */
    public static CustomViewBuilder buildCustomView(int id, int smallIcon, CharSequence contentTitle, String packageName, int layoutId) {
        return new CustomViewBuilder(packageName, layoutId)
                .setId(id)
                .setSmallIcon(smallIcon)
                .setContentTitle(contentTitle);
    }


    /**
     * 通知
     *
     * @param id           通知ID
     * @param notification 通知的内容
     */
    public static void notify(int id, Notification notification) {
        if (sNotificationManager == null) {
            sNotificationManager = getNotificationManager();
        }
        sNotificationManager.notify(id, notification);
    }

    /**
     * 取消通知
     *
     * @param id 通知ID
     */
    public static void cancel(int id) {
        if (sNotificationManager == null) {
            sNotificationManager = getNotificationManager();
        }
        sNotificationManager.cancel(id);
    }

    /**
     * 取消所有通知
     */
    public static void cancelAll() {
        if (sNotificationManager == null) {
            sNotificationManager = getNotificationManager();
        }
        sNotificationManager.cancelAll();
    }

    public static NotificationManager getNotificationManager() {
        return (NotificationManager) XPush.getContext().getSystemService(Activity.NOTIFICATION_SERVICE);
    }

    public static NotificationManager getManager() {
        if (sNotificationManager == null) {
            sNotificationManager = getNotificationManager();
        }
        return sNotificationManager;
    }
}
