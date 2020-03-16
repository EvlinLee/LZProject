package com.by.lizhiyoupin.app;

import android.content.Context;

import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.component_ui.manager.IWechatManager;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * data:2020/1/9
 * author:jyx
 * function:
 */
public class WechatManager implements IWechatManager {
    @Override
    public void jumpWechatApplet(Context context) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, CommonConst.WEIXIN_APP_ID);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = CommonConst.WEIXIN_APPLET_ID; // 填小程序原始id
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开  0是正式 1是开发 2是体验版本
        api.sendReq(req);

    }
}
