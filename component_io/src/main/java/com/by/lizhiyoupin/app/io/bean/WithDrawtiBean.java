package com.by.lizhiyoupin.app.io.bean;

/**
 * data:2019/11/20
 * author:jyx
 * function:
 */
public class WithDrawtiBean {

    public WithDrawtiBean(String alipay, String extractType, String amount, String phone, String code) {
        this.alipay = alipay;
        this.extractType = extractType;
        this.amount = amount;
        this.phone = phone;
        this.code = code;
    }

    /**
     * alipay : true
     * extractType : 3
     * amount : 105
     * phone : 15600716026
     * code : 953225
     */

    private String alipay;
    private String extractType;
    private String amount;
    private String phone;
    private String code;

    public String isAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getExtractType() {
        return extractType;
    }

    public void setExtractType(String extractType) {
        this.extractType = extractType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
