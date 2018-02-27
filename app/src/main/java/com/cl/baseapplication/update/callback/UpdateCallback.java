package com.cl.baseapplication.update.callback;

import android.content.Context;

import com.cl.baseapplication.update.data.UpdateInfo;


/**
 * @author admin
 */
public interface UpdateCallback {

    public void onCheckUpdate(Context context, final boolean isShow, final UpdateResultCallback resultCallback);

    public interface UpdateResultCallback {
        public void onUpdateSuccessResultCallback(final UpdateInfo updateInfo);

        public void onUpdateFailureResultCallback(String msg);
    }
}
