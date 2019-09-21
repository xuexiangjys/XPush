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

package com.xuexiang.xgdemo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Process;

import com.tencent.android.tpush.XGPush4Msdk;
import com.tencent.android.tpush.XGPushConfig;
import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.util.PermissionUtils;
import com.xuexiang.xgdemo.push.CustomPushReceiver;
import com.xuexiang.xgdemo.util.SettingSPUtils;
import com.xuexiang.xpage.AppPageConfig;
import com.xuexiang.xpage.PageConfig;
import com.xuexiang.xpage.PageConfiguration;
import com.xuexiang.xpage.model.PageInfo;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.core.dispatcher.impl.Android26PushDispatcherImpl;
import com.xuexiang.xpush.xg.XGPushClient;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.List;

import static com.xuexiang.xaop.consts.PermissionConsts.STORAGE;

/**
 * @author xuexiang
 * @since 2018/11/7 下午1:12
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initLibs();

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
     * 初始化推送
     */
    @Permission(STORAGE)
    private void initPush() {
        XPush.debug(BuildConfig.DEBUG);

        //小米推送
        XGPushConfig.setMiPushAppId(getApplicationContext(), "2882303761518174770");
        XGPushConfig.setMiPushAppKey(getApplicationContext(), "5821817461770");
        XGPushConfig.setHuaweiDebug(BuildConfig.DEBUG);
        //支持第三方
        XGPushConfig.enableOtherPush(getApplicationContext(), SettingSPUtils.getInstance().enableOtherPush());
        XPush.init(this, new XGPushClient());

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
