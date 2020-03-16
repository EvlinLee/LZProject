package com.by.lizhiyoupin.app.stack;

import android.app.Activity;

import com.by.lizhiyoupin.app.common.log.LZLog;

import java.util.Stack;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/29 20:15
 * Summary:
 */
public class LoginStack {
    public static final String TAG=LoginStack.class.getSimpleName();

    private static LoginStack instance = null;
    private Stack<Activity> stack;
    public static LoginStack instance() {
        if (instance == null) {
            instance = new LoginStack();
        }
        return instance;
    }
    private LoginStack() {
        stack = new Stack<>();
    }

    /**
     * @param activity 需要添加进栈管理的activity
     */
    public void addActivity(Activity activity) {
        LZLog.d(TAG, "============>>>>>>>>>>>>>>>>  activity in : " + activity.getClass().getSimpleName());
        if (!stack.contains(activity)) {
            stack.add(activity);
        }
    }

    /**
     * @param activity 需要从栈管理中删除的activity
     * @return 是否移除Activity
     */
    public boolean removeActivity(Activity activity) {
        LZLog.d(TAG, "============>>>>>>>>>>>>>>>>  activity out : " + activity.getClass().getSimpleName());
        boolean flag = stack.remove(activity);

        return flag;
    }

    public void finishAllLoginActivity() {
        LZLog.d(TAG, "============>>>>>>>>>>>>>>>>finishAllLoginActivity : " );
        while (!stack.isEmpty()){
            Activity pop = stack.peek();//peek取栈顶值（不出栈）
            stack.remove(pop);
            pop.finish();
        }
    }
}
