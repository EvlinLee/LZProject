package com.by.lizhiyoupin.app.component_ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.component_sdk.AliSdkManager;
import com.by.lizhiyoupin.app.component_ui.R;

import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/26 15:31
 * Summary: 用于 阿里百川等SDK授权
 */
@Route(path = "/ui/AutoWebActivity")
public class AutoWebActivity extends BaseActivity {
    public static final String TAG = AutoWebActivity.class.getSimpleName();
    private String url;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_layout);
        initImmersionBar(Color.WHITE,true);
        Intent intent = getIntent();
        url = intent.getStringExtra(CommonConst.KET_COUPON_CLICK_URL);
        initView();
    }

    private void initView() {
        WebView webView = findViewById(R.id.webView);
        if (TextUtils.isEmpty(url)) {
            LZLog.w(TAG, "跳转url不能为空");
            finish();
            return;
        }

        try {
            final Uri uri = Uri.parse(url);
            String queryParameter = uri.getQueryParameter("relationId");
            if (!TextUtils.isEmpty(queryParameter)) {
                LZLog.i(TAG, "直接跳转淘宝");
                AliSdkManager.showGoods(this, url, null);
                finish();
                return;
            }else {
                //先去授权
                LZLog.i(TAG, "先去授权"+url);
                new AliSdkManager().loginTaoBao(this, webView, url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
