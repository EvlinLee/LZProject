package com.by.lizhiyoupin.app.component_ui.web;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/24 19:37
 * Summary: native  使用Scheme 跳转的host方法名
 * 各方法名不可随意修改
 * 如：
 * final ISchemeManager scheme=(ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
 * scheme.handleUrl(mContext,"litchi://mainJump?mainTabIndex=main_home_page_tab&mainSubPageIndex=1&childPageIndex=1")
 */
public class LitchiProtocal {
    public static final String TAG = LitchiProtocal.class.getSimpleName();
    private static final String ACTIONBAR_NAME = "actionbar_name";
    private static final String KEY_ID = "key_id";
    private static final String KEY_FIRST_TAB_ID = "firstTabId";
    private static final String KEY_ACTION_TITLE_ID = "actionTitle";
    private static final String KEY_TYPE = "type";
    private static final String KEY_TITLE = "title";
    private static final String KEY_VIP_DETAIL_ID = "key_detail_id";
    private static final String KEY_ACTIVITY_TYPE= "activityType";

    //跳转到限时秒杀native url
    public static final String LIMITED_TIME_SPIKE_URL = "litchi://limitedTimeSpikeJump";

    /**
     * litchi://mainJump?mainTabIndex=main_home_page_tab&mainSubPageIndex=1&childPageIndex=1
     * mainTabIndex 即0级页 :省钱 main_home_page_tab ，超级导航 main_navigation_tab, 会员 main_vip_tab,发圈 main_circle_tab，我的 main_user_tab
     * mainSubPageIndex 首页一级栏目中可选tab页 即1级页 传值从1开始,本地需要给他-1 ，让他从0开始
     * childPageIndex  即2级页
     *
     * @param context
     */
    public static void mainJump(final Context context, final Uri uri) {

        String mainPageIndex = uri.getQueryParameter(CommonConst.KEY_MAIN_PAGE_INDEX);
        String subPage = uri.getQueryParameter(CommonConst.KEY_MAIN_SUBPAGE_INDEX);
        String page = uri.getQueryParameter(CommonConst.KEY_CHILD_PAGE_INDEX);
        int subPageIndex = 0;
        int pageIndex = 0;
        try {
            subPageIndex = TextUtils.isEmpty(subPage) ? 0 : Integer.valueOf(subPage) - 1;
            pageIndex = TextUtils.isEmpty(page) ? 0 : Integer.valueOf(page) - 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        CommonSchemeJump.showMainActivity(context, mainPageIndex, subPageIndex, pageIndex);
    }

    /**
     * 跳转 商品详情页
     * litchi://productDetailJump?platformType=0&commodityId=1212515415&activityType=1
     *
     * @param context
     * @param uri
     */
    public static void productDetailJump(final Context context, final Uri uri) {
        String platformType = uri.getQueryParameter("platformType");
        String commodityId = uri.getQueryParameter("commodityId");
        String activityType = uri.getQueryParameter(KEY_ACTIVITY_TYPE);
        try {
            if (!TextUtils.isEmpty(activityType)) {
                CommonSchemeJump.showPreciseDetailActivity(context,
                        commodityId == null ? 0 : Long.parseLong(commodityId),
                        platformType == null ? 0 : Integer.parseInt(platformType),
                        activityType == null ? -1 : Integer.parseInt(activityType));
            } else {
                CommonSchemeJump.showPreciseDetailActivity(context,
                        commodityId == null ? 0 : Long.parseLong(commodityId),
                        platformType == null ? 0 : Integer.parseInt(platformType));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * litchi://loginJump
     * 登录
     *
     * @param context
     */
    public static void loginJump(final Context context, final Uri uri) {
        CommonSchemeJump.showLoginActivity(context);
    }

    /**
     * litchi://secondListJump?actionbar_name=香辣速食&key_id=86380
     * actionbar_name 一级栏目名
     * key_id 一级栏目id
     *
     * @param context
     * @param uri
     */
    public static void secondListJump(final Context context, final Uri uri) {
        final String actionbarName = uri.getQueryParameter(ACTIONBAR_NAME);
        final String keyId = uri.getQueryParameter(KEY_ID);
        if (TextUtils.isEmpty(keyId)) {
            LZLog.i(TAG, "key_id not be null");
            return;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putString(CommonConst.KEY_ACTIONBAR_TITLE, actionbarName);
            bundle.putLong(CommonConst.KEY_SECOND_COMMODITY_ID, Long.valueOf(keyId));
            CommonSchemeJump.showSecondSortListActivity(context, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * litchi://worthBuyingJump
     * 跳转 值得买
     *
     * @param context
     * @param uri
     */
    public static void worthBuyingJump(final Context context, final Uri uri) {
        CommonSchemeJump.showWorthBuyingActivity(context);
    }

    /**
     * litchi://salesListJump
     * 跳转 销量榜
     *
     * @param context
     * @param uri
     */
    public static void salesListJump(final Context context, final Uri uri) {
        CommonSchemeJump.showSalesListActivity(context);
    }

    /**
     * litchi://limitedTimeSpikeJump
     * 跳转 限时秒杀
     *
     * @param context
     * @param uri
     */
    public static void limitedTimeSpikeJump(final Context context, final Uri uri) {
        CommonSchemeJump.showLimitedTimeSpikeActivity(context);
    }

    /**
     * litchi://shakeCouponListJump
     * 跳转 抖券
     *
     * @param context
     * @param uri
     */
    public static void shakeCouponListJump(final Context context, final Uri uri) {
        CommonSchemeJump.showShakeCouponListActivity(context);
    }

    /**
     * litchi://businessSecondListJump?firstTabId=XXX&actionTitle=xxx
     * 跳转商学院-二级文章列表页
     * firstTabId 一级icon列表Id
     *
     * @param context
     * @param uri
     */
    public static void businessSecondListJump(final Context context, final Uri uri) {
        final String firstTabId = uri.getQueryParameter(KEY_FIRST_TAB_ID);
        final String title = uri.getQueryParameter(KEY_ACTION_TITLE_ID);
        if (TextUtils.isEmpty(firstTabId)) {
            LZLog.i(TAG, "firstTabId not be null");
            return;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putLong(CommonConst.KEY_FIND_CIRCLE_FIRST_TAB_ID, Long.valueOf(firstTabId));
            bundle.putString(CommonConst.KEY_ACTIONBAR_TITLE, title);
            CommonSchemeJump.showBusinessSecondListActivity(context, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 金刚区排序列表页 icon跳转
     * litchi://areaListJump?type=nine_to_nine&title=9.9包邮
     *
     * @param context
     * @param uri
     */
    public static void areaListJump(final Context context, final Uri uri) {
        final String type = uri.getQueryParameter(KEY_TYPE);
        final String title = uri.getQueryParameter(KEY_TITLE);
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.KEY_HOME_ICON_TYPE, type);
        bundle.putString(CommonConst.KEY_ACTIONBAR_TITLE, title);
        CommonSchemeJump.showDiamondKongIconActivity(context, bundle);
    }


    /**
     * 每日爆款
     * litchi://dailyExplosionsJump
     *
     * @param context
     * @param uri
     */
    public static void dailyExplosionsJump(final Context context, final Uri uri) {
        CommonSchemeJump.showDailyExplosionsActivity(context);
    }

    /**
     * litchi://informationActivityJump
     * 跳转消息中心
     *
     * @param context
     * @param uri
     */
    public static void informationActivityJump(final Context context, final Uri uri) {
        CommonSchemeJump.showActivity(context, "/app/InformationActivity");
    }

    /**
     * litchi://signInActivityJump
     * 签到
     *
     * @param context
     * @param uri
     */
    public static void signInActivityJump(final Context context, final Uri uri) {
        CommonSchemeJump.showSignInActivity(context);
    }

    /**
     * litchi://vipProductDetailActivityJump?key_detail_id=XXXX010&activityType=1
     * 打开 vip礼包详情页
     *
     * @param context
     * @param uri
     */
    public static void vipProductDetailActivityJump(final Context context, final Uri uri) {
        try {
            final String detailId = uri.getQueryParameter(KEY_VIP_DETAIL_ID);
            final String activityType = uri.getQueryParameter(KEY_ACTIVITY_TYPE);
            long id = detailId==null?0:Long.valueOf(detailId);
            Bundle bundle = new Bundle();
            bundle.putLong(CommonConst.KEY_NATIVE_DETAIL_ID, id);
            if (!TextUtils.isEmpty(activityType)){
                bundle.putInt(CommonConst.KEY_ACTIVITY_FROM_PRODUCT_TYPE, Integer.parseInt(activityType));
            }
            CommonSchemeJump.showVipProductDetailActivity(context, bundle);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
