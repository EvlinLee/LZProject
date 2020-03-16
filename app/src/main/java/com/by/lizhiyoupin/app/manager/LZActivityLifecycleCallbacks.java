package com.by.lizhiyoupin.app.manager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.by.lizhiyoupin.app.common.log.LZLog;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/23 14:59
 * Summary: activity生命周期
 */
public class LZActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = LZActivityLifecycleCallbacks.class.getSimpleName();

    /**
     * 首次启动认为是从后台进入前台
     */
    private boolean isInBackground = true;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LZLog.d(TAG, "onActivityCreated : activity = " + activity);
    }


    @Override
    public void onActivityDestroyed(Activity activity) {
        LZLog.d(TAG, "onActivityDestroyed : activity = " + activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        LZLog.d(TAG, "onActivityResumed : activity = " + activity);


    }

    @Override
    public void onActivityPaused(Activity activity) {
        LZLog.d(TAG, "onActivityPaused : activity = " + activity);

    }

    @Override
    public void onActivityStarted(Activity activity) {
        LZLog.d(TAG, "onActivityStarted : activity = " + activity);

    }

    @Override
    public void onActivityStopped(Activity activity) {
        LZLog.d(TAG, "onActivityStopped : activity = " + activity);

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        LZLog.d(TAG, "onActivitySaveInstanceState : activity = " + activity);
    }

}
