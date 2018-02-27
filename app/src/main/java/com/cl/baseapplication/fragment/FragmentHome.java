package com.cl.baseapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cl.baseapplication.R;
import com.cl.baseapplication.adapter.XLixtViewAdapter;
import com.cl.baseapplication.base.BaseFragment;
import com.cl.baseapplication.widget.xlistview.XListView;

import java.util.ArrayList;

/**
 * 首页
 *
 * @author cliang
 * @date 2017-12-19
 */

public class FragmentHome extends BaseFragment{

    private View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_home, container, false);
            initView();
            initData();
        }
        return mView;
    }

    private void initView() {

    }

    private void initData() {

    }

}
