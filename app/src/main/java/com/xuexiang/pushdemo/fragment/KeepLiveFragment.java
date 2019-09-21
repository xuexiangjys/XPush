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

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.xuexiang.keeplive.KeepLive;
import com.xuexiang.keeplive.whitelist.IWhiteListCallback;
import com.xuexiang.keeplive.whitelist.IWhiteListProvider;
import com.xuexiang.keeplive.whitelist.WhiteListIntentWrapper;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageSimpleListFragment;

import java.util.List;

/**
 * @author xuexiang
 * @since 2019-09-02 14:07
 */
@Page(name = "KeepLive保活机制")
public class KeepLiveFragment extends XPageSimpleListFragment {
    /**
     * 初始化例子
     *
     * @param lists
     * @return
     */
    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("添加到白名单");
        lists.add("停止保活");
        return lists;
    }

    /**
     * 条目点击
     *
     * @param position
     */
    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
//                //自定义设置白名单跳转意图的数据提供
//                KeepLive.setIWhiteListProvider(new IWhiteListProvider() {
//                    @Override
//                    public List<WhiteListIntentWrapper> getWhiteList(Application application) {
//                        return null;
//                    }
//                });
//                //自定义设置白名单的显示处理
//                KeepLive.setIWhiteListCallback(new IWhiteListCallback() {
//                    @Override
//                    public void init(@NonNull String target, @NonNull String appName) {
//
//                    }
//
//                    @Override
//                    public void showWhiteList(@NonNull Activity activity, @NonNull WhiteListIntentWrapper intentWrapper) {
//
//                    }
//
//                    @Override
//                    public void showWhiteList(@NonNull Fragment fragment, @NonNull WhiteListIntentWrapper intentWrapper) {
//
//                    }
//                });
                KeepLive.gotoWhiteListActivity(this, "推送服务");
                break;
            case 1:
                KeepLive.stopWork();
                break;
            default:
                break;
        }
    }
}
