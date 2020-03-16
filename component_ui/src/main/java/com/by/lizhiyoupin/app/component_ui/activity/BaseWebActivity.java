package com.by.lizhiyoupin.app.component_ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.by.lizhiyoupin.app.CommonWebConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.bridge.DWebView;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.web.InterceptWebViewClient;
import com.by.lizhiyoupin.app.component_ui.web.JS2NativeProxy;
import com.by.lizhiyoupin.app.component_ui.web.LZBaseWebViewClient;
import com.by.lizhiyoupin.app.component_ui.web.LzWebChromeClient;
import com.by.lizhiyoupin.app.component_ui.web.Native2JSProxy;
import com.by.lizhiyoupin.app.component_ui.weight.PicPickerDialog;
import com.by.lizhiyoupin.app.component_ui.weight.X5WebView;
import com.tencent.smtt.sdk.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/21 10:58
 * Summary:
 */
public class BaseWebActivity extends BaseActivity implements Handler.Callback {
    public static final String TAG = BaseWebActivity.class.getSimpleName();
    protected TextView mBackIv;
    protected TextView mTitleIv;
    protected TextView mRightIv;

    protected static final String VALUE_BACK_BUTTON = "backButton";//返回 按钮
    protected LzWebChromeClient mWebChromeClient;

    protected X5WebView mWebView;
    private Handler mHandler;
    private boolean mfinishWeb;
    protected boolean mWebBackEnable;
    protected String mUrl;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void handleIntent() {
        final Intent intent = getIntent();
        final String url = intent.getStringExtra(CommonWebConst.URL_ADDRESS);
        mUrl = url;
        LZLog.i(TAG, "mUrl==" + mUrl);
    }

    protected void initWebView() {
        initWebView(false);
    }


    /**
     * @param intercept 是否來拦截跳转到淘宝等的url
     */
    protected void initWebView(boolean intercept) {
        initImmersionBar(Color.WHITE, true);
        final X5WebView webView = findViewById(R.id.web_view);

        mWebView = webView;

        webView.setBackgroundColor(Color.TRANSPARENT);
        initViewClient(intercept);
        initProgressBar();
        initChromeClient();
        //js 注入对象
        final JS2NativeProxy nativeProxy = new JS2NativeProxy(getHandler(), this);

        webView.addJavascriptObject(nativeProxy, null);
        webView.getX5WebViewExtension();
        //调试模式
        DWebView.setWebContentsDebuggingEnabled(false);
        //true 禁止Javascript对话框阻塞
        webView.disableJavascriptDialogBlock(true);
    }

    private void initViewClient(boolean intercept) {
        final LZBaseWebViewClient webViewClient;
        if (intercept) {
            webViewClient = new InterceptWebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    // super.onPageFinished(view, url);
                    LZLog.i(TAG, "onPageFinished=" + url);
                    //  view.loadUrl("javascript: var allLinks = document.getElementsByTagName('a'); if (allLinks) {var i;for (i=0; i<allLinks.length; i++) {var link = allLinks[i];var target = link.getAttribute('target'); if (target && target == '_blank') {link.setAttribute('target','_self');link.href = 'newtab:'+link.href;}}}");

                }
            };
        } else {
            webViewClient = new LZBaseWebViewClient();
        }
        mWebView.setWebViewClient(webViewClient);
    }

    private void initProgressBar() {

        /*添加进度条*/
        mProgressBar = new ProgressBar(this, null,
                android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 5);
        mProgressBar.setLayoutParams(layoutParams);

//        mProgressBar.setColor(Color.parseColor("#D60050"));
        Drawable drawable = getResources().getDrawable(
                R.drawable.progress_webview_bg);
        mProgressBar.setProgressDrawable(drawable);
        mProgressBar.setProgress(0);
        mWebView.addView(mProgressBar);
    }

    private void initChromeClient() {
        mWebChromeClient = getWebChromeClient();
        mWebView.setWebChromeClient(mWebChromeClient);

    }


    private LzWebChromeClient getWebChromeClient() {
        LzWebChromeClient webChromeClient = new LzWebChromeClient() {
            //当前正在加载过程中的页面
            String loadingPageUrl;

            // 通知应用打开新窗口
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog,
                                          boolean isUserGesture, Message resultMsg) {
                Log.d(TAG, "onCreateWindow: 新标签中打开网页");
                return true;
            }

            // 通知应用关闭新窗口
            @Override
            public void onCloseWindow(WebView window) {

            }

            @Override
            public void onReceivedTitle(WebView webView, String title) {
                super.onReceivedTitle(webView, title);
                if (!TextUtils.isEmpty(title) && mTitleIv != null) {
                    // 设置标题
                    mTitleIv.setText(title);
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
        return webChromeClient;
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(getMainLooper(), this);
        }
        return mHandler;
    }


    @Override
    protected void onResume() {
        super.onResume();
        Native2JSProxy.onWebViewShow(mWebView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Native2JSProxy.onWebViewHide(mWebView);
    }

    @Override
    public boolean handleMessage(Message msg) {
        //js調用传递的数据
        switch (msg.what) {
            case CommonWebConst.MSG_FINISH_PAGE:
                finish();
                break;
            case CommonWebConst.MSG_WEB_FINISH:
                int finishWeb = (int) msg.obj;
                mfinishWeb = finishWeb == 1;
                break;
            case CommonWebConst.MSG_SET_BACK_BUTTON_LISTENER_ENABLE:
                // 0: native按钮关闭,交给h5处理。1: native按钮打开处理,不交给h5
                mWebBackEnable = "0".equals(msg.obj);
                break;
            case CommonWebConst.MSG_START_LOGIN_ACTIVITY:
                //跳转登录页
                CommonSchemeJump.showLoginActivity(this);
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public void onBackPressed() {

        back();
    }

    protected void back() {
        if (mfinishWeb) {
            finish();
        }
        if (mWebBackEnable) {
            Native2JSProxy.clickButton(mWebView);
            return;
        }

        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }

    }

    @Override
    public void finish() {
        Native2JSProxy.closeWebView(mWebView);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PicPickerDialog picPickerDialog = mWebChromeClient.getPicPickerDialog();
        if (picPickerDialog != null) {
            picPickerDialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PicPickerDialog picPickerDialog = mWebChromeClient.getPicPickerDialog();
        if (picPickerDialog != null) {
            picPickerDialog.onRequestPermissionCallback(requestCode, grantResults);
        }
    }
}
