package com.xuexiang.keeplive.config;

import android.content.Context;
import android.content.Intent;

/**
 * 前台服务通知点击事件
 */
public interface ForegroundNotificationClickListener {

    /**
     * 前台服务被点击
     *
     * @param context
     * @param intent
     */
    void onNotificationClick(Context context, Intent intent);
}
