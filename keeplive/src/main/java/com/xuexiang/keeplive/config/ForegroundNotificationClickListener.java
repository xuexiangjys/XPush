package com.xuexiang.keeplive.config;

import android.content.Context;
import android.content.Intent;

/**
 * 前台服务通知点击事件
 */
public interface ForegroundNotificationClickListener {

    void foregroundNotificationClick(Context context, Intent intent);
}
