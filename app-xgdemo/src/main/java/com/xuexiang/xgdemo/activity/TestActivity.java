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

package com.xuexiang.xgdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.xaop.annotation.MainThread;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xgdemo.R;
import com.xuexiang.xutil.app.IntentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @date 2018/3/18 下午6:25
 */
public class TestActivity extends AppCompatActivity {

    public static final String KEY_PARAM_STRING = "key_param_string";
    public static final String KEY_PARAM_INT = "key_param_int";

    @BindView(R.id.tv_content)
    TextView mTvContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        showContent("参数1：" + IntentUtils.getStringExtra(getIntent(), KEY_PARAM_STRING) + "， 参数2：" + IntentUtils.getIntExtra(getIntent(), KEY_PARAM_INT, 0));
    }


    @MainThread
    private void showContent(String content) {
        mTvContent.setText(content);
    }

    @SingleClick
    @OnClick(R.id.back)
    void OnClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent i = new Intent();
                i = IntentUtils.putExtra(i, "back", "返回的是1111");
                setResult(RESULT_OK, i);
                finish();
                break;
            default:
                break;
        }
    }

}
