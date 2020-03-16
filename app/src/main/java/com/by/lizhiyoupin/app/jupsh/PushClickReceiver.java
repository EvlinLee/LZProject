package com.by.lizhiyoupin.app.jupsh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.io.bean.PushMessageBean;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;
import com.by.lizhiyoupin.app.main.MainActivity;
import com.google.gson.Gson;

/**
 * data:2019/12/3
 * author:jyx
 * function:
 * 自定义消息 点击事件
 */
public class PushClickReceiver extends BroadcastReceiver {
    public static final String TAG = PushClickReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (MyReceiver.MESSAGE_CLICK_MYNOTIFICATION.equals(intent.getAction())) {
            LZLog.i(TAG, "自定义消息 点击事件");
            String extra = intent.getStringExtra(MyReceiver.CLICK_MESSAGE);
            startMainActivity(extra);
        }
    }

    private void startMainActivity(String extra) {
        LiZhiApplication application = LiZhiApplication.getApplication();
        if (extra != null) {
            try {
                PushMessageBean pushMessageBean = new Gson().fromJson(extra, PushMessageBean.class);
                if (LiZhiApplication.getApplication().getInitMain()) {
                    LZLog.i(TAG, "mainActivity 已经初始化，extra");
                    final ISchemeManager scheme =
                            (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                    if (scheme != null && pushMessageBean != null) {
                        scheme.handleUrl(application, pushMessageBean.getMsgUrl());
                    }

                } else {
                    LZLog.i(TAG, "mainActivity 还未初始化，extra");
                    Intent intent = new Intent(application, MainActivity.class);
                    intent.setAction(PushActivity.RECEIVED_ACTION);
                    intent.putExtra(PushActivity.KEY_PUSH_START_EXTRAS, pushMessageBean.getMsgUrl());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    application.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LZLog.i(TAG, "没有extra");
            Intent intent = new Intent(application, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            application.startActivity(intent);
        }
    }

}
