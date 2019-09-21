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

package com.xuexiang.xgdemo.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.xuexiang.xaop.annotation.MainThread;
import com.xuexiang.xgdemo.R;
import com.xuexiang.xgdemo.util.SettingSPUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.core.XPushManager;
import com.xuexiang.xpush.core.queue.impl.MessageSubscriber;
import com.xuexiang.xpush.entity.CustomMessage;
import com.xuexiang.xpush.entity.XPushCommand;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.app.AppUtils;
import com.xuexiang.xutil.app.IntentUtils;
import com.xuexiang.xutil.app.ProcessUtils;
import com.xuexiang.xutil.app.ServiceUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.BindView;

import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_REGISTER;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_UNREGISTER;

/**
 * @author xuexiang
 * @since 2019-09-21 14:23
 */
@Page(name = "推送设置")
public class PushSettingFragment extends XPageFragment {

    @BindView(R.id.tv_token)
    TextView tvToken;
    @BindView(R.id.switch_other_push)
    SwitchCompat switchOtherPush;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_push_setting;
    }


    @Override
    protected TitleBar initTitleBar() {
        TitleBar titleBar = super.initTitleBar();
        titleBar.addAction(new TitleBar.TextAction("分享") {
            @Override
            public void performAction(View view) {
                String token = XPush.getPushToken();
                if (!StringUtils.isEmpty(token)) {
                    shareText(token);
                } else {
                    ToastUtils.toast("推送未连接！");
                }
            }
        });
        return titleBar;
    }

    /**
     * 分享文字
     *
     * @param content 文字
     */
    private void shareText(String content) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        shareIntent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        tvToken.setText(XPush.getPushToken());
        switchOtherPush.setChecked(SettingSPUtils.getInstance().enableOtherPush());
        switchOtherPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingSPUtils.getInstance().setEnableOtherPush(isChecked);
                rebootApp(XPush.getContext());
            }
        });
    }

    /**
     * 重启app
     *
     * @param context
     */
    private void rebootApp(Context context) {
        Intent intent = IntentUtils.getLaunchAppIntent(context.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
        AppUtils.exitApp();
    }

    /**
     * 初始化监听
     */
    @Override
    protected void initListeners() {
    }

}
