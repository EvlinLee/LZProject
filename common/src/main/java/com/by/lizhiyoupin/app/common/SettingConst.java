package com.by.lizhiyoupin.app.common;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/23 10:08
 * Summary:
 */
public interface SettingConst  extends CommonConst{
    //设置相关的sp文件夹名
    String PREF_NAME_DEVELOPER_SETTINGS = "developer_setting";
    //是否显示 切换环境item
    String KEY_SHOW_DEVELOPER_SETTING = "key_show_developer_setting";
    //登录
    String ACTION_LOGIN_SUCCESS="action_login_success";
    //登出
    String ACTION_LOGOUT_SUCCESS="action_logout_success";
    /**
     * 切换服务器类型
     */
    String KEY_SERVER_TYPE = "serverType";

    /**
     * 是否显示开发者选项
     */
    String KEY_SHOW_DEVELOPER_SETTINGS = "showDeveloperSettings";
    boolean DEFAULT_SHOW_DEVELOPER_SETTINGS = false;
    /**
     * 支付宝环境
     */
    String KEY_ALIPAY_TYPE = "alipayType";
    String DEFAULT_ALIPAY_TYPE = "0";
    /**
     * 是否使用本地H5资源包
     */
    String KEY_USE_H5_LOCAL_CACHE = "useH5LocalCache";
    boolean DEFAULT_USE_H5_LOCAL_CACHE = true;
    /**
     * 是否把日志输出到文件
     */
    String KEY_SAVE_LOG_TO_FILE = "saveLogToFile";
    boolean DEFAULT_SAVE_LOG_TO_FILE = false;

    /**
     * 获取上次覆盖安装版本
     */
    String KEY_LAST_OVER_INSTALL_VERSION = "key_of_last_over_install_version";

    String KEY_INCOME_RECORD_CACHE="key_income_record_cache";

}
