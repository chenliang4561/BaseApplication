package com.cl.baseapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cl.baseapplication.R;
import com.cl.baseapplication.base.BaseFragment;

/**
 * 我的
 *
 * @author cliang
 * @date 2017-12-19
 */

public class FragmentMine extends BaseFragment {

    private View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_mine, container, false);
        }
        return mView;
    }
}
