package com.cl.baseapplication.update.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.cl.baseapplication.update.activity.UpdateActivity;
import com.cl.baseapplication.update.callback.UpdateCallback;
import com.cl.baseapplication.update.callback.impl.DefaultUpdateImpl;
import com.cl.baseapplication.update.data.UpdateInfo;


public class UpdateService extends IntentService {

    private static final String TAG = "UpdateService.class";

    private boolean isShow;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UpdateService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            isShow = intent.getExtras().getBoolean("isShow", false);
            new DefaultUpdateImpl().onCheckUpdate(this, isShow, new UpdateCallback.UpdateResultCallback() {
                @Override
                public void onUpdateSuccessResultCallback(UpdateInfo updateInfo) {
                    if (updateInfo != null)
                        onUpdateCallbackSuccess(UpdateService.this, isShow, updateInfo);
                    else
                        onUpdateCallbackFailure(UpdateService.this, "", isShow);
                }

                @Override
                public void onUpdateFailureResultCallback(String msg) {
                    onUpdateCallbackFailure(UpdateService.this, msg, isShow);
                }
            });
        }
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onUpdateCallbackSuccess(Context context, boolean isShow, UpdateInfo updateInfo) {

//        if (ParseUtil.getCode(updateInfo.getVersion()) > ParseUtil.getCode(AppUtil.getAppVersionName(context))) {

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("updateInfo", updateInfo);
        bundle.putBoolean("isShow", isShow);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, UpdateActivity.class);
        context.startActivity(intent);

//        } else {
//            if (isShow)
//            Toast.makeText(context,"已经是最新版本",Toast.LENGTH_SHORT).show();
//        }
//
        ((Service) context).stopSelf();
    }

    public void onUpdateCallbackFailure(Context context, String msg, boolean isShow) {
        if (isShow) {
            if (TextUtils.isEmpty(msg)) {
                Toast.makeText(context, "检查更新失败，请检查您的网络连接", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, msg + "", Toast.LENGTH_SHORT).show();
            }
        }
        ((Service) context).stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
