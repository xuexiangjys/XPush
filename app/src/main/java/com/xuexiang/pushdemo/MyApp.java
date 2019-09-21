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

package com.xuexiang.pushdemo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.xuexiang.keeplive.KeepLive;
import com.xuexiang.keeplive.config.ForegroundNotification;
import com.xuexiang.keeplive.config.ForegroundNotificationClickListener;
import com.xuexiang.keeplive.config.KeepLiveService;
import com.xuexiang.pushdemo.push.CustomPushReceiver;
import com.xuexiang.pushdemo.util.PushPlatformUtils;
import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.util.PermissionUtils;
import com.xuexiang.xpage.AppPageConfig;
import com.xuexiang.xpage.PageConfig;
import com.xuexiang.xpage.PageConfiguration;
import com.xuexiang.xpage.model.PageInfo;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.core.dispatcher.impl.Android26PushDispatcherImpl;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.app.AppUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.List;

/**
 * @author xuexiang
 * @since 2018/11/7 下午1:12
 */
public class MyApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //解决4.x运行崩溃的问题
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initLibs();

        initKeepLive();

        if (shouldInitPush()) {
            initPush();
        }
    }

    /**
     * 初始化基础库
     */
    private void initLibs() {
        XUtil.init(this);
        XUtil.debug(BuildConfig.DEBUG);

        //页面注册
        PageConfig.getInstance().setPageConfiguration(new PageConfiguration() {
            @Override
            public List<PageInfo> registerPages(Context context) {
                //自动注册页面
                return AppPageConfig.getInstance().getPages();
            }
        }).debug("PageLog").enableWatcher(true).init(this);

        //初始化插件
        XAOP.init(this);
        //日志打印切片开启
        XAOP.debug(BuildConfig.DEBUG);
        //设置动态申请权限切片 申请权限被拒绝的事件响应监听
        XAOP.setOnPermissionDeniedListener(new PermissionUtils.OnPermissionDeniedListener() {
            @Override
            public void onDenied(List<String> permissionsDenied) {
                ToastUtils.toast("权限申请被拒绝:" + StringUtils.listToString(permissionsDenied, ","));
            }

        });
    }

    /**
     * 初始化保活
     */
    private void initKeepLive() {
        //定义前台服务的默认样式。即标题、描述和图标
        ForegroundNotification notification = new ForegroundNotification("推送服务", "推送服务正在运行中...", R.mipmap.ic_launcher,
                //定义前台服务的通知点击事件
                new ForegroundNotificationClickListener() {
                    @Override
                    public void onNotificationClick(Context context, Intent intent) {
                        ToastUtils.toast("点击了通知");
                        AppUtils.launchApp(getPackageName());
                    }
                })
                //要想不显示通知，可以设置为false，默认是false
                .setIsShow(true);
        //启动保活服务
        KeepLive.startWork(this, KeepLive.RunMode.ENERGY, notification,
                //你需要保活的服务，如socket连接、定时任务等，建议不用匿名内部类的方式在这里写
                new KeepLiveService() {
                    /**
                     * 运行中
                     * 由于服务可能会多次自动启动，该方法可能重复调用
                     */
                    @Override
                    public void onWorking() {
                        Log.e("xuexiang", "onWorking");
                    }
                    /**
                     * 服务终止
                     * 由于服务可能会被多次终止，该方法可能重复调用，需同onWorking配套使用，如注册和注销broadcast
                     */
                    @Override
                    public void onStop() {
                        Log.e("xuexiang", "onStop");
                    }
                }
        );
    }

    /**
     * 初始化推送
     */
    private void initPush() {
        XPush.debug(BuildConfig.DEBUG);

        PushPlatformUtils.initPushClient(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Android8.0静态广播注册失败解决方案一：动态注册
//            XPush.registerPushReceiver(new CustomPushReceiver());

            //Android8.0静态广播注册失败解决方案二：修改发射器
            XPush.setIPushDispatcher(new Android26PushDispatcherImpl(CustomPushReceiver.class));
        }

        XPush.register();
    }

    /**
     * @return 是否需要注册推送
     */
    private boolean shouldInitPush() {
        //只在主进程中注册(注意：umeng推送，除了在主进程中注册，还需要在channel中注册)
        String currentProcessName = getCurrentProcessName();
        String mainProcessName = BuildConfig.APPLICATION_ID;
        return mainProcessName.equals(currentProcessName) || mainProcessName.concat(":channel").equals(currentProcessName);
    }

    /**
     * 是否是主进程
     *
     * @return
     */
    private boolean isInMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取当前进程名称
     *
     */
    public String getCurrentProcessName() {
        int currentProcessId = Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            if (runningAppProcess.pid == currentProcessId) {
                return runningAppProcess.processName;
            }
        }
        return null;
    }

}
