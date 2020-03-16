package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.entity.DetailGuessYouLikeEntity;
import com.by.lizhiyoupin.app.io.entity.DetailRecommendationEntity;
import com.by.lizhiyoupin.app.io.entity.PreciseDetailEntity;
import com.by.lizhiyoupin.app.io.entity.ProductDetailEntity;
import com.by.lizhiyoupin.app.io.entity.RequestButtonRecommend;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/21 16:07
 * Summary:
 */
public interface ProductDetailApi {
    /**
     * 三方接口---商品详情接口
     *
     * @param goodsId 商品id(精选列表有传)
     * @param platformType 0淘宝，1京东 ，2拼多多
     * @param batch 批次号(用于限时秒杀)
     * @param fastBuyCommodityType 是否本地商品 0-三方商品 1-本地商品(用于限时秒杀)
     * @param activityType 活动类型 1-新人0元购
     * @return
     */
    @GET("goods-handpick/v2/handpick-details")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<PreciseDetailEntity>> requestThreePartyProductDetail(@Query("goodsId") Long goodsId,
                                                                             @Query("platformType")int platformType,
                                                                             @Query("deviceType") String deviceType,
                                                                             @Query("batch")Integer batch,
                                                                             @Query("fastBuyCommodityType")Integer fastBuyCommodityType,
                                                                             @Query("activityType")int activityType);

    @GET("goods-handpick/v1/page-images")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<String>>> requestThreePartyProductPics(@Query("goodsId") long goodsId,
                                                                             @Query("platformType")int platformType);

    /**
     * 本地服务器 ----商品详情
     * @param commodityId
     * @return
     */
    @GET("commodity/v1/info")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<ProductDetailEntity>> requestLocalProductDetail(@Query("id") Long commodityId,
                                                                        @Query("deviceType") String deviceType);

    /**
     * 本地和三方都调用同一个
     * 商品详情-获取荔枝推荐
     * @param keyWord 关键字(商品标题)
     * @param platformType 平台类型0 淘宝 1 京东 2 拼多多
     * @param deviceType 非必填 ，智能匹配-设备号类型：IMEI，或者IDFA，或者UTDID（UTDID不支持MD5加密)
     * @return
     */
    @GET("goods-handpick/v1/handpick-recommended")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<DetailRecommendationEntity>> requestDetailRecommendation(@Query("keyWord") String keyWord,
                                                                                 @Query("platformType") int platformType,
                                                                                 @Query("deviceType") String deviceType);

    /**
     * 本地和三方都调用同一个
     * 商品详情-获取猜你喜欢
     * @param goodsId  商品id
     * @param deviceType 必填   智能匹配-设备号类型：IMEI，或者IDFA，或者UTDID（UTDID不支持MD5加密)
     * @return
     */
    @GET("goods-handpick/v1/handpick-like")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<DetailGuessYouLikeEntity>> requestDetailGuessYouLike(@Query("goodsId") String goodsId,
                                                                             @Query("deviceType") String deviceType);

    /**
     * 商品介绍图片
     * @param goodsId
     * @return
     */
    @GET("goods-handpick/v1/details-images")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<String>>> requestThreePartyProductDecPic(@Query("goodsId") Long goodsId);

    /**
     * 添加 到推荐 列表
     * @param recommend
     * @return
     */
    @POST("recommend-dou/v1/talent-recommend")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Observable<BaseBean<Long>> requestAddButtonRecommendInfo(@Body RequestButtonRecommend recommend);

    /**
     * 从推荐列表 删除
     * @param recommendId
     * @return
     */
    @GET("recommend-dou/v1/delete-recommend")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<Object>> requestDeleteButtonRecommendInfo(@Query("recommendId") long recommendId);
}
