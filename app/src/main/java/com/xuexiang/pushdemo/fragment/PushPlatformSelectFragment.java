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
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpush.XPush;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2019-09-20 14:43
 */
@Page(name = "推送平台选择")
public class PushPlatformSelectFragment extends XPageFragment {
    @BindView(R.id.tv_push_platform)
    TextView tvPushPlatform;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_push_platform_select;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        tvPushPlatform.setText(String.format("%s(%d)", XPush.getPlatformName(), XPush.getPlatformCode()));
    }

    /**
     * 初始化监听
     */
    @Override
    protected void initListeners() {

    }


    @SingleClick
    @OnClick(R.id.btn_switch_platform)
    public void onViewClicked(View view) {
        switch(view.getId()) {
            case R.id.btn_switch_platform:
                switchPlatform();
                break;
            default:
                break;
        }
    }

    private void switchPlatform() {


    }
}
