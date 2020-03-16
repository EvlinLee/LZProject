package com.by.lizhiyoupin.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.activity.BaseWebActivity;
import com.by.lizhiyoupin.app.component_ui.web.Native2JSProxy;

import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/25 10:01
 * Summary: 协议页
 */
public class UserAgreementActivity extends BaseWebActivity implements View.OnClickListener {
    private TextView aboutTitle;
    private TextView aboutIntro;
    private String mType;
    private TextView mBack;
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        DeviceUtil.setStatusBarWordsColor(this,true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement_layout);
        Intent intent = getIntent();
        mType = intent.getStringExtra(CommonConst.KEY_AGREEMENT_TYPE);
        if (TextUtils.isEmpty(mType)){
            mType=CommonConst.KEY_AGREEMENT_TYPE_OF_USER;
        }
        initView();
    }

    private void initView() {
        mBack = findViewById(R.id.actionbar_back_tv);
        mBack.setText("");
        mBack.setOnClickListener(this);
        aboutTitle=findViewById(R.id.about_title) ;
        aboutIntro=findViewById(R.id.about_intro) ;

        aboutTitle.setText(getResources().getString(R.string.about_agreement_of_user));

        String intro="<p>最新版本生效日期：9102年08月19日</p>"
                +"<p>甲、乙双方根据中华人民共和国法律、法规等相关规定，本着诚信、互惠、规范的原则，经友好协商，就运营商接入荔枝优品平台之相关合作事项，达成一致协议。</p>"
                +"<p>甲、乙双方根据中华人民共和国法律、法规等相关规定，本着诚信、互惠、规范的原则，经友好协商，<b><font color='#111111'>就运营商接入荔枝优品平台之相关合作事项</font></b>，达成一致协议。</p>"
                +"<p>乙方有权根据需要不时地制定、修改本协议或各类规则，如本协议有任何变更，<b><font color='#111111'>乙方将在网站上以公示形式通知予商户</font></b>。如商户不同意相关变更，" +
                "<b><font color='#111111'>必须立即以书面通知的方式终止本协议并同时停止使用众创空间企业服务平台</font></b>。任何修订和新规则一经在众创空间企业服务平台上公布即自动生效，成为本协议的一部分。</p>"
                +"<p>甲、乙双方根据中华人民共和国法律、法规等相关规定，本着诚信、互惠、规范的原则，经友好协商，<b><font color='#111111'>就运营商接入荔枝优品平台之相关合作事项</font></b>，达成一致协议。</p>";

        aboutIntro.setText(Html.fromHtml(intro));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                if (mWebBackEnable){
                    Native2JSProxy.clickButton(mWebView);
                    return;
                }
                finish();
                break;
            default:
                break;
        }
    }
}
