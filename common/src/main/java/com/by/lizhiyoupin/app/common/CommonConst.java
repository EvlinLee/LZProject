package com.by.lizhiyoupin.app.common;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/20 16:21
 * Summary:
 */
public interface CommonConst {

    String CHARTSET_UTF8 = "UTF8";
    String KEY_CHANNEL_ID="UMENG_CHANNEL";//渠道
    String PREF_NAME_SETTING="pref_name_setting";//sp
    String WEIXIN_APP_ID="wx829af278a53c870e";//appid
    String WEIXIN_APP_SECRTE="881c27d63552b88e55c825a456ae7017";
    String WEIXIN_APPLET_ID="gh_ad8d5f0e0e47";//微信小程序appid

    String KEY_USER_INFO="key_user_info";

    int PROTOCAL_NOT_HANDLE = -1;
    int PROTOCAL_HANDLE_SUCCESS = 0;
    int PROTOCAL_ERROR_LITCHI_NOT_SUPPORT = 1;
    int PROTOCAL_ERROR_URL_BLANK = 2;

    String KEY_AGREEMENT_TYPE="key_agreement_type";//协议 类型
    String KEY_AGREEMENT_TYPE_OF_USER="key_agreement_type_of_user";//协议--用户协议
    String KEY_AGREEMENT_TYPE_OF_PRIVACY="key_agreement_type_of_privacy";//协议--隐私政策
    //scheme跳转失败
    String ACTION_LITCHI_PROTOCAL_ERROR="action_litchi_protocal_error";
    String KEY_MAIN_PAGE_INDEX="mainTabIndex";
    String KEY_MAIN_SUBPAGE_INDEX="mainSubPageIndex";
    String KEY_CHILD_PAGE_INDEX="childPageIndex";



    //Splash
    String KEY_SPLASH_TITLE="key_splash_title";
    String KEY_SPLASH_DESC="key_splash_secs";
    String KEY_SPLASH_IMG="key_splash_img";
    String KEY_SPLASH_END="key_splash_end";
    //登录的activity 请求code
    int LOGIN_REQUEST_CODE=1630;
    //登录方式
    String KEY_LOGIN_TYPE_FROM="key_login_type_from";
    //微信token
    String KEY_WECHAT_TOKEN="key_we_chat_access_token";
    //微信id
    String KEY_WECHAT_OPID="key_we_chat_opid";
    //手机号
    String KEY_LOGIN_PHONE="key_login_phone";
    //登录方式   微信- 绑定手机-验证- 登录
    String KEY_LOGIN_TYPE_WX="key_login_type_wx";
    //登录方式   手机号-验证-登录
    String KEY_LOGIN_TYPE_PHONE="key_login_type_phone";
    //剪切板内容
    String KEY_CLIP_SEARCH_TEXT="key_clip_search_text";
    //邀请码
    String KEY_INVITATION_CODE="key_invitation_code";

    int E_UPLIMIT_SORT_DOWN=0;//降序
    int E_UPLIMIT_SORT_UP=1;//升序
    //actionbar title
    String KEY_ACTIONBAR_TITLE="key_actionbar_title";

    //商学院的搜索历史
    String KEY_SAVE_BUSINESS_SEARCH_HISTORY="key_save_business_search_history";
    //搜索
    String KEY_SAVE_SEARCH_HISTORY="key_save_search_history";
    String KEY_SEARCH_TITLE="key_search_title";//搜索栏内容
    String KEY_SEARCH_ID="key_search_id";//搜索栏 id
    String KEY_CURRENT_SEARCH_PLATFORM="key_current_search_platform";//当前要搜索的平台
    String KEY_PRODUCT_PLATFORM_TYPE="key_product_platform_type";//商品来源 京东 拼多多 淘宝
    String KEY_LIMIT_TIME_TYPE="key_limit_time_type";//秒杀列表批次号
    String KEY_LIMIT_PRODUCT_TYPE="key_limit_product_type";//是否是本地库商品


    String SOURCE_PING_DUO_DUO="source_ping_duo_duo";//拼多多
    String SOURCE_JING_DONG="source_jing_dong";//京东
    String SOURCE_TAO_BAO="source_tao_bao";//淘宝

    //平台  0 淘宝 1 京东 2 拼多多 3天猫 4考拉 100自营
    int PLATFORM_TAO_BAO=0;
    int PLATFORM_JING_DONG=1;
    int PLATFORM_PIN_DUO_DUO=2;
    int PLATFORM_TIAN_MAO =3;
    int PLATFORM_KAO_LA =4;
    int PLATFORM_ZI_YING =100;

    String KEY_FIRST_COMMODITY_ID ="key_first_commodity_id";//首页 tabLayout一级类目的id
    String KEY_SECOND_COMMODITY_ID ="key_second_commodity_id";//本地二级类目的id
    String KEY_NATIVE_DETAIL_ID ="key_detail_id";//商品详情id
    String KEY_THREE_DETAIL_ID="key_three_detail_id";//商品详情的三方id
    String KEY_DETAIL_TITLE="key_detail_title";//商品详情title
    String KET_COUPON_CLICK_URL="ket_coupon_click_url";//商详优惠券地址
    String KEY_ACTIVITY_FROM_PRODUCT_TYPE="key_activity_from_product_type";//页面跳转来源

    //传递到订单页面数据
    String KEY_ORDER_PRODUCT_TITLE="key_order_product_title";//商品title
    String KEY_ORDER_PRODUCT_NUMBER="key_order_product_number";//商品数量
    String KEY_ORDER_PRODUCT_PRICE="key_order_product_price";//商品价格
    String KEY_ORDER_PRODUCT_PICTURE="key_order_product_picture";//商品图

