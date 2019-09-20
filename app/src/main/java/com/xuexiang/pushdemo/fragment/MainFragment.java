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

import android.view.KeyEvent;
import android.view.View;

import com.xuexiang.keeplive.whitelist.WhiteList;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageContainerListFragment;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.tip.ToastUtils;

/**
 * @author xuexiang
 * @since 2018/11/7 下午1:16
 */
@Page(name = "XPush 消息推送", anim = CoreAnim.none)
public class MainFragment extends XPageContainerListFragment implements ClickUtils.OnClick2ExitListener {

    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                //此处填写fragment
                PushPlatformSelectFragment.class,
                OperationFragment.class,
                PushMessageFragment.class,
                NotifyFragment.class,
                KeepLiveFragment.class,
                PollingFragment.class
        };
    }

    @Override
    protected TitleBar initTitleBar() {
        return super.initTitleBar().setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickUtils.exitBy2Click(2000, MainFragment.this);
            }
        });
    }


    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ClickUtils.exitBy2Click(2000, MainFragment.this);
        }
        return true;
    }

    /**
     * 再点击一次
     */
    @Override
    public void onRetry() {
        ToastUtils.toast("再按一次退出程序");
    }

    /**
     * 退出
     */
    @Override
    public void onExit() {
        WhiteList.gotoHome(getActivity());
    }
}
