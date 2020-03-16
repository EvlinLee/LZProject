package com.by.lizhiyoupin.app.stack;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.by.lizhiyoupin.app.common.log.LZLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Activity对象栈
 */
@SuppressWarnings("ALL")
public class ActivityStack {
    public static final String TAG=ActivityStack.class.getSimpleName();

    private static final String MAIN_PAGE_CLASS_NAME = "com.by.lizhiyoupin.app.main.MainActivity";
    private static final String PRECISE_DETAIL_CLASS_NAME = "com.by.lizhiyoupin.app.detail.activity.PreciseDetailActivity";

    private static ActivityStack instance = null;

    private Stack<Activity> stack;
    private Stack<Activity> mPreciseDetailActivities;
    private List<ActivityStackStatusChangeListener> callbacks = new ArrayList<>();

    private static Context appContext;

    public static ActivityStack instance() {
        if (instance == null) {
            instance = new ActivityStack();
        }
        return instance;
    }

    private ActivityStack() {
        stack = new Stack<>();
        mPreciseDetailActivities=new Stack<>();
    }

    public Stack<Activity> getStack() {
        return stack;
    }

    public static void registerActivityStackStatusChangeListener(ActivityStackStatusChangeListener callback) {
        if (!instance().callbacks.contains(callback)) {
            instance().callbacks.add(callback);
        }
    }

    public static void unRegisterActivityStackStatusChangeListener(ActivityStackStatusChangeListener callback) {
        instance().callbacks.remove(callback);
    }

    public static void clearActivityStackStatusChangeListener() {
        instance().callbacks.clear();
    }

    public static void closeLatestActivity() {
        Activity appCompatActivity = instance().currentActivity();
        if (appCompatActivity != null) {
            appCompatActivity.finish();
        }
    }

    /**
     * @param activity 需要添加进栈管理的activity
     */
    public void addActivity(Activity activity) {
        LZLog.d(TAG, "============>>>>>>>>>>>>>>>>  activity in : " + activity.getClass().getSimpleName());
        if (!stack.contains(activity)) {
            stack.add(activity);
            if (PRECISE_DETAIL_CLASS_NAME.equals(activity.getClass().getName())){
                //商品详情页 管理 最多给与2个
                mPreciseDetailActivities.add(activity);
                while (mPreciseDetailActivities.size()>2){
                    mPreciseDetailActivities.get(0).finish();
                }
            }
        }
        for (ActivityStackStatusChangeListener listener : callbacks) {
            listener.onActivityIn(stack, activity);
        }
    }

    /**
     * @param activity 需要从栈管理中删除的activity
     * @return 是否移除Activity
     */
    public boolean removeActivity(Activity activity) {
        LZLog.d(TAG, "============>>>>>>>>>>>>>>>>  activity out : " + activity.getClass().getSimpleName());
        boolean flag = stack.remove(activity);
        if (PRECISE_DETAIL_CLASS_NAME.equals(activity.getClass().getName())){
            mPreciseDetailActivities.remove(activity);
        }
        if (flag) {
            for (ActivityStackStatusChangeListener listener : callbacks) {
                listener.onActivityExit(stack, activity);
            }
        }
        return flag;
    }


    /**
     * @param activity 查询指定activity在栈中的位置，从栈顶开始
     * @return Activity所在栈位置
     */
    public int searchActivity(Activity activity) {
        return stack.search(activity);
    }

    /**
     * finish掉某个Activity
     * @param activity 将指定的activity从栈中删除然后finish()掉
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            stack.remove(activity);
            activity.finish();
            for (ActivityStackStatusChangeListener listener : callbacks) {
                listener.onActivityExit(stack, activity);
            }
        }
    }

    /**
     * @param activity 将指定类名的activity从栈中删除并finish()掉
     */
    public void finishActivityClass(Class<Activity> activity) {
        if (activity != null) {
            Iterator<Activity> iterator = stack.iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();
                if (next.getClass().equals(activity)) {
                    iterator.remove();
                    finishActivity(next);
                }
            }
        }
    }

    /**
     * @param activity 将指定类名的activity从栈中删除并finish()掉
     */
    public Activity finishActivityByName(CharSequence activityName) {
        Activity appCompatActivity = null;
        if (!TextUtils.isEmpty(activityName)) {
            Iterator<Activity> iterator = stack.iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();
                if (activityName.equals(next.getClass().getName())) {
                    appCompatActivity = next;
                }
            }
        }
        return appCompatActivity;
    }

    /**
     * 销毁所有的activity
     */
    public void finishAllActivity() {
        while (!stack.isEmpty()) {
            Activity activity = stack.pop();
            activity.finish();
            for (ActivityStackStatusChangeListener listener : callbacks) {
                listener.onActivityExit(stack, activity);
            }
        }
    }

    public static Activity currentActivity() {
        try {
            return instance().getCurrentActivity();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前栈顶Activity
     * @return
     */
    private Activity getCurrentActivity() throws NullPointerException {
        if (!stack.isEmpty()) {
            return stack.peek();
        }
        return null;
    }

    /**
     * 返回MainActivity对象
     * @return
     */
    public Activity getMainActivity() throws NullPointerException {
        Iterator<Activity> iterator = stack.iterator();
        while (iterator.hasNext()) {
            Activity next = iterator.next();
            if (next.getClass().getName().equals(MAIN_PAGE_CLASS_NAME)) {
                return next;
            }
        }
        return null;
    }

    /**
     * 是否在主界面
     * @return
     */
    public static boolean isInMainPage() {
        Activity currentActivity = instance().currentActivity();
        return currentActivity != null && currentActivity.getClass().getName().equals(MAIN_PAGE_CLASS_NAME);
    }

    public static boolean isActivityDestoryed(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity == null || activity.isDestroyed()) {
                return true;
            }
        } else {
            if (activity == null || activity.isFinishing()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isActivityDestoryed(Context context) {
        if (context instanceof Activity) {
            return isActivityDestoryed((Activity) context);
        } else {
            return context == null;
        }
    }

    public static void setAppContext(Context context) {
        appContext = context;
    }

    /**
     * 获取Application的Context
     * @return
     */
    public static Context getAppContext() {
        return appContext;
    }
}
