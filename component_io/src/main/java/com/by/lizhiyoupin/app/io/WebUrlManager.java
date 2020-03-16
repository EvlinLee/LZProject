package com.by.lizhiyoupin.app.io;

import com.by.lizhiyoupin.app.common.log.LZLog;

import java.net.URLEncoder;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/6 11:16
 * Summary:
 */
public class WebUrlManager {

    public static final String TAG = WebUrlManager.class.getSimpleName();
    public static final String TEST_WEB_IP = "http://test.web.lizhiyoupin.com/";
    public static final String PROD_WEB_IP = "http://web.lizhiyoupin.com/";


    //下载地址 需要在后面添加用户code
    private static final String DOWN_LOAD_H5 = "home/download?code=%s";

    //商学院 文章点击跳转地址
    private static final String BUSINESS_ARTICLE_URL = "home/business-school/";
    //用户注册服务协议
    private static final String LOGIN_REGISTER_SERVICE_URL = "login/arguments";
    //隐私政策
    private static final String LOGIN_PRIVACY_PROTOCOL_URL = "login/privacy";

    //新手教程
    private static final String NEWSUSER_TUTORIAL_URL = "help";

    //用户注册服务协议
    private static final String USER_RESGIN_SERVICE_URL = "login/arguments";

    //隐私政策
    private static final String USER_PRIVACY_SERVICE_URL = "login/privacy";

    //联系客服
    private static final String USER_CONTAC_HELP_URL = "help/contact";

    //注册分享链接
    private static final String RESIGN_URL = "goods/login?invite_code=%s";

    //自由职业者服务协议
    private static final String FREELANCE_SERVICE_URL = "login/service";

    //荔枝物料
    private static final String LITCHI_MATERIALS_URL = "tao/create-ticket";
    //荔枝权益
    private static final String LITCHI_POWER_URL = "goods/power?title=";
    //种草
    private static final String PRECISE_DETAIL_ZC_URL = "goods/app-detail?articleType=%s&articleId=%s";
    //优惠券配置
    private static final String USER_COUPURL_URL = "coupon/create-ticket";

    //签到邀请好友注册地址
    private static final String SIGN_IN_SHARE_URL="goods/login?invite_code=%s&is_zero=2";

    //淘礼金
    private static final String USER_TAO_URL = "tao/gift-money";

    //优惠券
    private static final String USER_COUPON_URL = "coupon/gift-money";

    private static String getBaseWebUrl() {
        if (IPManager.getInstance().isTestIp()) {
            return TEST_WEB_IP;
        } else if (IPManager.getInstance().isProdIp()) {
            return PROD_WEB_IP;
        }
        return PROD_WEB_IP;
    }

    /**
     * app下载地址
     *
     * @param code 邀请码
     * @return
     */
    public static String getDownLoadH5(String code) {
        if (code == null) {
            code = "";
        }
        return getBaseWebUrl() + String.format(DOWN_LOAD_H5, code);
    }

    /**
     * 商学院 文章点击跳转地址
     *
     * @param id 文章主键
     * @return
     */
    public static String getBusinessArticleUrl(long id) {
        return getBaseWebUrl() + BUSINESS_ARTICLE_URL + id;
    }


    /**
     * 用户注册服务协议
     *
     * @return
     */
    public static String getLoginRegisterServiceUrl() {
        return getBaseWebUrl() + LOGIN_REGISTER_SERVICE_URL;
    }

    /**
     * 隐私政策
     *
     * @return
     */
    public static String getLoginPrivacyProtocolUrl() {
        return getBaseWebUrl() + LOGIN_PRIVACY_PROTOCOL_URL;
    }

    /**
     * 新手教程
     *
     * @return
     */
    public static String getNewsUserTutorialUrl() {
        return getBaseWebUrl() + NEWSUSER_TUTORIAL_URL;
    }

    /**
     * 用户注册服务协议
     *
     * @return
     */
    public static String getUserResginServicUrl() {
        return getBaseWebUrl() + USER_RESGIN_SERVICE_URL;
    }

    /**
     * 隐私政策
     *
     * @return
     */
    public static String getUserPrivacyServiceUrl() {
        return getBaseWebUrl() + USER_PRIVACY_SERVICE_URL;
    }

    /**
     * 自由职业者服务协议
     *
     * @return
     */
    public static String getFreelanceServiceUrl() {
        return getBaseWebUrl() + FREELANCE_SERVICE_URL;
    }

    /**
     * 注册分享链接
     *
     * @return
     */
    public static String getRegisterShareUrl(String code) {
        if (code == null) {
            code = "";
        }
        return getBaseWebUrl() + String.format(RESIGN_URL, code);
    }

    /**
     * 联系客服
     *
     * @return
     */
    public static String getUserContactHelpUrl() {
        return getBaseWebUrl() + USER_CONTAC_HELP_URL;
    }

    /**
     * 荔枝物料
     *
     * @return
     */
    public static String getLitchiMaterialsUrl() {
        return getBaseWebUrl() + LITCHI_MATERIALS_URL;
    }

    /**
     * 荔枝权益
     *
     * @return
     */
    public static String getLitchiPowerUrl(String title, String url) {
        return getBaseWebUrl() + LITCHI_POWER_URL + title + "&img_url=" + url;
    }
    /**
     * 荔枝权益
     *
     * @return
     */
    public static String getUserCoupurlUrl() {
        return getBaseWebUrl() + USER_COUPURL_URL;
    }
    /**
     * 种草详情
     * @param articleType
     * @param articleId
     * @return
     */
    public static String getZongCaoUrl(int articleType, long articleId) {
        return getBaseWebUrl() + String.format(PRECISE_DETAIL_ZC_URL, articleType, articleId);
    }

    /**
     * 签到分享url
     * @param code 邀请码
     * @return
     */
    public static String getSignInShareUrl(String code){
        if (code == null) {
            code = "";
        }
        return getBaseWebUrl()+String.format(SIGN_IN_SHARE_URL,code);
    }






    /**
     * 淘礼金
     *
     * @return
     */
    public static String getLitchitaosUrl() {
        return getBaseWebUrl() + USER_TAO_URL;
    }

    /**
     * 优惠券
     *
     * @return
     */
    public static String getLitchiCouponUrl() {
        return getBaseWebUrl() + USER_COUPON_URL;
    }

    public static String appendParams(final String url, final String paramsFormat, final Object... params) {
        String destUrl = null;
        if (url.indexOf('?') > -1) {
            destUrl = url + '&' + getParams(paramsFormat, params);
        } else {
            destUrl = url + '?' + getParams(paramsFormat, params);
        }
        LZLog.d(TAG, "getUrl : " + destUrl);
        return destUrl;
    }

    private static String getParams(final String paramsFormat, final Object... params) {
        if (params == null) {
            return paramsFormat;
        }

        final int length = params.length;
        for (int i = 0; i < length; i++) {
            try {
                params[i] = URLEncoder.encode(params[i].toString(), "UTF8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return String.format(paramsFormat, params);
    }
}
