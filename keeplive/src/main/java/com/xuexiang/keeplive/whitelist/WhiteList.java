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

package com.xuexiang.keeplive.whitelist;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.xuexiang.keeplive.KeepLive;
import com.xuexiang.keeplive.whitelist.impl.DefaultWhiteListCallback;

import java.util.ArrayList;
import java.util.List;

import static android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;

/**
 * @author xuexiang
 * @since 2019-09-02 14:34
 */
public final class WhiteList {

    private static String sApplicationName;
    private static List<WhiteListIntentWrapper> sAllWhiteListIntent;
    private static List<WhiteListIntentWrapper> sMatchedWhiteListIntent;

    public static List<WhiteListIntentWrapper> getAllWhiteListIntent(Application application) {
        if (sAllWhiteListIntent == null) {
            sAllWhiteListIntent = new ArrayList<>();

            //Android 7.0+ Doze 模式
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                PowerManager pm = (PowerManager) application.getSystemService(Context.POWER_SERVICE);
                boolean ignoringBatteryOptimizations = pm.isIgnoringBatteryOptimizations(application.getPackageName());
                if (!ignoringBatteryOptimizations) {
                    Intent dozeIntent = new Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    dozeIntent.setData(Uri.parse("package:" + application.getPackageName()));
                    sAllWhiteListIntent.add(new WhiteListIntentWrapper(dozeIntent, IntentType.DOZE));
                }
            }

            //华为 自启管理
            Intent huaweiIntent = new Intent();
            huaweiIntent.setAction("huawei.intent.action.HSM_BOOTAPP_MANAGER");
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(huaweiIntent, IntentType.HUAWEI));

