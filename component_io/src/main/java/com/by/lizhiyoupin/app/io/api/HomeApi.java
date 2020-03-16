package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.CommonCategoryBean;
import com.by.lizhiyoupin.app.io.bean.CommonListBean;
import com.by.lizhiyoupin.app.io.bean.CommonSecondBean;
import com.by.lizhiyoupin.app.io.bean.DiamonKongBean;
import com.by.lizhiyoupin.app.io.bean.GuideArticleBean;
import com.by.lizhiyoupin.app.io.bean.GuideArticleDetailBean;
import com.by.lizhiyoupin.app.io.bean.PreciseBannerIconBean;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;
import com.by.lizhiyoupin.app.io.bean.PreciseSelectionBean;
import com.by.lizhiyoupin.app.io.bean.RecordsBean;
import com.by.lizhiyoupin.app.io.bean.RedGiftBean;
import com.by.lizhiyoupin.app.io.bean.VipGoodListBean;
import com.by.lizhiyoupin.app.io.bean.VipGoodsBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/15 20:37
 * Summary:
 */
public interface HomeApi {
    /**
     * 查询 精选频道 入口 广告等数据接口
     * banner+icon
     *
     * @return
     */
    @GET("selection-channel/v1/list")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<PreciseSelectionBean>> requestSelectionChannel();

    /**
     * 查询一级类目列表
     * 即首页顶部类目列表
     *
     * @return
     */
    @GET("commodity-kind/v1/first-level")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<CommonCategoryBean>>> requestCommodityKindList();

    /**
     * 查询其他频道二级类目 banner+icon数据
     *
     * @return
     */
    @GET("commodity-kind/v1/second-level")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<CommonSecondBean>> requestSecondLevel(@Query("id") Long id);

    /**
     * 精选目录下 底部列表查询
     *
     * @param start 起始页-default=0
     * @param limit 每页条数-default=10
     * @return
     */
    @GET("goods-handpick/v1/list-handpick")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<CommonCategoryBean>>> requestPreciseList(@Query("start") int start, @Query("limit") int limit);

    /**
     * @param orderType       排序类型 1 综合排序 2 销量排序 3 券后价排序 4 佣金排序
     * @param commodityKindId 一级类目id
     * @param start           分页起始页
     * @param limit           每页显示多少条 默认10 条
     * @return
     */
    @GET("commodity/v1/page")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<CommonListBean>> requestOtherCommonList(@Query("orderType") int orderType, @Query("commodityKindId") int commodityKindId,
                                                                @Query("start") int start, @Query("limit") int limit);

    /**
     * 其他一级类目的 本地 可排序商品列表
     *
     * @param map
     * @return
     */
    @GET("commodity/v1/page")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<RecordsBean>>> requestOtherCommonList(@QueryMap Map<String, Long> map);

    /**
     * 精选底部 三方 不可排序商品列表
     *
     * @param start
     * @param limit
     * @param deviceType 智能匹配-设备号类型：IMEI，或者IDFA，或者UTDID（UTDID不支持MD5加密
     * @return
     */
    @GET("goods-handpick/v1/list-handpick")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<PreciseListBean>>> requestPreciseSelectionList(@Query("start") int start, @Query("limit") int limit, @Query("deviceType") String deviceType);

    /**
     * 达人推荐列表
     * @param start
     * @param limit
     * @return
     */
    @GET("recommend-dou-commodity/v1/list")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<PreciseListBean>>> requestPreciseDarenRecommendList(@Query("start") int start, @Query("limit") int limit);

    /**
     * 二级类目 本地 可排序商品列表
     *
     * @return
     */
    @GET("commodity/v1/second-level")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<RecordsBean>>> requestSecondLevelList(@QueryMap Map<String, Long> map);

    /**
     * 种草文章分页查询
     *
     * @return
     */
    @GET("guide-article/v1/page")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<GuideArticleBean>>> requestGetGuideArticleList();


    /**
     * 种草 文章详情
     * @param articleType 文章类型 0 本地文章 1 三方文章
     * @param articleId  文章id
     * @return
     */
    @GET("guide-article/v1/info")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<GuideArticleDetailBean>> requestGetGuideArticleDetail(@Query("articleType") int articleType,
                                                                              @Query("articleId") long articleId);

    /**
     * 金刚区跳转后的商品数据列表
     *
     * @param map
     * @return
     */
    @GET("commodity/third/v1/slice-data")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<DiamonKongBean>> requestDiamondKongList(@QueryMap Map<String, String> map);


    /**
     * 分页查询99元礼包商品列表
     *
     * @param start 1开始
     * @param limit
     * @return
     */
    @GET("goods/v1/page")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<VipGoodListBean>> requestVipGoodsList(@Query("start") int start, @Query("limit") int limit);

    /**
     * 通过礼包id获取礼包详细信息接口
     *
     * @param id 礼包主键id
     * @param activityType 活动类型 1-新人0元购
     * @return
     */
    @GET("goods/v1/info")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<VipGoodsBean>> requestVipGoodsDetail(@Query("id") long id,@Query("activityType") int activityType);

    /**
     * 获取 红包弹框 数据
     *
     * @return
     */
    @GET("income/v1/red-gift")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<RedGiftBean>> requestRedGiftPackageInfo();

    /**
     * 商学院banner图
     *
     * @return
     */
    @GET("banner/v1/business")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<PreciseBannerIconBean>>> requestBusinessBannerList();


}
