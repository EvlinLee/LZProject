package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.entity.ChannelListBean;
import com.by.lizhiyoupin.app.io.entity.CreateShareEntity;
import com.by.lizhiyoupin.app.io.entity.SharePosterEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/23 19:55
 * Summary:
 */
public interface ShareApi {
    /**
     * 分享商品页面内容数据接口
     *
     * @param commodityId  商品id
     * @param type         分享类型 0 本地 1 api
     * @param platformType 平台类型 0 淘宝 1 京东 2 拼多多
     * @param inviteCode   邀请码
     * @return
     */
    /**
     *
     * @param commodityId 商品id
     * @param type 分享类型 0 本地 1 api
     * @param platformType 平台类型 0 淘宝 1 京东 2 拼多多 3 天猫 4 考拉
     * @param title 商品标题
     * @param zkFinalPrice 原价
     * @param discountsPriceAfter 卷后价
     * @param volume 销量
     * @param couponAmount 优惠卷金额-商品没有金额传null或者0都可以
     * @param pictUrl 主图url
     * @return
     */
    @POST("commodity/v1/share")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<CreateShareEntity>> requestShareDetail(@Query("commodityId") long commodityId,
                                                               @Query("type") int type,
                                                               @Query("platformType") int platformType,
                                                               @Query("title") String title,
                                                               @Query("zkFinalPrice") String zkFinalPrice,
                                                               @Query("discountsPriceAfter") String discountsPriceAfter,
                                                               @Query("volume") String volume,
                                                               @Query("couponAmount") String couponAmount,
                                                               @Query("pictUrl") String pictUrl);

    /**
     *
     * 分享弹框 渠道及内容
     * 分享接口(99礼包/其他分享)
     * @param commodityId 礼包id
     * @param userId 用户id
     * @param scenarioType 场景类型（0-礼包分享，1-其他，scenarioType为0时，其他参数必传）
     * @param inviteCode 邀请码
     * @return
     */
    @GET("goods/v1/share-all")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<ChannelListBean>>> requestShareBean(@Query("scenarioType") int scenarioType,
                                                                 @Query("commodityId") Long commodityId,
                                                                 @Query("userId") long userId,
                                                                 @Query("inviteCode") String inviteCode);

    /**
     *  根据 商品id 获取海报图
     * @param goodsId
     * @param platformType
     * @return
     */
    @PUT("ring-info/v1/share-img")
    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<SharePosterEntity>> requestSharePosterList(@Field("goodsId") Long goodsId,
                                                                         @Field("platformType") int platformType);



}
