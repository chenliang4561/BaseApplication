package com.cl.baseapplication.base;

import android.app.Application;

import com.android.volley.RequestQueue;

/**
 *
 * @author admin
 * @date 2017-12-11
 */

public class BaseApplication extends Application {

    private static BaseApplication instance;

    public static RequestQueue queue;

    public static BaseApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static RequestQueue getHttpQueue() {
        return queue;
    }
}
