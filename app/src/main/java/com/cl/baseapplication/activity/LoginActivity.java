package com.cl.baseapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cl.baseapplication.R;
import com.cl.baseapplication.base.BaseActivity;

/**
 * 登录
 *
 * @author cliang
 * @date 2017-12-11
 */

public class LoginActivity extends BaseActivity {

    private TextView mHeadTitle;
    private RelativeLayout mHeadBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mHeadTitle = findViewById(R.id.tv_head_title);
        mHeadTitle.setText("登录");
        mHeadBack = findViewById(R.id.rl_head_back);
        mHeadBack.setVisibility(View.GONE);
    }
}
