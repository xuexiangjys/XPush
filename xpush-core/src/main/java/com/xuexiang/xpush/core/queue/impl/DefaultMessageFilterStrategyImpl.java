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

package com.xuexiang.xpush.core.queue.impl;

import android.support.annotation.NonNull;

import com.xuexiang.xpush.core.queue.IMessageFilter;
import com.xuexiang.xpush.core.queue.IMessageFilterStrategy;
import com.xuexiang.xpush.entity.CustomMessage;
import com.xuexiang.xpush.entity.Notification;
import com.xuexiang.xpush.logs.PushLog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 默认消息推送过滤策略
 *
 * @author xuexiang
 * @since 2019-08-25 19:54
 */
public class DefaultMessageFilterStrategyImpl implements IMessageFilterStrategy {
    /**
     * 消息过滤器
     */
    private List<WeakReference<IMessageFilter>> mFilters = new ArrayList<>();

    @Override
    public boolean filterNotification(Notification notification) {
        if (mFilters == null || mFilters.size() == 0) {
            return false;
        }
        PushLog.d("正在对通知消息进行过滤:" + mFilters.size());

        Iterator<WeakReference<IMessageFilter>> it = mFilters.iterator();
        while (it.hasNext()) {
            IMessageFilter filter = it.next().get();
            if (filter != null) {
                if (filter.filter(notification)) {
                    PushLog.d("通知消息已被过滤:" + notification);
                    return true;
                }
            } else {
                it.remove();
            }
        }
        return false;
    }

    @Override
    public boolean filterCustomMessage(CustomMessage message) {
        if (mFilters == null || mFilters.size() == 0) {
            return false;
        }
        PushLog.d("正在对自定义（透传）消息进行过滤:" + mFilters.size());

        Iterator<WeakReference<IMessageFilter>> it = mFilters.iterator();
        while (it.hasNext()) {
            IMessageFilter filter = it.next().get();
            if (filter != null) {
                if (filter.filter(message)) {
                    PushLog.d("自定义（透传）消息已被过滤:" + message);
                    return true;
                }
            } else {
                it.remove();
            }
        }
        return false;
    }

    @Override
    public void addFilter(@NonNull IMessageFilter filter) {
        if (mFilters == null) {
            mFilters = new ArrayList<>();
        }

        WeakReference<IMessageFilter> reference = new WeakReference<>(filter);
        mFilters.add(reference);
    }

    @Override
    public void addFilter(int index, @NonNull IMessageFilter filter) {
        if (mFilters == null) {
            mFilters = new ArrayList<>();
        }

        WeakReference<IMessageFilter> reference = new WeakReference<>(filter);
        mFilters.add(index, reference);
    }

    @Override
    public void addFilters(@NonNull IMessageFilter... filters) {
        if (mFilters == null) {
            mFilters = new ArrayList<>();
        }

        List<WeakReference<IMessageFilter>> list = new ArrayList<>();
        for (IMessageFilter filter : filters) {
            list.add(new WeakReference<>(filter));
        }
        mFilters.addAll(list);
    }

    @Override
    public void setFilters(@NonNull IMessageFilter... filters) {
        if (mFilters == null) {
            mFilters = new ArrayList<>();
        } else {
            mFilters.clear();
        }
        addFilters(filters);
    }

    @Override
    public boolean removeFilter(@NonNull IMessageFilter filter) {
        if (mFilters == null || mFilters.size() == 0) {
            return false;
        }

        Iterator<WeakReference<IMessageFilter>> it = mFilters.iterator();
        while (it.hasNext()) {
            IMessageFilter mf = it.next().get();
            if (mf == filter) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeFilters(@NonNull IMessageFilter... filters) {
        for (IMessageFilter filter : filters) {
            removeFilter(filter);
        }
    }

    @Override
    public void removeAll() {
        if (mFilters != null) {
            mFilters.clear();
        }
    }
}
