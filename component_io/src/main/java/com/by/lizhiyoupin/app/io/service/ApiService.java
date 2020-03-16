package com.by.lizhiyoupin.app.io.service;

import com.by.lizhiyoupin.app.io.IPManager;
import com.by.lizhiyoupin.app.io.OkHttpClientUtils;
import com.by.lizhiyoupin.app.io.api.ActivityApi;
import com.by.lizhiyoupin.app.io.api.AvatarApi;
import com.by.lizhiyoupin.app.io.api.DetailPicApi;
import com.by.lizhiyoupin.app.io.api.FansApi;
import com.by.lizhiyoupin.app.io.api.FindCircleApi;
import com.by.lizhiyoupin.app.io.api.HomeApi;
import com.by.lizhiyoupin.app.io.api.IncomeApi;
import com.by.lizhiyoupin.app.io.api.MessageApi;
import com.by.lizhiyoupin.app.io.api.NewsApi;
import com.by.lizhiyoupin.app.io.api.OrderApi;
import com.by.lizhiyoupin.app.io.api.ProductDetailApi;
import com.by.lizhiyoupin.app.io.api.PushEditApi;
import com.by.lizhiyoupin.app.io.api.SearchApi;
import com.by.lizhiyoupin.app.io.api.SettingApi;
import com.by.lizhiyoupin.app.io.api.ShareApi;
import com.by.lizhiyoupin.app.io.api.ShoppingApi;
import com.by.lizhiyoupin.app.io.api.SignInApi;
import com.by.lizhiyoupin.app.io.api.SuperApi;
import com.by.lizhiyoupin.app.io.api.UserHomeApi;
import com.by.lizhiyoupin.app.io.api.VipApi;
import com.by.lizhiyoupin.app.io.api.WithdrawApi;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/20 16:55
 * Summary:
 */
public class ApiService {

    private static NewsApi newsApi;
    private static HomeApi homeApi;
    private static SearchApi mSearchApi;
    private static ProductDetailApi sProductDetailApi;
    private static VipApi sVipApi;
    private static ShareApi sShareApi;
    private static DetailPicApi sDetailPicApi;
    private static OrderApi sOrderApi;
    private static SettingApi sSettingApi;
    private static IncomeApi sIncomeApi;
    private static FansApi sFansApi;
    private static ActivityApi sActivityApi;
    private static AvatarApi sAvatarApi;
    private static MessageApi sMessageApi;
    private static ShoppingApi sShoppingApi;
    private static FindCircleApi sFindCircleApi;
    private static WithdrawApi sWithdrawApi;
    private static UserHomeApi sUserHomeApi;
    private static SuperApi sSuperKindApi;
    private static PushEditApi sPushEditApi;
    private static SignInApi sSignInApi;


    public static NewsApi getNewsApi() {
        if (null == newsApi) {
            newsApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpClient())
                    .baseUrl(IPManager.getInstance().getUserIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(NewsApi.class);

        }
        return newsApi;
    }

    public static HomeApi getHomeApi() {
        if (null == homeApi) {
            homeApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpClient())
                    .baseUrl(IPManager.getInstance().getGoodsIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(HomeApi.class);
        }
        return homeApi;
    }

    public static SearchApi getSearchApi() {
        if (null == mSearchApi) {
            mSearchApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpNoCacheClient())
                    .baseUrl(IPManager.getInstance().getGoodsIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(SearchApi.class);
        }
        return mSearchApi;
    }

    public static ProductDetailApi getProductDetailApi() {
        if (null == sProductDetailApi) {
            sProductDetailApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpNoCacheClient())
                    .baseUrl(IPManager.getInstance().getGoodsIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(ProductDetailApi.class);
        }
        return sProductDetailApi;
    }

    public static VipApi getVipApi() {
        if (null == sVipApi) {
            sVipApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpClient())
                    .baseUrl(IPManager.getInstance().getUserIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(VipApi.class);
        }
        return sVipApi;
    }

    public static ShareApi getShareApi() {
        if (null == sShareApi) {
            sShareApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpNoCacheClient())
                    .baseUrl(IPManager.getInstance().getGoodsIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(ShareApi.class);
        }
        return sShareApi;
    }

