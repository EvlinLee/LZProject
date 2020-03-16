package com.by.lizhiyoupin.app.component_ui.activity;

import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.utils.StatusBarUtils;
import com.by.lizhiyoupin.app.component_ui.utils.WebUtils;
import com.by.lizhiyoupin.app.component_ui.web.Native2JSProxy;

import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/25 11:20
 * Summary:
 */
@Route(path = "/ui/CommonWebActivity")
public class  CommonWebActivity extends BaseWebActivity implements View.OnClickListener {

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
        initWebView();
        WebUtils.synCookies(this, mUrl);
        mWebView.loadUrl(mUrl);
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
            if (mWebBackEnable){
                Native2JSProxy.clickButton(mWebView);
                return;
            }
            finish();
        }else if (v.getId()==R.id.actionbar_right_tv){
            mWebView.loadUrl(mUrl);
        }
    }


}
