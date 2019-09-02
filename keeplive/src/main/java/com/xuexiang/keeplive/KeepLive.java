package com.xuexiang.keeplive;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.xuexiang.keeplive.config.ForegroundNotification;
import com.xuexiang.keeplive.config.KeepLiveService;
import com.xuexiang.keeplive.service.JobHandlerService;
import com.xuexiang.keeplive.service.LocalService;
import com.xuexiang.keeplive.service.RemoteService;
import com.xuexiang.keeplive.utils.ServiceUtils;
import com.xuexiang.keeplive.whitelist.IWhiteListCallback;
import com.xuexiang.keeplive.whitelist.WhiteList;
import com.xuexiang.keeplive.whitelist.WhiteListIntentWrapper;

import java.util.List;

/**
 * 保活工具
 *
 * @author xuexiang
 * @since 2019-08-27 10:01
 */
public final class KeepLive {
    /**
     * 运行模式
     */
    public enum RunMode {
        /**
         * 省电模式
         * 省电一些，但保活效果会差一点
         */
        ENERGY,
        /**
         * 流氓模式
         * 相对耗电，但可造就不死之身
         */
        ROGUE
    }

    public static Application sApplication;
    public static ForegroundNotification sForegroundNotification = null;
    public static KeepLiveService sKeepLiveService = null;
    public static RunMode sRunMode = null;
    public static boolean sUseSilenceMusic = true;

    /**
     * 启动保活
     *
     * @param application            your application
     * @param foregroundNotification 前台服务 必须要，安卓8.0后必须有前台通知才能正常启动Service
     * @param keepLiveService        保活业务
     */
    public static void startWork(@NonNull Application application, @NonNull RunMode runMode, @NonNull ForegroundNotification foregroundNotification, @NonNull KeepLiveService keepLiveService) {
        if (ServiceUtils.isMainProcess(application)) {
            KeepLive.sApplication = application;
            KeepLive.sForegroundNotification = foregroundNotification;
            KeepLive.sKeepLiveService = keepLiveService;
            KeepLive.sRunMode = runMode;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //启动定时器，在定时器中启动本地服务和守护进程
                Intent intent = new Intent(application, JobHandlerService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    application.startForegroundService(intent);
                } else {
                    application.startService(intent);
                }
            } else {
                //启动本地服务
                Intent localIntent = new Intent(application, LocalService.class);
                //启动守护进程
                Intent guardIntent = new Intent(application, RemoteService.class);
                application.startService(localIntent);
                application.startService(guardIntent);
            }
        }
    }

    public static Application getApplication() {
        if (sApplication == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 KeepLive.startWork() 进行初始化！");
        }
        return sApplication;
    }

    /**
     * 是否启用无声音乐
     * 如不设置，则默认启用
     *
     * @param enable
     */
    public static void useSilenceMusice(boolean enable) {
        KeepLive.sUseSilenceMusic = enable;
    }

    //======================================白名单=============================================//

    public static List<WhiteListIntentWrapper> gotoWhiteListActivity(final Fragment fragment, String target) {
        return WhiteList.gotoWhiteListActivity(fragment, target);
    }

    public static List<WhiteListIntentWrapper> gotoWhiteListActivity(final Activity activity, String target) {
        return WhiteList.gotoWhiteListActivity(activity, target);
    }

    /**
     * 设置白名单意图跳转回调
     *
     * @param sIWhiteListCallback
     */
    public static void setIWhiteListCallback(IWhiteListCallback sIWhiteListCallback) {
        WhiteList.setIWhiteListCallback(sIWhiteListCallback);
    }

}
