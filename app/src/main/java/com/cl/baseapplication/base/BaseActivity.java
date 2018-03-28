package com.cl.baseapplication.base;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.cl.baseapplication.activity.MainActivity;
import com.cl.baseapplication.utils.PermissionPool;

import java.util.Observable;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author cliang
 * @date 2017-12-11
 */

public class BaseActivity extends Activity {

    private boolean couldDoubleBackExit;
    private boolean doubleBackExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("config",Activity.MODE_PRIVATE);
    }

    /**
     * 设置是否可以双击返回退出，需要有该功能的页面set true即可
     *
     * @param couldDoubleBackExit true-开启双击退出
     */
    public void setCouldDoubleBackExit(boolean couldDoubleBackExit) {
        this.couldDoubleBackExit = couldDoubleBackExit;
    }

    @Override
    public void onBackPressed() {
        if (!couldDoubleBackExit) {
            // 非双击退出状态，使用原back逻辑
            super.onBackPressed();
            return;
        }

        // 双击返回键关闭程序
        // 如果两秒重置时间内再次点击返回,则退出程序
        if (doubleBackExitPressedOnce) {
            exit();
            return;
        }

        doubleBackExitPressedOnce = true;
        showToast("再按一次返回键关闭程序");
//        Observable.just(null)
//                .delay(2, TimeUnit.SECONDS)
//                .subscribe(new Action1<Object>() {
//                    @Override
//                    public void call(Object o) {
//                        // 延迟两秒后重置标志位为false
//                        doubleBackExitPressedOnce = false;
//                    }
//                });
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 退出程序
     */
    protected void exit() {
        // 退出程序方法有多种
        // 这里使用clear + new task的方式清空整个任务栈,只保留新打开的Main页面
        // 然后Main页面接收到退出的标志位exit=true,finish自己,这样就关闭了全部页面
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("exit", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * Android6.0 权限处理
     * @param code 权限标记Code
     * @param permissionName 权限名称
     */
    public void permissionDispose(@PermissionPool.PermissionCode int code,
                                  @PermissionPool.PermissionName String permissionName){
        if(ContextCompat.checkSelfPermission(this, permissionName) != PackageManager.PERMISSION_GRANTED){
            //没有权限,开始申请
            ActivityCompat.requestPermissions(this, new String[]{permissionName}, code);
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
