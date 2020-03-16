package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.ShopBannerBean;
import com.by.lizhiyoupin.app.io.bean.ShopMdBean;
import com.by.lizhiyoupin.app.io.bean.ShopMindBean;
import com.by.lizhiyoupin.app.io.bean.SuperAttionBean;
import com.by.lizhiyoupin.app.io.bean.SuperKindBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * data:2019/11/23
 * author:jyx
 * function:
 */
public interface SuperApi {
    /**
     * 超级购-分类列表
     * @return
     */
    @GET("super-kind/v1/all-kind")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<SuperKindBean>>> requestSuperKind();


    /**
     * 超级购-分类列表
     * @return
     */
    @GET("super-info/v1/shop-kind")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<ShopMindBean>> requestShopKind(@Query("kindId") int kindId,
                                                       @Query("minId") int minId/*,
                                                       @Query("pageNo") int pageNo,
                                                       @Query("pageSize") int pageSize*/);

    /**
     * 超级购-用户关注店铺
     * @return
     */
    @POST("user-super/v1/follow-shop")
    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<SuperAttionBean>> requestFollowShop(@Field("shopId") int shopId,
                                                            @Field("followType") int followType);



    /**
     * 超级购-根据店铺获取商品列表
     * @return
     */
    @GET("super-info/v1/goods-list")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<ShopMdBean>> requestShopGoods(@Query("shopId") int shopId,
                                                      @Query("minId") int minId,
                                                    /*  @Query("pageNo") int pageNo,
                                                      @Query("pageSize") int pageSize,*/
                                                      @Query("sortType") int sortType,
                                                      @Query("sortDesc") int sortDesc
                                                             );

    /**
     * 超级购-分类列表
     * @return
     */
    @GET("super-info/v1/brandList")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<ShopBannerBean>>> requestShopBanner();


}
