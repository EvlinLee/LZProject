package com.by.lizhiyoupin.app.io.bean;

import java.io.Serializable;

/**
 * data:2019/10/29
 * author:jyx
 * function:
 */
public class PaymentBean implements Serializable {

    /**
     * code : 1
     * msg : 成功
     * data : alipay_sdk=alipay-sdk-java-4.7.11
     * .ALL&app_id=2019072465943529&biz_content=%7B%22body%22%3A%22%E6%B5%8B%E8%AF%95%E7%9A
     * %84Body%22%2C%22out_trade_no%22%3A%22201910291722325211%22%2C%22product_code%22%3A
     * %22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E8%BF%99%E4%B8%AA%E4%B9%9F%E6%98%AF%E6%B5
     * %8B%E8%AF%95%E7%9A%84%E6%A0%87%E9%A2%98%22%2C%22timeout_express%22%3A%2230m%22%2C
     * %22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app
     * .pay¬ify_url=http%3A%2F%2F47.99.89
     * .136%3A7080%2Fgoods-order%2Fv1%2Fstatus&sign=rANxDnuNcX2pREA%2BnqqIPLopOakS
     * %2Bxs2OrUfLM9nZfYR19LXVtrmMtNkoF4y6nQiubhLscAdqWv
     * %2BxlZmygXynUz1ley9VQ6HWoOd0L5PDsE0TfcOeGdVy58q25feTiVU0cjeRFrkbG791ghf127TkzZV7BHQ7nu7P
     * %2FLDXmqpuSX4i6RKFsdaCreht2gz
     * %2FjoqGg6OwowXv9EPrk2Xzwl6dJRwpxj0jyUFfeakUlNJb2kGXGneOEFHFttlxnfHXAfSZ6fBzfKWrTfxTOeXssWW33I3ENrk7xp2VtrnpxJpEWB%2F1yXXN9Q%2BZz9d%2BbP%2BO3FwvYgldSTQyVyf5JbTdbB8zg%3D%3D&sign_type=RSA2×tamp=2019-10-29+17%3A22%3A45&version=1.0
     */

    private int code;
    private String msg;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
