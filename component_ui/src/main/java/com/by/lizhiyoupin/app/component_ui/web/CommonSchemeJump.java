package com.by.lizhiyoupin.app.component_ui.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.by.lizhiyoupin.app.common.CommonConst;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/24 16:42
 * Summary: 路由跳转
 */
public class CommonSchemeJump {
    public static final String TAG = CommonSchemeJump.class.getSimpleName();

    public static Intent createIntent(@NonNull final Context context, @NonNull final Class cls) {
        final Intent intent = new Intent(context, cls);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    public static void showActivity(@Nullable final Context context, @NonNull final Class cls) {
        context.startActivity(createIntent(context, cls));
    }

    public static void showActivity(@Nullable final Context context, @NonNull String routerName) {
        try {
            ARouter.getInstance().build(routerName).navigation(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showActivity(@Nullable final Context context, @NonNull String routerName, Bundle bundle) {
        try {
            ARouter.getInstance().build(routerName).with(bundle).navigation(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showActivityForResult(@NonNull final Activity context, @NonNull String routerName, final int requestCode) {
        try {
            ARouter.getInstance().build(routerName).navigation(context, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showActivityForResult(@NonNull final Activity context, @NonNull String routerName, Bundle bundle, final int requestCode) {
        try {
            ARouter.getInstance().build(routerName).with(bundle).navigation(context, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开 主页面
     *
     * @param context
     */
    public static void showMainActivity(final Context context) {
        showActivity(context, "/app/MainActivity");
    }

    /**
     * 打开 MainActivity 并选择跳转页面
     *
     * @param context
     * @param mainPageIndex
     * @param subPageIndex
     * @param pageIndex
     */
    public static void showMainActivity(final Context context, String mainPageIndex, int subPageIndex, int pageIndex) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(CommonConst.KEY_MAIN_PAGE_INDEX, mainPageIndex);
            bundle.putInt(CommonConst.KEY_MAIN_SUBPAGE_INDEX, subPageIndex);
            bundle.putInt(CommonConst.KEY_CHILD_PAGE_INDEX, pageIndex);
            showActivity(context, "/app/MainActivity", bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 百川淘宝
     *
     * @param context
     * @param bundle
     */
    public static void showAutoWebActivity(@Nullable final Context context, Bundle bundle) {
        showActivity(context, "/ui/AutoWebActivity", bundle);
    }

    /**
     * 金刚区 排序列表页
     *
     * @param context
     * @param bundle
     */
    public static void showDiamondKongIconActivity(@Nullable final Context context, Bundle bundle) {
        showActivity(context, "/app/DiamondKongIconActivity", bundle);
    }


    /**
     * 打开 三方api 商品详情页
     *
     * @param context
     * @param threeId
     * @param platformType
     */
    public static void showPreciseDetailActivity(final Context context, long threeId, int platformType) {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConst.KEY_PRODUCT_PLATFORM_TYPE, platformType);
        bundle.putLong(CommonConst.KEY_THREE_DETAIL_ID, threeId);
        showActivity(context, "/app/PreciseDetailActivity", bundle);
    }
    public static void showPreciseDetailActivity(final Context context, long threeId, int platformType,int activityType) {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConst.KEY_PRODUCT_PLATFORM_TYPE, platformType);
        bundle.putLong(CommonConst.KEY_THREE_DETAIL_ID, threeId);
        bundle.putInt(CommonConst.KEY_ACTIVITY_FROM_PRODUCT_TYPE, activityType);
        showActivity(context, "/app/PreciseDetailActivity", bundle);
    }

    /**
     * 打开 vip礼包详情页
     *
     * @param context
     * @param bundle
     */
    public static void showVipProductDetailActivity(final Context context, final Bundle bundle) {
        showActivity(context, "/app/VipProductDetailActivity", bundle);
    }

    /**
     * 去登录
     *
     * @param context
     */
    public static void showLoginActivity(final Context context) {
        showActivity(context, "/app/LoginMainActivity");
    }

    /**
     * 去登录 有回调
     *
     * @param context
     * @param requestCode 建议使用 CommonConst.LOGIN_REQUEST_CODE
     */
    public static void showLoginActivityForResult(final Activity context, int requestCode) {
        showActivityForResult(context, "/app/LoginMainActivity", requestCode);
    }

    /**
     * 打开 搜索结果页
     *
     * @param context
     */
    public static void showSearchResultActivity(final Context context, final Bundle bundle) {
        showActivity(context, "/app/SearchResultActivity", bundle);
    }

    /**
     * 打开 二级类目列表页
     *
     * @param context
     */
    public static void showSecondSortListActivity(final Context context, final Bundle bundle) {
        showActivity(context, "/app/SecondSortListActivity", bundle);
    }

    /**
     * 打开 分享页面
     *
     * @param context
     * @param bundle
     */
    public static void showCreateShareActivity(final Context context, final Bundle bundle) {
        showActivity(context, "/app/CreateShareActivity", bundle);
    }

    /**
     * 打开 订单页面
     *
     * @param context
     * @param bundle
     */
    public static void showPayOrderActivity(final Context context, final Bundle bundle) {
        showActivity(context, "/app/PayOrderActivity", bundle);
    }

    /**
     * 打开 设置页面
     *
     * @param context
     */
    public static void showSettingActivity(final Context context, final Bundle bundle) {
        showActivity(context, "/app/SettingActivity", bundle);
    }

    /**
     * 打开 修改昵称页面
     *
     * @param context
     */
    public static void showNicknameActivity(final Context context, final Bundle bundle) {
        showActivity(context, "/app/Revise_NicknameActivity", bundle);
    }

    /**
     * 打开 修改手机号页面
     *
     * @param context
     */
    public static void showNumberActivity(final Context context) {
        showActivity(context, "/app/Revise_NumberActivity");
    }

    /**
     * 打开 支付宝绑定页面
     *
     * @param context
     */
    public static void showAlipayBindingActivity(final Context context) {
        showActivity(context, "/app/" +
                "");
    }

    /**
     * 打开 银行卡绑定页面
     *
     * @param context
     */
    public static void showBankcardBindingActivity(final Context context) {
        showActivity(context, "/app/BankcardBindingActivity");
    }

    /**
     * 打开 普通用户提现页面
     *
     * @param context
     */
    public static void showUser_withdrawActivity(final Context context) {
        showActivity(context, "/app/User_withdrawActivity");
    }

    /**
     * 打开 运营商提现页面
     *
     * @param context
     */
    public static void showOperator_withdrawActivity(final Context context) {
        showActivity(context, "/app/Operator_withdrawActivity");
    }

    /**
     * 打开 提现明细页面
     *
     * @param context
     */
    public static void showWithdrawDetailsActivity(final Context context) {
        showActivity(context, "/app/WithdrawDetailsActivity");
    }

    /**
     * 打开收益相关的 数据报表页面
     *
     * @param context
     * @param bundle
     */
    public static void showIncomeDataReportActivity(final Context context, final Bundle bundle) {
        showActivity(context, "/app/IncomeDataReportActivity", bundle);
    }

    /**
     * 跳转 我的订单
     *
     * @param context
     */
    public static void showOrderQueryActivity(final Context context) {
        showActivity(context, "/app/OrderQueryActivity");
    }

    /**
     * 跳转 值得买
     *
     * @param context
     */
    public static void showWorthBuyingActivity(final Context context) {
        showActivity(context, "/app/WorthBuyingActivity");
    }

    /**
     * 跳转 销量榜
     *
     * @param context
     */
    public static void showSalesListActivity(final Context context) {
        showActivity(context, "/app/SalesListActivity");
    }

    /**
     * 限时秒杀页面
     *
     * @param context
     */
    public static void showLimitedTimeSpikeActivity(final Context context) {
        showActivity(context, "/app/LimitedTimeSpikeActivity");
    }

    /**
     * 抖券列表 页面
     *
     * @param context
     */
    public static void showShakeCouponListActivity(final Context context) {
        showActivity(context, "/app/ShakeCouponListActivity");
    }


    /**
     * 抖券主页面
     *
     * @param context
     */
    public static void showShakeCouponMainRvActivity(final Context context, Bundle bundle) {
        showActivity(context, "/app/ShakeCouponMainRvActivity", bundle);
    }

    /**
     * 购物车
     *
     * @param context
     */
    public static void showShoppingCartActivity(final Context context) {
        showActivity(context, "/app/ShoppingCartActivity");
    }

    /**
     * 足迹
     *
     * @param context
     */
    public static void showFootprintActivity(final Context context) {
        showActivity(context, "/app/FootprintActivity");
    }

    /**
     * 跳转商学院-二级文章列表页
     *
     * @param context
     */
    public static void showBusinessSecondListActivity(final Context context, Bundle bundle) {
        showActivity(context, "/app/BusinessSecondListActivity", bundle);
    }

    /**
     * 商学院--荔枝资讯列表页
     *
     * @param context
     */
    public static void showBusinessInformationActivity(final Context context) {
        showActivity(context, "/app/BusinessInformationActivity");
    }

    /**
     * 商学院 搜索页
     *
     * @param context
     */
    public static void showBusinessSearchActivity(final Context context) {
        showActivity(context, "/app/BusinessSearchActivity");
    }

    /**
     * 商学院 搜索结果页
     *
     * @param context
     * @param searchTitle
     */
    public static void showBusinessSearchResultActivity(final Context context, String searchTitle) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.KEY_SEARCH_TITLE, searchTitle);
        showActivity(context, "/app/BusinessSearchResultActivity", bundle);
    }

    public static void showJingdong(final Context context, final String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        intent.setClassName("com.jingdong.app.mall", "com.jd.lib.productdetail.ProductDetailActivity");
        context.startActivity(intent);
    }

    /**
     * 每日爆款
     *
     * @param context
     */
    public static void showDailyExplosionsActivity(final Context context) {

        showActivity(context, "/app/DailyExplosionsActivity");
    }

    /**
     * 种草详情
     *
     * @param context
     * @param articleId
     * @param articleType
     */
    public static void showWantToBuyDetailActivity(final Context context, long articleId, int articleType) {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConst.WANT_TO_BUY_FROM_TYPE, articleType);
        bundle.putLong(CommonConst.WANT_TO_BUY_ID, articleId);
        showActivity(context, "/app/WantToBuyDetailActivity", bundle);
    }

    /**
     * 推送编辑页
     *
     * @param context
     * @param messageId 消息Id
     */
    public static void showPushEditActivity(final Context context, @Nullable Long messageId) {
        Bundle bundle = new Bundle();
        if (messageId != null) {
            bundle.putLong(CommonConst.KEY_PUSH_HAD_EDIT_FROM_LIST, messageId);
        }
        showActivity(context, "/app/PushEditorActivity", bundle);
    }

    /**
     * 签到
     * @param context
     */
    public static void showSignInActivity(final Context context){
        showActivity(context, "/app/SignInActivity");
    }
}
