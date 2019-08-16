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

package com.xuexiang.xpush.notify.builder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.notify.NotificationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知的基础构建者
 *
 * @author xuexiang
 * @since 2019-08-16 10:35
 */
public class BaseBuilder {
    private static final String DEFAULT_CHANNEL_ID_PREFIX = "xpush_channel_id_";
    private static final String DEFAULT_CHANNEL_NAME_PREFIX = "xpush_channel_name_";

    private NotificationCompat.Builder mBuilder;

    //==========最基本的ui=============//
    /**
     * 顶部状态栏的小图标
     */
    private int mSmallIcon;
    /**
     * 通知中心的标题
     */
    protected CharSequence mContentTitle;
    /**
     * 通知中心中的内容
     */
    protected CharSequence mContentText;

    /**
     * 概要内容
     */
    protected CharSequence mSummaryText;

    private boolean mHeadUp;

    //==========事件=======//
    /**
     * 通知点击的事件
     */
    private PendingIntent mContentIntent;
    /**
     * 通知删除的事件
     */
    private PendingIntent mDeleteIntent;
    /**
     *
     */
    private PendingIntent mFullscreenIntent;

    //=======最基本的控制管理=========//
    /**
     * 通知ID
     */
    private int mId;
    /**
     * 通知渠道ID
     */
    private String mChannelId;
    /**
     * 通知渠道ID
     */
    private String mChannelName;
    /**
     * 大图标
     */
    private int mBigIcon;
    /**
     * 在顶部状态栏中的提示信息
     */
    private CharSequence mTicker = "您有新的消息";

    private CharSequence mSubText;

    /**
     * 通知时间
     */
    private long mWhen;

    /**
     * 是否显示通知时间
     */
    private boolean mIsShowWhen = true;

    /**
     * 通知栏上的按钮
     */
    private List<BtnActionBean> mBtnActionBeans;

    /**
     * 通知的优先级
     */
    private int mPriority = NotificationCompat.PRIORITY_DEFAULT;

    private int mDefaults = NotificationCompat.DEFAULT_LIGHTS;//默认只有走马灯提醒
    /**
     * 是否有声音
     */
    private boolean mIsSound = true;
    /**
     * 是否震动
     */
    private boolean mIsVibrate = true;
    /**
     * 是否闪烁
     */
    private boolean mLights = true;

    /**
     * 声音的资源地址
     */
    private Uri mSoundUri;
    /**
     * 通知振动
     */
    private long[] mVibratePatten;
    /**
     * 通知是否不可侧滑删除
     */
    private boolean mIsOnGoing = false;

    /**
     * 是否显示是前台服务的通知
     */
    private boolean mIsForeGroundService = false;

    /**
     * 通知的可见度
     */
    private int mVisibility = NotificationCompat.VISIBILITY_SECRET;

    /**
     * 通知的拓展样式
     */
    private NotificationCompat.Style mStyle;

    /**
     * 是否一直提示
     */
    private boolean mIsPolling = false;


