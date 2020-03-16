package com.by.lizhiyoupin.app.io.manager;


import com.by.lizhiyoupin.app.io.bean.UserInfoBean;

/**
 *   final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
 *                                     .getManager(IAccountManager.class.getName());
 */
public interface IAccountManager {
    boolean isLogined();
    long getAccountId();
    String getUserPhone();
    String getUserToken();
    void removeAccountInfo();
    UserInfoBean getAccountInfo();
    void saveAccountInfo(final UserInfoBean userInfoBean);
    void onLogout();

    /**
     * 登录保存数据及修改登录状态
     * @param userInfoBean
     */
    void onLogin(UserInfoBean userInfoBean);
}
