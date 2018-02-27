package com.cl.baseapplication.update;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cl.baseapplication.update.service.UpdateService;


/**
 * update this app
 */
public class UpdateUtil {

    private UpdateUtil() {
    }

    public static UpdateUtil getInstance() {
        return UpdateUtilHolder.INSTANCE;
    }

    /**
     * check update this app
     *
     * @param context this context
     * @param isShow  yes or no  show update tip :
     */
    public void checkUpdate(Context context, boolean isShow) {

        Intent intent = new Intent(context, UpdateService.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isShow", isShow);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    private static class UpdateUtilHolder {
        private static final UpdateUtil INSTANCE = new UpdateUtil();
    }

}
