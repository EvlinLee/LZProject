package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.IncomeDetailsVO;
import com.by.lizhiyoupin.app.io.bean.OrderLogisticsBean;
import com.by.lizhiyoupin.app.io.bean.OrderSupportBean;
import com.by.lizhiyoupin.app.io.bean.WeiXinPayVO;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * data:2019/10/30
 * author:jyx
 * function:
 */
public interface OrderApi {
    /**
     * 支付宝下单接口
     * @param gid  商品id
     * @return
     */
    @POST("goods-order/v1/order")
    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<String>> requestPaymentOrder(@Field("gid") Long gid,@Field("type") int type,@Field("userAddressId") Long userAddressId);

    /**
     * 微信下单接口
     * @param gid  商品id
     * @return
     */
    @POST("goods-order/v1/order")
    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<WeiXinPayVO>> requestWXPaymentOrder(@Field("gid") Long gid, @Field("type") int type,@Field("userAddressId") Long userAddressId);

    /**
     *
     * @param desc 排序 0降序 1升序
     * @param start 起始页
     * @param limit 每页显示多少条，默认10条
     * @param platformType 平台类型 0 全部 1淘宝 2京东 3拼多多
     * @param orderStatus 订单状态 0全部 1待返佣 2已到账 3失效
     * @param orderType  必选  订单类型 1我的订单 2团队订单
     * @param coid 订单号
     * @return
     */
    @GET("commodity-order/v1/my-order")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<IncomeDetailsVO>>> requestGetMyOrderList(@Query("desc") int desc,
                                                                      @Query("start") int start,
                                                                      @Query("limit") int limit,
                                                                      @Query("platformType") int platformType,
                                                                      @Query("orderStatus") int orderStatus,
                                                                      @Query("orderType") int orderType,
                                                                      @Query("coid") String coid);


    /**
     *
     * @param desc 排序 0降序 1升序
     * @param start 起始页
     * @param limit 每页显示多少条，默认10条
     * @param orderStatus 	订单状态 10全部 0关闭 1正常 2待付款
     * @return
     */
    @GET("goods-order/v1/my-order")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<OrderSupportBean>>> requestOrderSupportList(@Query("desc") int desc,
                                                                         @Query("start") int start,
                                                                         @Query("limit") int limit,
                                                                         @Query("orderStatus") int orderStatus
                                                                     );
    /**
     * 订单找回
     * @param coid 订单号
     * @return
     */
    @GET("commodity-order/v1/retrieve")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<Object>> requestOrderFindBack(@Query("coid") String coid);


    /**
     * 99元礼包（自营）订单物流信息查询
     * @param number 物流单号
     * @param company 物流公司名称
     * @return
     */
    @GET("goods-order/v1/logistics")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<OrderLogisticsBean>> requestOrderLogistics(@Query("company") String company,
                                                                         @Query("number") String number);
}
