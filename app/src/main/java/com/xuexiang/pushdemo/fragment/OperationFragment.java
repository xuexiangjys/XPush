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

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuexiang.pushdemo.R;
import com.xuexiang.xaop.annotation.MainThread;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xpush.XPush;
import com.xuexiang.xpush.core.XPushManager;
import com.xuexiang.xpush.core.queue.impl.MessageSubscriber;
import com.xuexiang.xpush.entity.CustomMessage;
import com.xuexiang.xpush.entity.Notification;
import com.xuexiang.xpush.entity.XPushCommand;
import com.xuexiang.xpush.huawei.HuaweiPushClient;
import com.xuexiang.xpush.umeng.UMengPushClient;
import com.xuexiang.xpush.util.PushUtils;
import com.xuexiang.xpush.xg.XGPushClient;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_GET_ALIAS;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_GET_TAG;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_REGISTER;
import static com.xuexiang.xpush.core.annotation.CommandType.TYPE_UNREGISTER;

/**
 * @author xuexiang
 * @since 2019-08-18 20:10
 */
@Page(name = "推送设置")
public class OperationFragment extends XPageFragment {

    @BindView(R.id.tv_push_platform)
    TextView tvPushPlatform;
    @BindView(R.id.tv_token)
    TextView tvToken;

    @BindView(R.id.ll_status)
    LinearLayout llStatus;
    @BindView(R.id.tv_status)
    TextView tvStatus;

    @BindView(R.id.ll_tag)
    LinearLayout llTag;
    @BindView(R.id.et_tag)
    EditText etTag;
    @BindView(R.id.btn_add_tag)
    Button btnAddTag;
    @BindView(R.id.btn_delete_tag)
    Button btnDeleteTag;
    @BindView(R.id.btn_get_tag)
    Button btnGetTag;

    @BindView(R.id.ll_alias)
    LinearLayout llAlias;
    @BindView(R.id.et_alias)
    EditText etAlias;
    @BindView(R.id.btn_bind_alias)
    Button btnBindAlias;
    @BindView(R.id.btn_unbind_alias)
    Button btnUnbindAlias;
    @BindView(R.id.btn_get_alias)
    Button btnGetAlias;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_operation;
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

    @Override
    protected void initViews() {
        tvPushPlatform.setText(String.format("%s(%d)", XPush.getPlatformName(), XPush.getPlatformCode()));
        tvToken.setText(XPush.getPushToken());
        tvStatus.setText(PushUtils.formatConnectStatus(XPush.getConnectStatus()));

        //极光推送都支持
        if (XPush.getPlatformCode() == UMengPushClient.UMENG_PUSH_PLATFORM_CODE || XPush.getPlatformCode() == XGPushClient.XGPUSH_PLATFORM_CODE) {
            //友盟/信鸽推送不支持获取tag和alias
            btnGetTag.setVisibility(View.GONE);
            btnGetAlias.setVisibility(View.GONE);

//            llStatus.setVisibility(View.GONE);
        } else if (XPush.getPlatformCode() == HuaweiPushClient.HUAWEI_PUSH_PLATFORM_CODE) {
            //华为推送不支持tag和alias操作
            llTag.setVisibility(View.GONE);
            llAlias.setVisibility(View.GONE);
        }
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
            if (!command.isSuccess()) {
                return;
            }
            switch (command.getType()) {
                case TYPE_REGISTER:
                    tvToken.setText(command.getContent());
                    break;
                case TYPE_UNREGISTER:
                    tvToken.setText("");
                    break;
                case TYPE_GET_TAG:
                    etTag.setText(command.getContent());
                    break;
                case TYPE_GET_ALIAS:
                    etAlias.setText(command.getContent());
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
                addTags();
                break;
            case R.id.btn_delete_tag:
                deleteTags();
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

    private void addTags() {
        String tag = etTag.getText().toString();
        if (StringUtils.isEmpty(tag)) {
            ToastUtils.toast("标签不能为空");
            return;
        }

        XPush.addTags(PushUtils.string2Array(tag));
    }

    private void deleteTags() {
        String tag = etTag.getText().toString();
        if (StringUtils.isEmpty(tag)) {
            ToastUtils.toast("标签不能为空");
            return;
        }

        XPush.deleteTags(PushUtils.string2Array(tag));
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
