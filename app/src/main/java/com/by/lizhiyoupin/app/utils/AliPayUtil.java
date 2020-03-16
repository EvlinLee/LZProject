package com.by.lizhiyoupin.app.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;

/**
 * data:2019/10/28
 * author:jyx
 * function:支付宝支付
 */
public class AliPayUtil {

   /*private static final int SDK_PAY_FLAG = 0; //9000支付成功  8000支付确认中
    AliPayUtil.alipay((Activity) context, info, handler, 0);//调用实例
    */

    /**
     * @param context
     * @param info   支付信息
     * @param mHandler
     * @param SDK_PAY_FLAG 支付结果
     */
    public static void alipay(final Activity context, String info, final Handler mHandler, final int SDK_PAY_FLAG){
        // 订单信息
        final String orderInfo = info;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                PayTask task = new PayTask(context);
                String result = task.pay(orderInfo, true);
                Message message = new Message();
                message.what = SDK_PAY_FLAG;
                message.obj = result;
                mHandler.sendMessage(message);
            }
        };
        //异步调用
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
