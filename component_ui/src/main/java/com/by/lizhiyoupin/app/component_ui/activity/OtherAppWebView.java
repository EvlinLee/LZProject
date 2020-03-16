package com.by.lizhiyoupin.app.component_ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.CommonWebConst;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.utils.StatusBarUtils;
import com.by.lizhiyoupin.app.component_ui.web.LZBaseWebViewClient;
import com.by.lizhiyoupin.app.component_ui.web.LzWebChromeClient;
import com.by.lizhiyoupin.app.component_ui.web.Native2JSProxy;
import com.by.lizhiyoupin.app.component_ui.web.Scheme;
import com.by.lizhiyoupin.app.component_ui.weight.X5WebView;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;
import com.tencent.smtt.sdk.WebView;

import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/26 13:42
 * Summary: 本页面 用于 解析url 重定向跳转 三方app然后关闭本页面，
 * 作为中转页使用
 */
@Route(path = "/ui/OtherAppWebView")
public class OtherAppWebView extends BaseWebActivity implements View.OnClickListener {
    public static final String TAG = OtherAppWebView.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent();

        if (TextUtils.isEmpty(mUrl)) {
            finish();
            return;
        }
        try {
            setContentView(R.layout.activity_common_web_layout);
            StatusBarUtils.setColor(this, Color.WHITE, 0);
        } catch (Exception e) {
            e.printStackTrace(); // 处理webview的java.lang.NullPointerException
            finish();
            return;
        }

        initWebView();
        initView();
        mWebView.loadUrl(mUrl);
    }

    private void initView() {
        mBackIv = findViewById(R.id.actionbar_back_tv);
        mTitleIv = findViewById(R.id.actionbar_title_tv);
        mBackIv.setOnClickListener(this);
    }

    protected void initWebView() {
        initImmersionBar(Color.WHITE, true);
        final X5WebView webView = findViewById(R.id.web_view);
        mWebView = webView;
        webView.setBackgroundColor(Color.TRANSPARENT);
        final LZBaseWebViewClient webViewClient = new LZBaseWebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LZLog.d(TAG, "shouldOverrideUrlLoading url=" + url + ", host=" + view.getUrl());
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
                    return false;
                } else {
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        // TODO: 2019/11/26 注意
                        //跳转其他页面或app后关闭本页面
                        getHandler().sendEmptyMessageDelayed(CommonWebConst.MSG_FINISH_PAGE, 500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        };
        webView.setWebViewClient(webViewClient);

        /*添加进度条*/
        ProgressBar mProgressBar = new ProgressBar(this, null,
                android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 5);
        mProgressBar.setLayoutParams(layoutParams);

        mProgressBar.setProgress(0);
        mWebView.addView(mProgressBar);

        webView.setWebViewClient(webViewClient);

        final LzWebChromeClient webChromeClient = new LzWebChromeClient() {
            //当前正在加载过程中的页面
            String loadingPageUrl;

            @Override
            public void onReceivedTitle(WebView webView, String s) {
                //这里可以设置title
                if (mTitleIv != null) {
                    mTitleIv.setTag(s);
                }
            }

            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                //页面内部跳转，WebViewClient#shouldOverrideUrlLoading 方法不一样调用
                LZLog.d(TAG, "onProgressChanged : " + newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    if (mProgressBar.getVisibility() == View.GONE) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);
                }

                loadingPageUrl = webView.getUrl();
                super.onProgressChanged(webView, newProgress);
            }
        };
        webView.setWebChromeClient(webChromeClient);
        mWebChromeClient = webChromeClient;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.actionbar_back_tv) {
            if (mWebBackEnable){
                Native2JSProxy.clickButton(mWebView);
                return;
            }
            finish();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
