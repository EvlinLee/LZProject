package com.by.lizhiyoupin.app.io;

import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;

import java.io.Serializable;


public class BaseBean<T> implements Serializable {
    public static final String CODE_FAIL = "0";//返回失败
    public static final String CODE_SUCCESS = "1";//成功
    public static final String CODE_ERROR = "2";//接口失败，请重试
    public static final String CODE_TOKEN_ERROR = "202";//身份token已过期
    public static final String CODE_USER_UNLOGIN = "204";//会员未登录
    public static final String CODE_VERIFICATION_OVERDUE = "130";//验证码已过期
    public static final String CODE_VERIFICATION_ERROR = "131";//验证码错误
    public static final String CODE_PRODUCT_NO_ERROR = "301";// 商品不存在
    public static final String CODE_PRODUCT_NO_BUT_JUMP_ERROR = "302";// 商品不存在,需要跳转到淘宝天猫等其他app

    public static final String CODE_PRODUCT_RED_NO_DIALOG_SUCCESS = "601";// 签到弹框已弹过，不需要再次弹出

    public String code;//返回码
    public String msg;//返回信息
    public T data;//成功返回内容;

    public T getResult() {
        return data;
    }

    public void setResult(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

    public boolean success() {
        if (CODE_TOKEN_ERROR.equals(code)){
            //token 已过期
            IAccountManager accountManager=(IAccountManager) ComponentManager.getInstance()
                                                     .getManager(IAccountManager.class.getName());
            if (accountManager.isLogined()){
                accountManager.onLogout();
            }
            return false;
        }
        return CODE_SUCCESS.equals(code);
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}