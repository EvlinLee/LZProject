package com.by.lizhiyoupin.app.component_ui.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.by.lizhiyoupin.app.component_ui.bridge.DWebView;
import com.tencent.smtt.sdk.CookieSyncManager;


/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/21 11:12
 * Summary:
 */
public class X5WebView extends DWebView {

    public X5WebView(Context context) {
        super(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public X5WebView(Context context, AttributeSet arg1) {
        super(context, arg1);
        this.getView().setClickable(true);
    }


    @Override
    public void destroy() {
        try {
            //加载null内容
            loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            stopLoading();
            CookieSyncManager.getInstance().stopSync();
            //清除历史记录
            clearHistory();
            //移除WebView
            ((ViewGroup) getParent()).removeView(this);
            getSettings().setJavaScriptEnabled(false);
            setWebViewClient(null);
            setWebChromeClient(null);
        } catch (Throwable e) {
            // java.lang.Throwable: Error: WebView.destroy() called while still attached!
            e.printStackTrace();
        } finally {
            super.destroy();
        }
    }
}
