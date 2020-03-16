package com.by.lizhiyoupin.app.message_box;

import android.content.Context;
import android.text.TextUtils;

import com.hjq.toast.ToastUtils;

/**
 * 封装自定义Toast对象,
 * 避免同时弹出多个Toast，可定制图标
 */
public class MessageToast {


    public static void showToast(Context context, CharSequence info){
        showToast(context, info, -1);
    }

    public static void showToast(Context context, CharSequence info, int imgRes){
        if(TextUtils.isEmpty(info)) {
            return;
        }
        //使用ToastUtils 提升体验
        ToastUtils.show(info);
    }

    /**
     *
     * @param context
     * @param info
     * @param gravity 位置 Gravity.CENTER   Gravity.BOTTOM等
     */
    public static void showToastBottom(Context context, CharSequence info,int gravity){
        ToastUtils.show(info);
    }
}