    String KEY_ADDRESS_BEAN="key_address_bean";//收货地址bean

    //传递到分享页面
    String KEY_SHARE_COMMISSION_PRICE="key_share_commission_price";//返红包
    String KEY_SHARE_AFTER_PRICE="key_share_after_price";//券后价
    String KEY_SHARE_ORIGINAL_PRICE="key_share_original_price";//原价，在售价
    String KEY_SHARE_PRODUCT_TITLE="key_share_product_title";//标题
    String KEY_SHARE_PRODUCT_VOLUME="key_share_product_volume";//销量
    String KEY_SHARE_PRODUCT_COUPONAMOUNT="key_share_product_couponAmount";//优惠卷金额
    String KEY_SHARE_PRODUCT_MAIN_PICTURL="key_share_product_main_pictUrl";//主图url
    String KEY_SHARE_PRODUCT_ALL_PICTURL="key_share_product_all_pictUrl";//商品轮播图
    String KEY_SHARE_PRODUCT_RUSH_ADDRESS_URL="key_share_product_rush_address_url";//抢购地址



    String KEY_SHARE_FROM_TYPE="key_share_from_type";//分享类型 0 本地 1 api
    int KEY_SHARE_NATIVE_VALUE=0;//本地 0
    int KEY_SHARE_THREE_VALUE=1;//三方1 api
    String KEY_SHARE_PLATFORM_TYPE="key_share_platform_type";//平台类型 0 淘宝 1 京东 2 拼多多


    //提现页面
    String KEY_OPERATE_NAME="key_operate_name";//运营商
    
    String KEY_INCOME_TIME_TYPE="key_income_time_type";//查看更多（日报，周报，月报）
    String KEY_INCOME_TAB_SELECTED="key_income_tab_selected";//0上月（上周），1近3月（近3周），2 近6月（近6周）

    //消息Id
    String KEY_PUSH_HAD_EDIT_FROM_LIST="key_push_had_edit_from_list";
    //选中商品
    String KEY_PUSH_GOODS_SELECT="key_push_goods_select";
    //选中粉丝
    String KEY_FANS_SELECT="key_fans_select";
    //已选粉丝id
    String KEY_FANS_SELECT_IDS="key_fans_select_ids";
    //粉丝是否 全选
    String KEY_FANS_SELECT_All="key_fans_select_all";

    //粉丝列表类型
    String KEY_FANS_LIST_TYPE="key_fans_list_type";
    //订单列表类型
    String KEY_ORDER_SEARCH_TYPE="key_order_search_type";

    String KEY_ORDER_TYPE="key_order_type";
    //tab位置
    String KEY_INDICATOR_TYPE="key_indicator_type";
    //抖券传值
    String KEY_SHAKE_COUPON_VALUE="key_shake_coupon_value";
    String KEY_SHAKE_COUPON_PAGE="key_shake_coupon_page";
    String KEY_SHAKE_COUPON_FROM="key_shake_coupon_from";//来源页 0来自 三方数据，1来自三方+自己配置数据

    //发圈传值
    String KEY_FIND_CIRCLE_FIRST_TAB_ID ="key_find_circle_first_tab_id";//一级类目id
    String KEY_FIND_CIRCLE_SECOND_TAB_ID ="key_find_circle_second_tab_id";//二级类目id


    //金刚区 传值
    String KEY_HOME_ICON_TYPE="key_home_icon_type";

    //提现页面
    String WITHDRAW_ACCOUNT="account";
    String WITHDRAW_STATUS="status";
    String WITHDRAW_MONEY="withdrawmoney";
    String WITHDRAW_BANKACCOUTN="bankAccount";
    String WITHDRAW_FULLNAME="fullName";
    String WITHDRAW_IDCARD="idCard";
    String WITHDRAW_BINDSTATUS="bindStatus";
    String WITHDRAW_BANKNICKNAME="bankNickName";
    String WITHDRAW_BANKNAME="bankName";
    String WITHDRAW_EID="eid";
    String WITHDRAW_NAME="nickname";
    String WITHDRAW_OPERATOR="operator";


    //超级购页面

    String SUPER_SHOPIMG="shopimg";
    String SUPER_SHOPNAME="shop_name";
    String SUPER_SHOPCONTENT="shop_content";
    String SUPER_SHOPDESCRIBLE="shop_describle";
    String SUPER_SHOPURL="shop_url";
    String SUPER_SHOPID="shop_id";
    String SUPER_SHOPSTATUS="shop_status";

    //首页 种草 type 文章类型 0 本地文章 1 三方文章
    String WANT_TO_BUY_FROM_TYPE="want_to_buy_from_type";
    //种草文章id
    String WANT_TO_BUY_ID="want_to_buy_id";

    //订单 自购  团队   自营

    String ORDER_SELFPURCHASE="order_selfpurchase";
    String ORDER_SELFSUPPORADDRESS="order_selfsupporaddress";
    String ORDER_SELFSUPPORNUMBER="order_selfsuppornumber";


    //签到每日弹框 时间
    String KEY_SIGN_IN_EVERY_DAY_TIME="KEY_SIGN_IN_EVERY_DAY_TIME";
    //签到每日弹框已经弹出次数
    String KEY_SIGN_IN_TIMES="key_sign_in_times";
    //最多 签到每日弹框已经弹出次数
    int SIGN_IN_DIALOG_MAX_TIMES=3;
    //签到推送开关
    String KEY_SIGN_IN_PUSH_SWITCH="key_sign_in_push_switch";

    //首页弹框
    String AGREEMENT_KEY_TYPE="agreement_key_type";


}