    public static DetailPicApi getDetailPicApi() {
        if (null == sDetailPicApi) {
            sDetailPicApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpNoCacheClient())
                    .baseUrl(IPManager.getInstance().getTaobaoPicIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(DetailPicApi.class);
        }
        return sDetailPicApi;
    }

    public static OrderApi getOrderApi() {
        if (null == sOrderApi) {
            sOrderApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpNoCacheClient())
                    .baseUrl(IPManager.getInstance().getGoodsIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(OrderApi.class);
        }
        return sOrderApi;
    }

    public static SettingApi getSettingApi() {
        if (null == sSettingApi) {
            sSettingApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpNoCacheClient())
                    .baseUrl(IPManager.getInstance().getUserIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(SettingApi.class);
        }
        return sSettingApi;
    }

    public static IncomeApi getIncomeApi() {
        if (null == sIncomeApi) {
            sIncomeApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpNoCacheClient())
                    .baseUrl(IPManager.getInstance().getGoodsIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(IncomeApi.class);
        }
        return sIncomeApi;
    }

    public static FansApi getFansApi() {
        if (sFansApi == null) {
            sFansApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpNoCacheClient())
                    .baseUrl(IPManager.getInstance().getGoodsIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(FansApi.class);
        }
        return sFansApi;
    }

    public static AvatarApi getAvatarApi() {
        if (null == sAvatarApi) {
            sAvatarApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpNoCacheClient())
                    .baseUrl(IPManager.getInstance().getUserIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(AvatarApi.class);
        }
        return sAvatarApi;
    }

    public static ActivityApi getActivityApi() {
        if (null == sActivityApi) {
            sActivityApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpNoCacheClient())
                    .baseUrl(IPManager.getInstance().getGoodsIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(ActivityApi.class);
        }
        return sActivityApi;
    }

    public static MessageApi getMessageApi() {
        if (null == sMessageApi) {
            sMessageApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpNoCacheClient())
                    .baseUrl(IPManager.getInstance().getUserIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(MessageApi.class);
        }
        return sMessageApi;
    }

    public static ShoppingApi getShoppingApi() {
        if (sShoppingApi == null) {
            sShoppingApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpClient())
                    .baseUrl(IPManager.getInstance().getUserIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(ShoppingApi.class);

        }
        return sShoppingApi;
    }
    public static WithdrawApi getWithdrawApi() {
        if (sWithdrawApi == null) {
            sWithdrawApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpClient())
                    .baseUrl(IPManager.getInstance().getUserIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(WithdrawApi.class);

        }
        return sWithdrawApi;
    }

    public static FindCircleApi getFindCircleApi() {
        if (sFindCircleApi == null) {
            sFindCircleApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpClient())
                    .baseUrl(IPManager.getInstance().getUserIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(FindCircleApi.class);

        }
        return sFindCircleApi;
    }

    public static UserHomeApi getUserHomeApi() {
        if (sUserHomeApi == null) {
            sUserHomeApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpNoCacheClient())
                    .baseUrl(IPManager.getInstance().getGoodsIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(UserHomeApi.class);
        }
        return sUserHomeApi;
    }

    public static SuperApi getSuperApi() {
        if (sSuperKindApi == null) {
            sSuperKindApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpNoCacheClient())
                    .baseUrl(IPManager.getInstance().getGoodsIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(SuperApi.class);
        }
        return sSuperKindApi;
    }
    public static PushEditApi getPushEditApi() {
        if (sPushEditApi == null) {
            sPushEditApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpNoCacheClient())
                    .baseUrl(IPManager.getInstance().getUserIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(PushEditApi.class);
        }
        return sPushEditApi;
    }

    public static SignInApi getSignInApi() {
        if (sSignInApi == null) {
            sSignInApi = new Retrofit.Builder().client(OkHttpClientUtils.getOkHttpNoCacheClient())
                    .baseUrl(IPManager.getInstance().getUserIp())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(SignInApi.class);
        }
        return sSignInApi;
    }

}
