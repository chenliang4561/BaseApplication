package com.cl.baseapplication.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cl.baseapplication.R;
import com.cl.baseapplication.adapter.XLixtViewAdapter;
import com.cl.baseapplication.base.BaseFragment;
import com.cl.baseapplication.utils.PermissionPool;
import com.cl.baseapplication.widget.xlistview.XListView;

import java.util.ArrayList;

/**
 * 首页
 *
 * @author cliang
 * @date 2017-12-19
 */

public class FragmentHome extends BaseFragment {

    private View mView;
    private Button mButton;

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

    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE
    };

    private void initView() {
        mButton = (Button) mView.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                permissionDispose(PermissionPool.CAMERA, Manifest.permission.CAMERA);
                permissionDispose(PermissionPool.CALL_PHONE, Manifest.permission.CALL_PHONE);
                permissionDispose(PermissionPool.GET_ACCOUNTS, Manifest.permission.GET_ACCOUNTS);
            }
        });
    }

    private void initData() {

    }

    @Override
    public void onAccreditSucceed(int requestCode) {
//        switch (requestCode) {
//            case PermissionPool.CAMERA:
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                //这里可能会编译报错，IDE提示你应该处理权限问题，然而我们已经处理了，所以直接运行不用管它
//                startActivityForResult(intent, 0);
//                break;
//        }
    }

    @Override
    public void onAccreditFailure(int requestCode) {
//        switch (requestCode) {
//            case PermissionPool.CAMERA:
//                Toast.makeText(getActivity(), "相机授权失败，无法继续操作", Toast.LENGTH_SHORT).show();
//                break;
//        }
    }
}
