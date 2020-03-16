package com.by.lizhiyoupin.app.component_ui.web;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.by.lizhiyoupin.app.CommonWebConst;

import androidx.annotation.Nullable;


/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/21 19:34
 * Summary:
 */
public class CommonWebJump {

    public static void showCommonWebActivity(Context context, String url) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebConst.URL_ADDRESS, url);
            ARouter.getInstance().build("/ui/CommonWebActivity").with(bundle).navigation(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开会拦截跳转其他app的webview
     * @param context
     * @param url
     */
    public static void showInterceptOtherWebActivity(Context context, String url) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebConst.URL_ADDRESS, url);
            ARouter.getInstance().build("/ui/InterceptOtherWebActivity").with(bundle).navigation(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转拼多多等其他app
     *
     * @param context
     * @param url
     */
    public static void showOtherAppWebView(Context context, String url) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebConst.URL_ADDRESS, url);
            ARouter.getInstance().build("/ui/OtherAppWebView").with(bundle).navigation(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 百川淘宝
     * 拼多多
     *
     * @param context
     * @param bundle
     */
    public static void showAutoWebActivity(@Nullable final Context context, Bundle bundle) {
        try {
            ARouter.getInstance().build("/ui/AutoWebActivity").with(bundle).navigation(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
