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

package com.xuexiang.pushdemo.service;

import com.xuexiang.xutil.net.JsonUtil;

/**
 * 轮询任务【例子】
 *
 * @author xuexiang
 * @since 2019-09-10 23:41
 */
public class PollingTask {
    public static final int TYPE_ALARM_MANAGER = 1;
    public static final int TYPE_JOB_SCHEDULER = 2;
    public static final int TYPE_RXJAVA = 3;

    /**
     * 任务类型
     */
    private int mType;
    /**
     * 任务参数
     */
    private String mParam;

    public PollingTask(int type, String param) {
        mType = type;
        mParam = param;
    }

    public int getType() {
        return mType;
    }

    public PollingTask setType(int type) {
        mType = type;
        return this;
    }

    public String getParam() {
        return mParam;
    }

    public PollingTask setParam(String param) {
        mParam = param;
        return this;
    }

    public String toJson() {
        return JsonUtil.toJson(this);
    }

    @Override
    public String toString() {
        return "PollingTask{" +
                "mType=" + mType +
                ", mParam='" + mParam + '\'' +
                '}';
    }
}