            //华为 锁屏清理
            Intent huaweiGodIntent = new Intent();
            huaweiGodIntent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(huaweiGodIntent, IntentType.HUAWEI_GOD));

            //小米 自启动管理
            Intent xiaomiIntent = new Intent();
            xiaomiIntent.setAction("miui.intent.action.OP_AUTO_START");
            xiaomiIntent.addCategory(Intent.CATEGORY_DEFAULT);
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(xiaomiIntent, IntentType.XIAOMI));

            //小米 神隐模式
            Intent xiaomiGodIntent = new Intent();
            xiaomiGodIntent.setComponent(new ComponentName("com.miui.powerkeeper", "com.miui.powerkeeper.ui.HiddenAppsConfigActivity"));
            xiaomiGodIntent.putExtra("package_name", application.getPackageName());
            xiaomiGodIntent.putExtra("package_label", getApplicationName());
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(xiaomiGodIntent, IntentType.XIAOMI_GOD));

            //三星 5.0/5.1 自启动应用程序管理
            Intent samsungLIntent = application.getPackageManager().getLaunchIntentForPackage("com.samsung.android.sm");
            if (samsungLIntent != null)
                sAllWhiteListIntent.add(new WhiteListIntentWrapper(samsungLIntent, IntentType.SAMSUNG_L));

            //三星 6.0+ 未监视的应用程序管理
            Intent samsungMIntent = new Intent();
            samsungMIntent.setComponent(new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.battery.BatteryActivity"));
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(samsungMIntent, IntentType.SAMSUNG_M));

            //魅族 自启动管理
            Intent meizuIntent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            meizuIntent.addCategory(Intent.CATEGORY_DEFAULT);
            meizuIntent.putExtra("packageName", application.getPackageName());
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(meizuIntent, IntentType.MEIZU));

            //魅族 待机耗电管理
            Intent meizuGodIntent = new Intent();
            meizuGodIntent.setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.powerui.PowerAppPermissionActivity"));
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(meizuGodIntent, IntentType.MEIZU_GOD));

            //Oppo 自启动管理
            Intent oppoIntent = new Intent();
            oppoIntent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(oppoIntent, IntentType.OPPO));

            //Oppo 自启动管理(旧版本系统)
            Intent oppoOldIntent = new Intent();
            oppoOldIntent.setComponent(new ComponentName("com.color.safecenter", "com.color.safecenter.permission.startup.StartupAppListActivity"));
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(oppoOldIntent, IntentType.OPPO_OLD));

            //Vivo 后台高耗电
            Intent vivoGodIntent = new Intent();
            vivoGodIntent.setComponent(new ComponentName("com.vivo.abe", "com.vivo.applicationbehaviorengine.ui.ExcessivePowerManagerActivity"));
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(vivoGodIntent, IntentType.VIVO_GOD));

            //金立 应用自启
            Intent gioneeIntent = new Intent();
            gioneeIntent.setComponent(new ComponentName("com.gionee.softmanager", "com.gionee.softmanager.MainActivity"));
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(gioneeIntent, IntentType.GIONEE));

            //乐视 自启动管理
            Intent letvIntent = new Intent();
            letvIntent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(letvIntent, IntentType.LETV));

            //乐视 应用保护
            Intent letvGodIntent = new Intent();
            letvGodIntent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.BackgroundAppManageActivity"));
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(letvGodIntent, IntentType.LETV_GOD));

            //酷派 自启动管理
            Intent coolpadIntent = new Intent();
            coolpadIntent.setComponent(new ComponentName("com.yulong.android.security", "com.yulong.android.seccenter.tabbarmain"));
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(coolpadIntent, IntentType.COOLPAD));

            //联想 后台管理
            Intent lenovoIntent = new Intent();
            lenovoIntent.setComponent(new ComponentName("com.lenovo.security", "com.lenovo.security.purebackground.PureBackgroundActivity"));
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(lenovoIntent, IntentType.LENOVO));

            //联想 后台耗电优化
            Intent lenovoGodIntent = new Intent();
            lenovoGodIntent.setComponent(new ComponentName("com.lenovo.powersetting", "com.lenovo.powersetting.ui.Settings$HighPowerApplicationsActivity"));
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(lenovoGodIntent, IntentType.LENOVO_GOD));

            //中兴 自启管理
            Intent zteIntent = new Intent();
            zteIntent.setComponent(new ComponentName("com.zte.heartyservice", "com.zte.heartyservice.autorun.AppAutoRunManager"));
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(zteIntent, IntentType.ZTE));

            //中兴 锁屏加速受保护应用
            Intent zteGodIntent = new Intent();
            zteGodIntent.setComponent(new ComponentName("com.zte.heartyservice", "com.zte.heartyservice.setting.ClearAppSettingsActivity"));
            sAllWhiteListIntent.add(new WhiteListIntentWrapper(zteGodIntent, IntentType.ZTE_GOD));
        }
        return sAllWhiteListIntent;
    }


    public static String getApplicationName() {
        if (sApplicationName == null) {
            PackageManager pm;
            ApplicationInfo ai;
            try {
                pm = KeepLive.getApplication().getPackageManager();
                ai = pm.getApplicationInfo(KeepLive.getApplication().getPackageName(), 0);
                sApplicationName = pm.getApplicationLabel(ai).toString();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                sApplicationName = KeepLive.getApplication().getPackageName();
            }
        }
        return sApplicationName;
    }

    public static List<WhiteListIntentWrapper> getMatchedWhiteListIntent() {
        if (sMatchedWhiteListIntent == null) {
            sMatchedWhiteListIntent = new ArrayList<>();
            List<WhiteListIntentWrapper> intentWrapperList = getAllWhiteListIntent(KeepLive.getApplication());
            for (final WhiteListIntentWrapper intentWrapper : intentWrapperList) {
                //如果本机上没有能处理这个Intent的Activity，说明不是对应的机型，直接忽略进入下一次循环。
                if (!intentWrapper.doesActivityExists()) continue;

                if (intentWrapper.getType() == IntentType.DOZE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        PowerManager pm = (PowerManager) KeepLive.getApplication().getSystemService(Context.POWER_SERVICE);
                        if (!pm.isIgnoringBatteryOptimizations(KeepLive.getApplication().getPackageName())) {
                            sMatchedWhiteListIntent.add(intentWrapper);
                        }
                    }
                } else {
                    sMatchedWhiteListIntent.add(intentWrapper);
                }
            }
        }
        return sMatchedWhiteListIntent;
    }


    //==========================跳转到白名单设置界面==============================//
    /**
     * 白名单意图跳转回调
     */
    private static IWhiteListCallback sIWhiteListCallback;

    /**
     * 设置白名单意图跳转回调
     *
     * @param sIWhiteListCallback
     */
    public static void setIWhiteListCallback(IWhiteListCallback sIWhiteListCallback) {
        WhiteList.sIWhiteListCallback = sIWhiteListCallback;
    }

    /**
     * 跳转到设置白名单的页面
     *
     * @param activity
     * @param target
     * @return
     */
    @NonNull
    public static List<WhiteListIntentWrapper> gotoWhiteListActivity(final Activity activity, String target) {
        checkCallback(target);

        List<WhiteListIntentWrapper> matchedWhiteListIntent = getMatchedWhiteListIntent();
        for (final WhiteListIntentWrapper intentWrapper : matchedWhiteListIntent) {
            if (intentWrapper.getType() == IntentType.DOZE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    PowerManager pm = (PowerManager) KeepLive.getApplication().getSystemService(Context.POWER_SERVICE);
                    if (pm.isIgnoringBatteryOptimizations(KeepLive.getApplication().getPackageName())) {
                        sMatchedWhiteListIntent.remove(intentWrapper);
                    } else {
                        sIWhiteListCallback.showWhiteList(activity, intentWrapper);
                    }
                }
            } else {
                sIWhiteListCallback.showWhiteList(activity, intentWrapper);
            }
        }
        return matchedWhiteListIntent;
    }

    /**
     * 跳转到设置白名单的页面
     *
     * @param fragment
     * @param target
     * @return
     */
    @NonNull
    public static List<WhiteListIntentWrapper> gotoWhiteListActivity(final Fragment fragment, String target) {
        checkCallback(target);

        List<WhiteListIntentWrapper> matchedWhiteListIntent = getMatchedWhiteListIntent();
        for (final WhiteListIntentWrapper intentWrapper : matchedWhiteListIntent) {
            if (intentWrapper.getType() == IntentType.DOZE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    PowerManager pm = (PowerManager) KeepLive.getApplication().getSystemService(Context.POWER_SERVICE);
                    if (pm.isIgnoringBatteryOptimizations(KeepLive.getApplication().getPackageName())) {
                        sMatchedWhiteListIntent.remove(intentWrapper);
                    } else {
                        sIWhiteListCallback.showWhiteList(fragment, intentWrapper);
                    }
                }
            } else {
                sIWhiteListCallback.showWhiteList(fragment, intentWrapper);
            }
        }
        return matchedWhiteListIntent;
    }

    private static void checkCallback(String target) {
        if (sIWhiteListCallback == null) {
            sIWhiteListCallback = new DefaultWhiteListCallback();
        }
        if (target == null) target = "核心服务的持续运行";
        sIWhiteListCallback.init(target, getApplicationName());
    }

    /**
     * 防止华为机型未加入白名单时按返回键回到桌面再锁屏后几秒钟进程被杀
     */
    public static void gotoHome(Activity a) {
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_HOME);
        a.startActivity(launcherIntent);
    }
}
