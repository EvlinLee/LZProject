package com.by.lizhiyoupin.app.jupsh;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.io.bean.PushMessageBean;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;
import com.by.lizhiyoupin.app.main.MainActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import androidx.core.app.NotificationCompat;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    private static final String NOTIFICATION_PUSH_GROUP_ID = "10";
    private static final String NOTIFICATION_PUSH_GROUP_NAME = "通知";
    private static final String NOTIFICATION_PUSH_ID = "25";
    private static final String NOTIFICATION_PUSH_NAME = "荔枝优品";

    public static final String MESSAGE_CLICK_MYNOTIFICATION = "message_click_mynotification";
    public static final String CLICK_MESSAGE = "click_message";
    private boolean sound = true;
    private int mImportantNotifyId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                Intent intent1 = new Intent(context, PushClickReceiver.class);
                intent1.setAction(MESSAGE_CLICK_MYNOTIFICATION);

                intent1.putExtra(CLICK_MESSAGE, bundle.getString(JPushInterface.EXTRA_EXTRA));
                PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                showNotificationCustom(context, bundle.getString(JPushInterface.EXTRA_TITLE), bundle.getString(JPushInterface.EXTRA_MESSAGE), pi, 10000 + mImportantNotifyId, sound, bundle);
                mImportantNotifyId++;


            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");
                //所有通知消息（非自定义透传）的点击事件（在线消息），自定义的都是自己通知的点击事件内
                startMainActivity(bundle.getString(JPushInterface.EXTRA_EXTRA));

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Logger.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startMainActivity(String extra) {
        LiZhiApplication application = LiZhiApplication.getApplication();
        if (extra != null) {
            try {
                PushMessageBean pushMessageBean = new Gson().fromJson(extra, PushMessageBean.class);
                if (LiZhiApplication.getApplication().getInitMain()) {
                    LZLog.i(TAG,"mainActivity 已经初始化extra");
                    final ISchemeManager scheme =
                            (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                    if (scheme != null && pushMessageBean != null) {
                        scheme.handleUrl(application, pushMessageBean.getMsgUrl());
                    }

                } else {
                    LZLog.i(TAG,"mainActivity 还未初始化extra");
                    Intent intent = new Intent(application, MainActivity.class);
                    intent.setAction(PushActivity.RECEIVED_ACTION);
                    intent.putExtra(PushActivity.KEY_PUSH_START_EXTRAS, pushMessageBean.getMsgUrl());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    application.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            LZLog.i(TAG,"没有extra");
            Intent intent = new Intent(application, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            application.startActivity(intent);
        }
    }

    private void showNotificationCustom(Context mcontext, String title, String content, PendingIntent intent, int id, boolean sound, Bundle bundle) {
        Log.i(TAG, "showNotificationCustom intent : " + intent);
        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
        final NotificationManager notificationManager = (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "SDK > 26");
            NotificationChannelGroup group = new NotificationChannelGroup(NOTIFICATION_PUSH_GROUP_ID, NOTIFICATION_PUSH_GROUP_NAME);
            notificationManager.createNotificationChannelGroup(group);
            builder = new NotificationCompat.Builder(mcontext, NOTIFICATION_PUSH_ID);
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_PUSH_ID, NOTIFICATION_PUSH_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setGroup(NOTIFICATION_PUSH_GROUP_ID);
            notificationManager.createNotificationChannel(channel);
            builder.setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                    .setContentIntent(intent)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setSmallIcon(mcontext.getApplicationInfo().icon);
        } else {
            builder = new NotificationCompat.Builder(mcontext);
            builder.setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                    .setContentIntent(intent)
                    .setPriority(Notification.PRIORITY_HIGH) //设置该通知优先级
                    .setSmallIcon(mcontext.getApplicationInfo().icon);
        }
        if (sound) {
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        }
        final Notification notification = builder.build();
        try {
            notificationManager.notify(id, notification);
            Log.i(TAG, "showNotificationCustom notify success");
        } catch (Exception e) {
            // Lenovo A680 可能会crash
            e.printStackTrace();
        }

    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Logger.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Logger.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

}
