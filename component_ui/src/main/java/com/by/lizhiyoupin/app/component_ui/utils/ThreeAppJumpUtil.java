package com.by.lizhiyoupin.app.component_ui.utils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.by.lizhiyoupin.app.message_box.MessageToast;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/23 10:40
 * Summary: 跳转打开 其他app
 */
public class ThreeAppJumpUtil {


    /**
     * 跳转到微信
     */
    public static void jump2Wechat(Context context){
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            MessageToast.showToast(context,"检查到您手机没有安装微信，请安装后使用该功能");
        }
    }

    /**
     * 打开指定的QQ聊天页面
     *
     * @param context 上下文
     * @param QQ      QQ号码
     */
    public static boolean jump2QQchat(Context context, String QQ) {
        try {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + QQ;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }
}
