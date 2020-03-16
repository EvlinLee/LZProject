package com.by.lizhiyoupin.app;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/21 19:15
 * Summary:
 */
public interface CommonWebConst {

    String SOURCE_PAGE = "source_page";//来源页

    String URL_ADDRESS="url_address";
    int MSG_BEFORE_FINISH_PAGE=1;//关闭页面 前
    int MSG_FINISH_PAGE=2;//关闭页面
    int MSG_START_LOGIN_ACTIVITY=3;//打开登录页
    int MSG_GET_USER_INFO=4;//获取用户信息

    int MSG_SET_BACK_BUTTON_LISTENER_ENABLE = 15;    // 设置监听


    int MSG_WEB_FINISH = 27;    //关闭当前web finish


}
