package com.cl.baseapplication.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * activity基类
 *
 * @author cl
 * @date 2018-3-28
 */

public abstract class BaseBindActivity<VB extends ViewDataBinding> extends AppCompatActivity {

    protected VB mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().inflate(this.getLayoutId(),null,false);
        mBinding = DataBindingUtil.bind(rootView);
        super.setContentView(rootView);
        initView();
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initView();
}