    /**
     * 设置基础信息
     *
     * @param icon
     * @param contentTitle
     * @param contentText
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setBaseInfo(int icon, CharSequence contentTitle, CharSequence contentText) {
        mSmallIcon = icon;
        mContentTitle = contentTitle;
        mContentText = contentText;
        return (T) this;
    }

    /**
     * 设置顶部状态栏的小图标
     *
     * @param smallIcon
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setSmallIcon(int smallIcon) {
        mSmallIcon = smallIcon;
        return (T) this;
    }

    /**
     * 设置通知中心的标题
     *
     * @param contentTitle
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setContentTitle(CharSequence contentTitle) {
        mContentTitle = contentTitle;
        return (T) this;
    }

    /**
     * 设置通知中心中的内容
     *
     * @param contentText
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setContentText(CharSequence contentText) {
        mContentText = contentText;
        return (T) this;
    }

    /**
     * 设置通知的概要内容
     *
     * @param summaryText
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setSummaryText(CharSequence summaryText) {
        mSummaryText = summaryText;
        return (T) this;
    }

    /**
     * 设置通知的ID
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setId(int id) {
        mId = id;
        return (T) this;
    }

    /**
     * 设置渠道ID号
     *
     * @param channelId
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setChannelId(String channelId) {
        mChannelId = channelId;
        return (T) this;
    }

    /**
     * 设置渠道名称
     *
     * @param channelName
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setChannelName(String channelName) {
        mChannelName = channelName;
        return (T) this;
    }

    /**
     * 设置推送优先级
     *
     * @param priority
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setPriority(int priority) {
        mPriority = priority;
        return (T) this;
    }

    /**
     * 设置通知点击事件
     *
     * @param contentIntent
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setContentIntent(PendingIntent contentIntent) {
        mContentIntent = contentIntent;
        return (T) this;
    }

    /**
     * 设置通知删除事件
     *
     * @param deleteIntent
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setDeleteIntent(PendingIntent deleteIntent) {
        mDeleteIntent = deleteIntent;
        return (T) this;
    }

    /**
     * 设置通知全屏事件
     *
     * @param fullscreenIntent
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setFullScreenIntent(PendingIntent fullscreenIntent) {
        mFullscreenIntent = fullscreenIntent;
        return (T) this;
    }

    /**
     * 设置大图标
     *
     * @param bigIcon
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setBigIcon(int bigIcon) {
        mBigIcon = bigIcon;
        return (T) this;
    }

    public <T extends BaseBuilder> T setHeadUp(boolean headUp) {
        mHeadUp = headUp;
        return (T) this;
    }

    /**
     * 设置顶部状态栏中的提示信息
     *
     * @param ticker
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setTicker(CharSequence ticker) {
        mTicker = ticker;
        return (T) this;
    }

    public <T extends BaseBuilder> T setSubtext(CharSequence subText) {
        mSubText = subText;
        return (T) this;
    }

    /**
     * 设置通知的时间
     *
     * @param when
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setWhen(long when) {
        mWhen = when;
        return (T) this;
    }

    /**
     * 设置是否显示通知时间
     *
     * @param isShowWhen
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setIsShowWhen(boolean isShowWhen) {
        mIsShowWhen = isShowWhen;
        return (T) this;
    }

    /**
     * 增加按钮点击
     *
     * @param icon
     * @param text
     * @param pendingIntent
     * @return
     */
    public <T extends BaseBuilder> T addAction(int icon, CharSequence text, PendingIntent pendingIntent) {
        if (mBtnActionBeans == null) {
            mBtnActionBeans = new ArrayList<>();
        }
        if (mBtnActionBeans.size() > 5) {
            throw new RuntimeException("5 buttons at most!");
        }
        mBtnActionBeans.add(new BtnActionBean(icon, text, pendingIntent));
        return (T) this;
    }

    /**
     * 设置表现形式
     *
     * @param sound   是否有声音
     * @param vibrate 是否震动
     * @param lights  是否闪烁
     * @return
     */
    public <T extends BaseBuilder> T setDisplayForm(boolean sound, boolean vibrate, boolean lights) {
        mIsSound = sound;
        mIsVibrate = vibrate;
        mLights = lights;
        return (T) this;
    }

    /**
     * 设置声音的资源地址
     *
     * @param soundUri
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setSoundUri(Uri soundUri) {
        mSoundUri = soundUri;
        return (T) this;
    }

    /**
     * 设置通知振动
     *
     * @param vibratePatten
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setVibratePatten(long[] vibratePatten) {
        mVibratePatten = vibratePatten;
        return (T) this;
    }

    /**
     * 设置通知是否不可侧滑删除
     *
     * @param isOnGoing
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setIsOnGoing(boolean isOnGoing) {
        mIsOnGoing = isOnGoing;
        return (T) this;
    }

    /**
     * 设置是否显示是前台服务的通知
     *
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setForegroundService() {
        mIsForeGroundService = true;
        mIsOnGoing = true;
        return (T) this;
    }

    /**
     * 设置通知的可见度
     *
     * @param visibility
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setVisibility(int visibility) {
        mVisibility = visibility;
        return (T) this;
    }

    /**
     * 设置通知的拓展样式
     *
     * @param style
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setStyle(NotificationCompat.Style style) {
        mStyle = style;
        return (T) this;
    }

    /**
     * 设置是否一直提示
     *
     * @param isPolling
     * @param <T>
     * @return
     */
    public <T extends BaseBuilder> T setIsPolling(boolean isPolling) {
        mIsPolling = isPolling;
        return (T) this;
    }

