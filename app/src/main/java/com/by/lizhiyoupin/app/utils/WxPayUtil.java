package com.by.lizhiyoupin.app.utils;

import android.content.Context;

import com.by.lizhiyoupin.app.common.CommonConst;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.util.Collections.sort;

/**
 * data:2019/10/28
 * author:jyx
 * function:微信支付
 */
public class WxPayUtil {

   /* private WxaPayUtils wxaPayUtils;
    wxaPayUtils.wxPay(appId, partnerid, prepayid, nonceStr, timeStamp, packageValue, sign);//调用实例
*/
    private IWXAPI api;
    private PayReq req;
    private volatile static WxPayUtil sWxPayUtil;
    public static WxPayUtil getInstance(Context context){
        if (sWxPayUtil==null){
            sWxPayUtil=new WxPayUtil(context);
        }
        return sWxPayUtil;
    }
    private WxPayUtil(Context context) {
        api = WXAPIFactory.createWXAPI(context, CommonConst.WEIXIN_APP_ID, false);//这里填入自己的微信APPID
        api.registerApp(CommonConst.WEIXIN_APP_ID);//填写自己的APPID，注册本身APP

    }


    public void wxPay(String appId, String partnerid,String prepayid,String nonceStr,String timeStamp,String packageValue,String sign) {
        req = new PayReq();//PayReq就是订单信息对象
        //给req对象赋值
        req.appId =appId;//APPID
        req.partnerId = partnerid;//    商户号
        req.prepayId = prepayid;//  预付款ID
        req.nonceStr = nonceStr;//随机数
        req.timeStamp =  timeStamp;//时间戳
        req.packageValue = "Sign=WXPay";//固定值Sign=WXPay
        req.sign = sign;//签名
        api.sendReq(req);
    }


    private String getRoundString() {
        Random random = new Random();
        return random.nextInt(10000) + "";
    }

    private String getTimeStamp() {
        return new Date().getTime() / 10 + "";
    }

    private String getSign() {
        Map<String, String> map = new HashMap<>();
        map.put("appid", req.appId);
        map.put("partnerid", req.partnerId);
        map.put("prepayid", req.prepayId);
        map.put("package", req.packageValue);
        map.put("noncestr", req.nonceStr);
        map.put("timestamp", req.timeStamp);

        ArrayList<String> sortList = new ArrayList<>();
        sortList.add("appid");
        sortList.add("partnerid");
        sortList.add("prepayid");
        sortList.add("package");
        sortList.add("noncestr");
        sortList.add("timestamp");
        sort(sortList);
        String md5 = "";
        int size = sortList.size();
        for (int k = 0; k < size; k++) {
            if (k == 0) {
                md5 += sortList.get(k) + "=" + map.get(sortList.get(k));
            } else {
                md5 += "&" + sortList.get(k) + "=" + map.get(sortList.get(k));
            }
        }
        String stringSignTemp = md5 + "&key=商户密钥";//这里填写自己的商户密钥，所以说如果签名工作实在服务端完成的，商户密钥在APP端是用不到的
        String sign = MD5.Md5(stringSignTemp).toUpperCase();
        return sign;
    }
}
