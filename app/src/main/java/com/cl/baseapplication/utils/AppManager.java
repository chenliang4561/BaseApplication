package com.cl.baseapplication.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 *
 * @author cliang
 * @date 2017-12-19
 */

public class AppManager {

    private static Stack<Activity> mStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单实例
     */
    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (mStack == null) {
            mStack = new Stack<>();
        }
        mStack.add(activity);
        Object[] objects = mStack.toArray();
        for (int i = 0; i < objects.length; i++) {
            Activity activity1 = (Activity) objects[i];
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = mStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = mStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            mStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : mStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 除最顶层activity，结束所有顶层以下activity
     */
    public void finishActivityExceptLast() {

    }

    /**
     * 除第一个Activity页面外，结束所有activity
     * 修改了删除顺序
     */
    public void finishActivityExceptFirst() {
        for (int i = 0; i < mStack.size(); i++) {
            System.out.println("....." + mStack.get(i).toString());
        }
        for (int i = mStack.size(); i > 1; i--) {
            if (null != mStack.get(i - 1)) {
                mStack.get(i - 1).finish();
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = mStack.size(); i > 0; i--) {
        }
        for (int i = mStack.size(); i > 0; i--) {

            if (null != mStack.get(i - 1)) {
                mStack.get(i - 1).finish();
            }
        }
        mStack.clear();
        if (!mStack.empty()) {
            for (int i = 0; i < mStack.size(); i++) {
                System.out.println(mStack.get(i).toString());
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
