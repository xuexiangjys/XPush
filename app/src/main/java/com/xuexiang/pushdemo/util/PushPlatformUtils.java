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

package com.xuexiang.pushdemo.util;

import android.app.Application;

import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.core.IPushClient;
import com.xuexiang.xpush.core.IPushInitCallback;
import com.xuexiang.xpush.huawei.HuaweiPushClient;
import com.xuexiang.xpush.jpush.JPushClient;
import com.xuexiang.xpush.umeng.UMengPushClient;
import com.xuexiang.xpush.xg.XGPushClient;
import com.xuexiang.xpush.xiaomi.XiaoMiPushClient;
import com.xuexiang.xutil.system.RomUtils;

import static com.xuexiang.xutil.system.RomUtils.SYS_EMUI;
import static com.xuexiang.xutil.system.RomUtils.SYS_MIUI;

/**
 * 推送平台初始化工具
 *
 * @author xuexiang
 * @since 2019-09-20 17:44
 */
public final class PushPlatformUtils {

    private PushPlatformUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化推送平台
     *
     * @param application
     */
    public static void initPushClient(Application application) {
        int platformCode = SettingSPUtils.getInstance().getPushPlatformCode();
        if (platformCode == 0) {
            //自动注册
            XPush.init(application, new IPushInitCallback() {
                @Override
                public boolean onInitPush(int platformCode, String platformName) {
                    String romName = RomUtils.getRom().getRomName();
                    if (romName.equals(SYS_EMUI)) {
                        return platformCode == HuaweiPushClient.HUAWEI_PUSH_PLATFORM_CODE && platformName.equals(HuaweiPushClient.HUAWEI_PUSH_PLATFORM_NAME);
                    } else if (romName.equals(SYS_MIUI)) {
                        return platformCode == XiaoMiPushClient.MIPUSH_PLATFORM_CODE && platformName.equals(XiaoMiPushClient.MIPUSH_PLATFORM_NAME);
                    } else {
                        return platformCode == JPushClient.JPUSH_PLATFORM_CODE && platformName.equals(JPushClient.JPUSH_PLATFORM_NAME);
                    }
                }
            });
            SettingSPUtils.getInstance().setPushPlatformCode(XPush.getPlatformCode());
        } else {
            //手动注册
            switch (platformCode) {
                case JPushClient.JPUSH_PLATFORM_CODE:
                    XPush.init(application, new JPushClient());
                    break;
                case UMengPushClient.UMENG_PUSH_PLATFORM_CODE:
                    XPush.init(application, new UMengPushClient());
                    break;
                case HuaweiPushClient.HUAWEI_PUSH_PLATFORM_CODE:
                    XPush.init(application, new HuaweiPushClient());
                    break;
                case XiaoMiPushClient.MIPUSH_PLATFORM_CODE:
                    XPush.init(application, new XiaoMiPushClient());
                    break;
                case XGPushClient.XGPUSH_PLATFORM_CODE:
                    XPush.init(application, new XGPushClient());
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 切换推送平台
     *
     * @param platformCode
     */
    public static void switchPushClient(int platformCode) {
        //先注销当前推送平台
        XPush.unRegister();
        //设置新的推送平台
        XPush.setIPushClient(getPushClientByPlatformCode(platformCode));
        //注册推送
        XPush.register();
        SettingSPUtils.getInstance().setPushPlatformCode(platformCode);
    }

    public static IPushClient getPushClientByPlatformCode(int platformCode) {
        switch (platformCode) {
            case UMengPushClient.UMENG_PUSH_PLATFORM_CODE:
                return new UMengPushClient();
            case HuaweiPushClient.HUAWEI_PUSH_PLATFORM_CODE:
                return new HuaweiPushClient();
            case XiaoMiPushClient.MIPUSH_PLATFORM_CODE:
                return new XiaoMiPushClient();
            case XGPushClient.XGPUSH_PLATFORM_CODE:
                return new XGPushClient();
            default:
                return new JPushClient();
        }
    }


}
