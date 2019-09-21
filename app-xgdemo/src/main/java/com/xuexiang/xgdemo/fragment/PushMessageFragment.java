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

import android.widget.TextView;

import com.xuexiang.xaop.annotation.MainThread;
import com.xuexiang.xgdemo.R;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpush.core.XPushManager;
import com.xuexiang.xpush.core.queue.IMessageFilter;
import com.xuexiang.xpush.core.queue.impl.MessageSubscriber;
import com.xuexiang.xpush.entity.CustomMessage;
import com.xuexiang.xpush.entity.Notification;
import com.xuexiang.xutil.common.StringUtils;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019-08-17 17:14
 */
@Page(name = "推送消息接收")
public class PushMessageFragment extends XPageFragment {


    @BindView(R.id.tv_content)
    TextView tvContent;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_push_message;
    }


    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {

    }

    /**
     * 初始化监听
     */
    @Override
    protected void initListeners() {
        XPushManager.get()
                .addFilter(mMessageFilter)
                .register(mMessageSubscriber);
    }

    private IMessageFilter mMessageFilter = new IMessageFilter() {
        @Override
        public boolean filter(Notification notification) {
            if (StringUtils.isEmpty(notification.getContent()) || notification.getContent().contains("XPush")) {
                showMessage("通知被拦截");
                return true;
            }
            return false;
        }

        @Override
        public boolean filter(CustomMessage message) {
            if (StringUtils.isEmpty(message.getMsg()) || message.getMsg().contains("XPush")) {
                showMessage("自定义消息被拦截");
                return true;
            }
            return false;
        }
    };

    private MessageSubscriber mMessageSubscriber = new MessageSubscriber() {
        @Override
        public void onMessageReceived(CustomMessage message) {
            showMessage(String.format("收到自定义消息:%s", message));
        }

        @Override
        public void onNotification(Notification notification) {
            showMessage(String.format("收到通知:%s", notification));
        }
    };

    @MainThread
    private void showMessage(String msg) {
        tvContent.setText(msg);
    }


    @Override
    public void onDestroyView() {
        XPushManager.get().unregister(mMessageSubscriber);
        XPushManager.get().removeFilter(mMessageFilter);
        super.onDestroyView();
    }
}
