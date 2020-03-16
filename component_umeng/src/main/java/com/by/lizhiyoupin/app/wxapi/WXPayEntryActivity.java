package com.by.lizhiyoupin.app.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_umeng.R;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.weixin.view.WXCallbackActivity;
/*
* data:2019/10/28
* author:jyx
* function:微信支付
* */
public class WXPayEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {
    private static IWXAPI mWxApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_entry);
        mWxApi = WXAPIFactory.createWXAPI(this, CommonConst.WEIXIN_APP_ID, false);//这里填入自己的微信APPID
        mWxApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.d("coyc", "onPayFinish, errCode = " + baseResp.errCode);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCord = baseResp.errCode;
            if (errCord == 0) {
                CommonToast.showToast("支付成功");
                CommonSchemeJump.showActivity(this,"/app/PaySuccessActivity");
                final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                        .getManager(IAccountManager.class.getName());
                finish();
            }else if (errCord==-2){
                finish();
                CommonToast.showToast("支付取消");
            } else{
                finish();
                CommonToast.showToast("支付失败");
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWxApi.handleIntent(intent,this);
    }
}
