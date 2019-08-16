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

package com.xuexiang.xpush.notify.builder;

import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 带多条消息合并的消息盒通知
 *
 * @author xuexiang
 * @since 2019-08-16 10:30
 */
public class MailboxBuilder extends BaseBuilder {

    private List<String> mMessages;

    /**
     * 添加消息内容
     * @param msg
     * @return
     */
    public MailboxBuilder addMsg(String msg) {
        if (mMessages == null) {
            mMessages = new ArrayList<>();
        }
        mMessages.add(msg);
        return this;
    }

    /**
     * 设置消息的内容
     * @param messages
     * @return
     */
    public MailboxBuilder setMessages(List<String> messages) {
        mMessages = messages;
        return this;
    }

    @Override
    protected void beforeBuild() {
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        if (mMessages != null && mMessages.size() > 0) {
            if (mMessages.size() > 1) {
                for (String msg : mMessages) {
                    inboxStyle.addLine(msg);
                }
                String text = "你收到了[" + mMessages.size() + "]条信息";
                inboxStyle.setSummaryText(text);
                setStyle(inboxStyle);
            } else if (mMessages.size() == 1) {
                setContentText(mMessages.get(0));
            }
        }
    }
}
