package com.by.lizhiyoupin.app.component_ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebView;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by davidwei on 2017-09-05
 */

public final class WebUtils {
    public static final String TAG=WebUtils.class.getSimpleName();

    public static String addParam(final String url, final String addkey, final String addValue) {
        Uri uri = Uri.parse(url);

        Set<String> keys = new HashSet<>(uri.getQueryParameterNames());
        keys.add(addkey);

        int port = uri.getPort();

        StringBuilder sb;
        if(port != -1) {
            sb = new StringBuilder(uri.getScheme() + "://" + uri.getHost() + ":" + uri.getPort() + uri.getPath());
        } else {
            sb = new StringBuilder(uri.getScheme() + "://" + uri.getHost() + uri.getPath());
        }

        int i=0;
        for(String key : keys) {
            if(i == 0) {
                sb.append("?");
            } else {
                sb.append("&");
            }

            if(key.equalsIgnoreCase(addkey)) {
                sb.append(addkey).append("=").append(addValue);
            } else {
                sb.append(key).append("=").append(uri.getQueryParameter(key));
            }
            i++;
        }

        return sb.toString();
    }


    public static void synCookies(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        com.tencent.smtt.sdk.CookieManager cookieManager = null;
        try {
            CookieSyncManager.createInstance(context);
            cookieManager = CookieManager.getInstance();
        } catch (Throwable e) { // android.content.pm.PackageManager$NameNotFoundException: com.google.android.webview
            e.printStackTrace();
            return;
        }
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        CookieManager finalCookieManager = cookieManager;
    /*    String token = SPUtils.getString(CommonStatic.SERVICE_TOKEN, "");
        String userId = SPUtils.getString(CommonStatic.SERVICE_USER_ID, "");
        finalCookieManager.setCookie(url, CommonStatic.SERVICE_TOKEN + "=" + token);//cookies是在HttpClient中获得的cookie
        finalCookieManager.setCookie(url, CommonStatic.SERVICE_USER_ID + "=" + userId);*/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            com.tencent.smtt.sdk.CookieSyncManager.getInstance().sync();
        } else {
            com.tencent.smtt.sdk. CookieManager.getInstance().flush();
        }

        //获取Cookie
        String mCookie = finalCookieManager.getCookie(url);
        LZLog.i(TAG, "Cookie==" + mCookie);

    }


    /**
     * 清除Cookie
     *
     * @param context
     */
    public static void removeCookie(Context context) {
        com.tencent.smtt.sdk.CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 使用x5的截取webview图
     *
     * @param context
     * @param webView
     * @return
     */
    public static Bitmap captureX5WebViewUnsharp(Context context, WebView webView) {
        if (webView == null) {
            return null;
        }
        if (context == null) {
            context = webView.getContext();
        }
        Picture picture = webView.capturePicture();
        int width = picture.getWidth();
        int height = picture.getHeight();
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            if (width > 0 && height > 0) {
                //创建位图
                //绘制(会调用native方法，完成图形绘制)
                picture.draw(canvas);
            }

        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
