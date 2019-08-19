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
import android.widget.EditText;
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
import com.xuexiang.xpush.entity.XPushCommand;
import com.xuexiang.xpush.util.PushUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_GET_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_GET_TAG;

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
    @BindView(R.id.et_tag)
    EditText etTag;
    @BindView(R.id.et_alias)
    EditText etAlias;

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

        @MainThread
        @Override
        public void onCommandResult(XPushCommand command) {
            switch(command.getType()) {
                case TYPE_GET_TAG:
                    etTag.setText(command.getToken());
                    break;
                case TYPE_GET_ALIAS:
                    etAlias.setText(command.getToken());
                    break;
                default:
                    break;
            }
        }
    };

    @SingleClick
    @OnClick({R.id.btn_start, R.id.btn_stop, R.id.btn_add_tag, R.id.btn_delete_tag, R.id.btn_get_tag, R.id.btn_bind_alias, R.id.btn_unbind_alias, R.id.btn_get_alias})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                XPush.register();
                break;
            case R.id.btn_stop:
                XPush.unRegister();
                break;
            case R.id.btn_add_tag:
                addTag();
                break;
            case R.id.btn_delete_tag:
                deleteTag();
                break;
            case R.id.btn_get_tag:
                XPush.getTags();
                break;
            case R.id.btn_bind_alias:
                bindAlias();
                break;
            case R.id.btn_unbind_alias:
                unBindAlias();
                break;
            case R.id.btn_get_alias:
                XPush.getAlias();
                break;
            default:
                break;
        }
    }

    private void addTag() {
        String tag = etTag.getText().toString();
        if (StringUtils.isEmpty(tag)) {
            ToastUtils.toast("标签不能为空");
            return;
        }

        XPush.addTag(tag);
    }

    private void deleteTag() {
        String tag = etTag.getText().toString();
        if (StringUtils.isEmpty(tag)) {
            ToastUtils.toast("标签不能为空");
            return;
        }

        XPush.deleteTag(tag);
    }

    private void bindAlias() {
        String alias = etAlias.getText().toString();
        if (StringUtils.isEmpty(alias)) {
            ToastUtils.toast("别名不能为空");
            return;
        }

        XPush.bindAlias(alias);
    }

    private void unBindAlias() {
        String alias = etAlias.getText().toString();
        if (StringUtils.isEmpty(alias)) {
            ToastUtils.toast("别名不能为空");
            return;
        }

        XPush.unBindAlias(alias);
    }

    @Override
    public void onDestroyView() {
        XPushManager.get().unregister(mMessageSubscriber);
        super.onDestroyView();
    }

}
