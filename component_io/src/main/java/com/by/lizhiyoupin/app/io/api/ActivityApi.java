package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.LimitedTimeListBean;
import com.by.lizhiyoupin.app.io.bean.LimitedTimeSkillBean;
import com.by.lizhiyoupin.app.io.bean.LimitedTimeSkillTitleBean;
import com.by.lizhiyoupin.app.io.bean.LimitedTimeTitleBean;
import com.by.lizhiyoupin.app.io.bean.ProduclimitSkilltListBean;
import com.by.lizhiyoupin.app.io.bean.ProductListBean;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBean;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBuyBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/13 15:37
 * Summary: 活动
 */
public interface ActivityApi {
    /**
     * 获取 值得买 列表
     *
     * @return
     */
    @GET("special/worth/buying/today")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<ProductListBean>>> requestWorthBuyingList();


    /**
     * 销量榜
     *
     * @param saleType 榜单类型：sale_type=1是实时销量榜（近2小时销量），type=2是今日爆单榜，type=3是昨日爆单榜，type=4是出单指数版
     * @param start    默认1
     * @param limit    1-100
     * @param cid      商品类目：0全部，1女装，2男装，3内衣，4美妆，5配饰，6鞋品，7箱包，8儿童，9母婴，10居家，11美食，12数码，13家电，14其他，15
     *                 车品，16文体，17宠物
     * @param itemType 是否只获取营销返利商品，1是，0否
     * @return
     */
    @GET("special/various/large/lists")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<ProductListBean>>> requestSaleList(@Query("saleType") int saleType,
                                                                @Query("minId") int start,
                                                                @Query("back") int limit,
                                                                @Query("cid") int cid,
                                                                @Query("itemType") int itemType);

    /**
     * 秒杀时间列表
     *
     * @return
     */
    @GET("special/rush/to/buy")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<LimitedTimeTitleBean>>> requestLimitedTimeTitleList();

    /**
     * 秒杀时间列表
     *
     * @return
     */
    @GET("time-limit/v1/batch")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<LimitedTimeSkillTitleBean>>> requestLimitedTimeSkillTitleList();

    /**
     * 首页限时秒杀
     *
     * @return
     */
    @GET("time-limit/v1/fast-buy")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<LimitedTimeSkillBean>>> requestLimitTimeSkillList();

    /**
     * 秒杀列表
     *
     * @param hourType 快抢时间点：1.昨天的0点，2.昨天10点，3.昨天12点，4.昨天15点，5.昨天20点，6.今天的0点，7.今天10点，8.今天12点，9
     *                 .今天15点，10.今天20点，11.明天的0点，12.明天10点，13.明天12点，14.明天15点，15.明天20点
     * @param start    默认1
     * @return
     */
    @GET("special/quick/grab/merchandise")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<LimitedTimeListBean>> requestLimitedTimeList(@Query("hourType") int hourType,
                                                                     @Query("minId") int start);


    /**
     * 秒杀列表
     *
     * @param batch 快抢时间点：1.昨天的0点，2.昨天10点，3.昨天12点，4.昨天15点，5.昨天20点，6.今天的0点，7.今天10点，8.今天12点，9
     *              .今天15点，10.今天20点，11.明天的0点，12.明天10点，13.明天12点，14.明天15点，15.明天20点
     * @param minId 默认1
     * @return
     */
    @GET("time-limit/v1/batch-commodity")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<ProduclimitSkilltListBean>>> requestLimitedSkillTimeList(@Query(
            "batch") int batch,
                                                                                      @Query("minId") int minId);

    /**
     * 添加商品 秒杀推送 提醒标记
     *
     * @param batch
     * @return
     */
    @GET("time-limit/v1/remind-commodity")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<Object>> requestAddPushRemindTag(@Query("batch") int batch,
                                                         @Query("commodityId") String commodityId,
                                                         @Query("fastBuyCommodityType") int fastBuyCommodityType);

    /**
     * 删除商品 秒杀推送 提醒标记
     *
     * @param batch
     * @return
     */
    @GET("time-limit/v1/cancel-remind-commodity")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<Object>> requestDeletePushRemindTag(@Query("batch") int batch,
                                                            @Query("commodityId") String commodityId,
                                                            @Query("fastBuyCommodityType") int fastBuyCommodityType);

    /**
     * 抖券列表
     *
     * @param hourType
     * @param start
     * @param limit
     * @return
     */
    @GET("special/dy/commodity")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<ShakeCouponBean>>> requestShakeCouponList(@Query("catId") int hourType,
                                                                       @Query("minId") int start,
                                                                       @Query("back") int limit);

    /**
     * 抖券列表（可查询 自营配置数据）
     *
     * @param hourType 0 代表全部类型
     * @param start
     * @param limit
     * @param itemId   宝贝ID 0，在列表时不需要传递，在播放视频页查询是需传递当前视频的宝贝ID用于去重
     * @param type     类别 0-活动配置里面的抖券(三方) ，1-首页底部tab的抖券(1 三方+自营数据)
     * @return
     */
    @GET("recommend-dou/v1/dou")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<ShakeCouponBean>>> requestPreciseShakeCouponList(@Query("catId") int hourType,
                                                                              @Query("minId") int start,
                                                                              @Query("back") int limit,
                                                                              @Query("itemId") String itemId,
                                                                              @Query("type") int type);

    /**
     * 抖券主页刷新列表
     *
     * @param hourType
     * @param start
     * @param limit
     * @param itemId
     * @return
     */
    @GET("special/dy/commodity")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<ShakeCouponBean>>> requestShakeCouponList2(@Query("catId") int hourType,
                                                                        @Query("minId") int start,
                                                                        @Query("back") int limit,
                                                                        @Query("itemId") long itemId);

    /**
     * 抖券轮播弹幕列表
     *
     * @return
     */
    @GET("special/user/purchase/status")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<ShakeCouponBuyBean>>> requestShakeCouponBarrageList();

}
