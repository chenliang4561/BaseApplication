package com.cl.baseapplication.base;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.cl.baseapplication.utils.PermissionPool;

/**
 *
 * @author cliang
 * @date 2017-12-11
 */

public class BaseFragment extends Fragment {
    /**
     * Android6.0 权限处理
     * @param code 权限标记Code
     * @param permissionName 权限名称
     */
    public void permissionDispose(@PermissionPool.PermissionCode int code,
                                  @PermissionPool.PermissionName String permissionName){
        if(ContextCompat.checkSelfPermission(getActivity(), permissionName) != PackageManager.PERMISSION_GRANTED){
            //没有权限,开始申请
            ActivityCompat.requestPermissions(getActivity(), new String[]{permissionName}, code);
        }else{
            //有权限
            onAccreditSucceed(code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
            //授权成功
            onAccreditSucceed(requestCode);
        }else if(grantResults[0]==PackageManager.PERMISSION_DENIED){
            //授权失败
            onAccreditFailure (requestCode);
        }
    }

    /**
     * 有授权执行的方法(子类重写)
     */
    public void onAccreditSucceed(int requestCode) {
    }

    /**
     * 没有授权执行的方法(子类重写)
     */
    public void onAccreditFailure(int requestCode) {
    }
}
