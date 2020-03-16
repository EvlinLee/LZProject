package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.ShoppingCartResponse;
import com.by.lizhiyoupin.app.io.entity.RequestShoppingCartEntity;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/18 14:31
 * Summary:
 */
public interface ShoppingApi {



    /**
     *  根据会员token获取购物车信息
     * @param apiToken
     * @param start
     * @param limit
     * @return
     */
    @GET("shopping-car/v1/page")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<ShoppingCartResponse>> requestGetShoppingCartList(@Query("apiToken") String apiToken,
                                                                          @Query("start") int start,
                                                                          @Query("limit") int limit);
    /**
     * 添加到购物车
     * @param entity
     * @return
     */
    @POST("shopping-car/v1/info")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Observable<BaseBean<RequestShoppingCartEntity>> requestAdd2ShoppingCartInfo(@Body RequestShoppingCartEntity entity);


    /**
     * 根据购物车主键id  删除购物车数据
     * @param shoppingId 购物车主键id
     * @return
     */
    @DELETE("shopping-car/v1/info")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<Object>> requestDeleteShoppingCartInfo(@Query("id") Long shoppingId);



}
