package com.by.lizhiyoupin.app.component_ui.activity;

import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.PackageUtil;
import com.by.lizhiyoupin.app.component_sdk.AliSdkManager;
import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.manager.IWechatManager;
import com.by.lizhiyoupin.app.component_ui.utils.StatusBarUtils;
import com.by.lizhiyoupin.app.component_ui.utils.WebUtils;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.component_ui.web.Native2JSProxy;
import com.by.lizhiyoupin.app.component_ui.web.Scheme;

import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/3 16:02
 * Summary: 拦截 淘宝等重定向
 */
@Route(path = "/ui/InterceptOtherWebActivity")
public class InterceptOtherWebActivity extends BaseWebActivity implements View.OnClickListener {
    public static final String TAG = InterceptOtherWebActivity.class.getSimpleName();

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
        //网页中的视频，上屏幕的时候，可能出现闪烁的情况，需要如下设置：Activity在onCreate时需要设置
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        initView();

        if ("2".equals(Uri.parse(mUrl).getQueryParameter(Scheme.TAG_WEB_NOT_JUMP_TAO))) {
            mUrl = mUrl.replaceAll(Scheme.TAG_WEB_NOT_JUMP_TAO, "lzyp_over");
            jumpTaoOrH5(mUrl);
            return;
        } else if ("3".equals(Uri.parse(mUrl).getQueryParameter(Scheme.TAG_WEB_NOT_JUMP_TAO))) {
            final IWechatManager wechatManager = (IWechatManager) ComponentManager.getInstance().getManager(IWechatManager.class.getName());
            if (wechatManager != null) {
                wechatManager.jumpWechatApplet(this);
                finish();
            }
        }

        initWebView(true);
        WebUtils.synCookies(this, mUrl);
        mWebView.loadUrl(mUrl);

    }


    private void jumpTaoOrH5(String url) {
        //确定,直接跳转淘宝天猫
        if (PackageUtil.checkAppInstalled(this, PackageUtil.PackageType.TAO_BAO) || PackageUtil.checkAppInstalled(this, PackageUtil.PackageType.TIAN_MAO)) {
            LZLog.i(TAG, "直接跳转淘宝");
            AliSdkManager.showGoods(this, url, null);
        } else {
            LZLog.i(TAG, "跳转淘宝h5,这个不要去拦截，因为在这个回调里的都是找不到的商品数据");
            CommonWebJump.showCommonWebActivity(this, url);
        }
        finish();
    }

    private void initView() {
        mBackIv = findViewById(R.id.actionbar_back_tv);
        mTitleIv = findViewById(R.id.actionbar_title_tv);
        mRightIv = findViewById(R.id.actionbar_right_tv);
        mBackIv.setOnClickListener(this);
        mRightIv.setOnClickListener(this);
        mRightIv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.actionbar_refresh,0,0,0);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.actionbar_back_tv) {
            if (mWebBackEnable) {
                Native2JSProxy.clickButton(mWebView);
                return;
            }
            finish();
        }else if (v.getId()==R.id.actionbar_right_tv){
            mWebView.loadUrl(mUrl);
        }

    }


}
