package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.AddressBean;
import com.by.lizhiyoupin.app.io.bean.ChildcountBean;
import com.by.lizhiyoupin.app.io.bean.PlusTimeBean;
import com.by.lizhiyoupin.app.io.bean.VipGoodListBean;
import com.by.lizhiyoupin.app.io.bean.VipPowerBean;
import com.by.lizhiyoupin.app.io.entity.RequestAddressEntity;
import com.by.lizhiyoupin.app.io.entity.RequestPosterBean;
import com.by.lizhiyoupin.app.io.entity.SharePosterEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/22 17:42
 * Summary:
 */
public interface VipApi {
    /**
     * 获取会员权益信息数据接口
     *
     * @param phone
     * @return
     */
    @GET("user-equity/v1/list")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<VipPowerBean>>> requestVipPowerList(@Query("phone") String phone);

    /**
     * 分页查询99元礼包商品
     *
     * @param start 1开始
     * @param limit
     * @return
     */
    @Deprecated
    @GET("goods/v1/page")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<VipGoodListBean>> requestVipGoodsList(@Query("start") int start, @Query("limit") int limit);

    /**
     * 根据会员 token查询当前会员设置的地址接口
     * @param apiToken
     * @return
     */
    @GET("user-address/v1/list")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<AddressBean>>> requestGetUserAddress(@Query("apiToken") String apiToken);

    /**
     * 新增/修改会员地址接口(若id填写了已有的地址id则是修改)
     * id	        否	Long	主键id
     * userId	    是	Long	会员id
     * consignee	是	String	收货人
     * mobile	    是	String	手机号
     * provinceName	是	String	省名
     * cityName	    是	String	市名
     * districtName	是	String	区名
     * streetName	是	String	街道
     * address	    是	String	全详细地址
     * isDefault	是	int	    是否默认收货地址 0.否 1.是
     * @param entity
     * @return
     */
    @POST("user-address/v1/info")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Observable<BaseBean<AddressBean>> requestUpdateUserAddress(@Body RequestAddressEntity entity);

    /**
     * 查询下属会员下单人数
     *
     * @return
     */
    @GET("user/v1/child-count")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<ChildcountBean>> requestGetUpgrade(@Query("type") int type);

    /**
     * plus会员任务开启
     *
     * @return
     */
    @POST("plus-mission/v1/info")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<String>> requestGetPlus();


    /**
     * 运营商plus任务开启
     *
     * @return
     */
    @POST("operator-mission/v1/info")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<String>> requestGetOperator();


    /**
     * 查询plus会员任务日期
     *
     * @return
     */
    @GET("plus-mission/v1/info")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<PlusTimeBean>> requestGetPlusTime();

    /**
     * 查询运营商plus会员任务日期
     *
     * @return
     */
    @GET("operator-mission/v1/info")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<PlusTimeBean>> requestGetOperatorTime();

    /**
     * 获取分享海报图 集合
     * @param goodsId 商品id
     * @param platformType 平台
     * @return
     */
    @PUT("ring-info/v1/share-img")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Observable<BaseBean<SharePosterEntity>> requestSharePosterList(@Body List<RequestPosterBean> requestBody);

}
