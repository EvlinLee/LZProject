package com.by.lizhiyoupin.app.component_ui.web;

import com.by.lizhiyoupin.app.component_ui.weight.X5WebView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/21 18:57
 * Summary: android 调 js
 */
public class Native2JSProxy {
    public static final String TAG=Native2JSProxy.class.getSimpleName();

    public static void closeWebView(final X5WebView webView){
        if (webView==null){
            return;
        }
        webView.loadUrl("javascript:vueObj.closeWinCb()");
    }

    public static void webViewOpen(final X5WebView webView){
        //首次加载webview
        if (webView==null){
            return;
        }
        webView.callHandler("webViewOpen",new Object[]{});
    }

    /**
     * @param webView
     */
    public static void onWebViewShow(final X5WebView webView) {
        if (webView == null) {
            return;
        }
        webView.callHandler("webViewShow",new Object[]{});
    }

    /**
     * @param webView
     */
    public static void onWebViewHide(final X5WebView webView) {
        if (webView == null) {
            return;
        }

        webView.callHandler("webViewHide",new Object[]{});
    }

    /**
     * 点击按钮
     *
     */
    public static void clickButton(final X5WebView webView) {
        if (webView == null) {
            return;
        }
        webView.callHandler("execWebViewCloseEvent",new Object[]{});
    }
}