    /**
     * 构建通知内容
     */
    public void build() {
        beforeBuild();

        if (mChannelId == null) {
            mChannelId = DEFAULT_CHANNEL_ID_PREFIX + mId;
        }
        if (mChannelName == null) {
            mChannelName = DEFAULT_CHANNEL_NAME_PREFIX + mId;
        }
        mBuilder = new NotificationCompat.Builder(XPush.getContext(), mChannelId);

        if (mSmallIcon > 0) {
            mBuilder.setSmallIcon(mSmallIcon);// 设置顶部状态栏的小图标
        }
        if (mBigIcon > 0) {
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(XPush.getContext().getResources(), mBigIcon));
        }

        mBuilder.setTicker(mTicker);  // 在顶部状态栏中的提示信息

        mBuilder.setContentTitle(mContentTitle);// 设置通知中心的标题

        if (!TextUtils.isEmpty(mContentText)) {
            mBuilder.setContentText(mContentText);// 设置通知中心中的内容
        }

        if (!TextUtils.isEmpty(mSubText)) {
            mBuilder.setContentText(mSubText);
        }

        if (mWhen > 0) {
            mBuilder.setWhen(mWhen);
        } else {
            mBuilder.setWhen(System.currentTimeMillis());
        }

        mBuilder.setShowWhen(mIsShowWhen);

        //事件
        mBuilder.setContentIntent(mContentIntent);// 该通知要启动的Intent
        mBuilder.setDeleteIntent(mDeleteIntent);
        mBuilder.setFullScreenIntent(mFullscreenIntent, true);

        /*
         * 将AutoCancel设为true后，当你点击通知栏的notification后，它会自动被取消消失,
         * 不设置的话点击消息后也不清除，但可以滑动删除
         */
        mBuilder.setAutoCancel(true);

        // 将Ongoing设为true 那么notification将不能滑动删除
        mBuilder.setOngoing(mIsOnGoing);
        /*
         * 从Android4.1开始，可以通过以下方法，设置notification的优先级，
         * 优先级越高的，通知排的越靠前，优先级低的，不会在手机最顶部的状态栏显示图标
         */
        mBuilder.setPriority(mPriority);

        if (mIsSound) {
            mDefaults |= Notification.DEFAULT_SOUND;
            if (mSoundUri != null) {
                mBuilder.setSound(mSoundUri);
            }
        }
        if (mIsVibrate) {
            mDefaults |= Notification.DEFAULT_VIBRATE;
            if (mVibratePatten != null) {
                mBuilder.setVibrate(mVibratePatten);
            }
        }
        if (mLights) {
            mDefaults |= Notification.DEFAULT_LIGHTS;
        }
        mBuilder.setDefaults(mDefaults);

        //按钮
        if (mBtnActionBeans != null && mBtnActionBeans.size() > 0) {
            for (BtnActionBean bean : mBtnActionBeans) {
                mBuilder.addAction(bean.icon, bean.text, bean.pendingIntent);
            }
        }

        //HeadUp
        if (mHeadUp) {
            mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
            mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
        } else {
            mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            mBuilder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
        }

        mBuilder.setVisibility(mVisibility);

        if (mStyle != null) {
            mBuilder.setStyle(mStyle);
        }

        afterBuild();
    }


    protected void beforeBuild() {

    }

    protected void afterBuild() {

    }

    /**
     * 显示通知
     */
    public void show() {
        build();
        Notification notification = mBuilder.build();
        if (mIsForeGroundService) {
            notification.flags = Notification.FLAG_FOREGROUND_SERVICE;
        }
        if (mIsPolling) {
            notification.flags |= Notification.FLAG_INSISTENT;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(mChannelId, mChannelName, NotificationManager.IMPORTANCE_HIGH);
            NotificationUtils.getManager().createNotificationChannel(channel);
        }

        NotificationUtils.notify(mId, notification);
    }


    public NotificationCompat.Builder getBuilder() {
        return mBuilder;
    }


    /**
     * 按钮类
     */
    public static class BtnActionBean {
        public int icon;
        public CharSequence text;
        public PendingIntent pendingIntent;

        public BtnActionBean(int icon, CharSequence text, PendingIntent pendingIntent) {
            this.icon = icon;
            this.text = text;
            this.pendingIntent = pendingIntent;
        }
    }
}
