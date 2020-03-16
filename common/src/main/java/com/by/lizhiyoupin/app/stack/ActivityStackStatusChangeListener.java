package com.by.lizhiyoupin.app.stack;


import android.app.Activity;

import java.util.Stack;

public interface ActivityStackStatusChangeListener {

    void onActivityIn(Stack<Activity> activities, Activity activityIn);

    void onActivityExit(Stack<Activity> activities, Activity activityExit);
}
