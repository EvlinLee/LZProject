package com.by.lizhiyoupin.app.io.bean;

import java.util.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/22 13:51
 * Summary:
 */
public class AccountInfo extends Observable {
    private UserInfoBean mUserInfoBean;
    private boolean isLogin;



    public UserInfoBean getUserInfoBean() {
        return mUserInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        mUserInfoBean = userInfoBean;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
