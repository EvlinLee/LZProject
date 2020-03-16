package com.by.lizhiyoupin.app.io.entity;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/14 09:24
 * Summary: 登录注册
 */
public class LoginRegisterEntity {
    private String phone;//手机号
    private String smsCode;//短信验证码
    private String inviteCode;//邀请码

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    @Override
    public String toString() {
        return "LoginRegisterEntity{" +
                "phone='" + phone + '\'' +
                ", smsCode='" + smsCode + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                '}';
    }
}
