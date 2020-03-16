package com.by.lizhiyoupin.app.component_ui.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by xuebinliu on 2015/8/4.
 */
public class LZBaseWebViewClient extends WebViewClient {
    private static final String TAG = LZBaseWebViewClient.class.getSimpleName();

    @Override
    public void onLoadResource(WebView webView, String url) {
        LZLog.d(TAG, "onLoadResource url = " + url);
        super.onLoadResource(webView, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
        LZLog.d(TAG, "shouldInterceptRequest url = " + url);


        return null;
    }

    /**
     * Give the host application a chance to take over the control when a new
     * url is about to be loaded in the current WebView. If WebViewClient is not
     * provided, by default WebView will ask Activity Manager to choose the
     * proper handler for the url. If WebViewClient is provided, return true
     * means the host application handles the url, while return false means the
     * current WebView handles the url.
     * This method is not called for requests using the POST "method".
     *
     * @param view The WebView that is initiating the callback.
     * @param url  The url to be loaded.
     * @return True if the host application wants to leave the current WebView
     * and handle the url itself, otherwise return false.
     * <p>
     * return super.shouldOverrideUrlLoading(view, url); 这个返回的方法会调用父类方法，也就是跳转至手机浏览器，
     * 平时写webview一般都在方法里面写 webView.loadUrl(url);  然后把这个返回值改成下面的false
     * 返回: return true;  webview处理url是根据程序来执行的。
     * 返回: return false; webview处理url是在webview内部执行。
     * 即返回true表示你已经处理此次请求  即WebView不主动加载该Url。返回false表示有webview自行处理 即WebView主动加载该Url
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LZLog.d(TAG, "shouldOverrideUrlLoading url=" + url);
        final Context context = view.getContext();
        final ISchemeManager schemeManager = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
        if (!TextUtils.equals(view.getUrl(), url) && schemeManager != null && schemeManager.handleWebViewUrl(context, url)
                != CommonConst.PROTOCAL_NOT_HANDLE) {
            LZLog.d(TAG, "shouldOverrideUrlLoading scheme handle successful");
            return true;
        }

        final Uri uri = Uri.parse(url);
        final String scheme = uri.getScheme();
        if (TextUtils.isEmpty(scheme)) {
            view.loadUrl(url);
            return false;
        } else if (Scheme.HTTP.equalsIgnoreCase(scheme) || Scheme.HTTPS.equalsIgnoreCase(scheme)) {
            if (url.contains("https://wx.tenpay.com")) {
                return super.shouldOverrideUrlLoading(view, url);
            }
            //view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);

        } else {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    /**
     * 开始载入页面
     *
     * @param view
     * @param url
     * @param favicon
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        LZLog.d(TAG, "onPageStarted url=" + url);
    }

    /**
     * 页面加载结束
     *
     * @param view
     * @param url
     */
    @Override
    public void onPageFinished(final WebView view, String url) {
        super.onPageFinished(view, url);
        LZLog.d(TAG, "onPageFinished url=" + url);
    }

    @Override
    public void onReceivedError(WebView webView, int i, String s, String s1) {
        super.onReceivedError(webView, i, s, s1);
        LZLog.d(TAG, "onReceivedError s" + s + ", s1=" + s1);
    }

    @Override
    public void onReceivedSslError(WebView webView, SslErrorHandler handler, SslError error) {
        LZLog.d(TAG, "onReceivedSslError error=" + error);
        handler.proceed();
    }
}
