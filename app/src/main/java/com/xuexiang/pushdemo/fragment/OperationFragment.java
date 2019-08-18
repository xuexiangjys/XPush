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

package com.xuexiang.pushdemo.fragment;

import android.view.View;
import android.widget.TextView;

import com.xuexiang.pushdemo.R;
import com.xuexiang.xaop.annotation.MainThread;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.core.XPushManager;
import com.xuexiang.xpush.core.queue.impl.MessageSubscriber;
import com.xuexiang.xpush.entity.CustomMessage;
import com.xuexiang.xpush.entity.Notification;
import com.xuexiang.xpush.util.PushUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2019-08-18 20:10
 */
@Page(name = "推送设置")
public class OperationFragment extends XPageFragment {

    @BindView(R.id.tv_push_platform)
    TextView tvPushPlatform;
    @BindView(R.id.tv_status)
    TextView tvStatus;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_operation;
    }

    @Override
    protected void initViews() {
        tvPushPlatform.setText(String.format("%s(%d)", XPush.getPlatformName(), XPush.getPlatformCode()));
        tvStatus.setText(PushUtils.formatConnectStatus(XPush.getConnectStatus()));
    }

    @Override
    protected void initListeners() {
        XPushManager.get().register(mMessageSubscriber);
    }

    private MessageSubscriber mMessageSubscriber = new MessageSubscriber() {
        @MainThread
        @Override
        public void onMessageReceived(CustomMessage message) {
        }

        @MainThread
        @Override
        public void onNotification(Notification notification) {
        }

        @MainThread
        @Override
        public void onConnectStatusChanged(int connectStatus) {
            tvStatus.setText(PushUtils.formatConnectStatus(connectStatus));
        }
    };

    @SingleClick
    @OnClick({R.id.btn_start, R.id.btn_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                XPush.register();
                break;
            case R.id.btn_stop:
                XPush.unRegister();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        XPushManager.get().unregister(mMessageSubscriber);
        super.onDestroyView();
    }

}
