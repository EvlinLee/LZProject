package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.SearchCommodityBean;
import com.by.lizhiyoupin.app.io.bean.SearchTaoBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/18 18:22
 * Summary:
 */
public interface SearchApi {
    /**
     * 查询一级类目列表
     * 即首页顶部类目列表
     * goodsId String 商品id,优先级高于关键词
     * keyWord 搜索关键词
     * apiType api类型 0 淘宝 1 京东 2 拼多多
     * sortType 排序类型 1 综合 2 销量 3 价格 4 佣金
     * orderByType 排序方式 false 倒序 true 正序
     * hasCoupon 是否有优惠券 false 没有 true 有
     * start 起始页(默认0)
     * limit 每页条数(默认10)
     *
     * @return
     */
    @GET("commodity-search/v1/api-search")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<SearchCommodityBean>>> requestSearchCommodityList(@QueryMap Map<String, Object> Map);

    /**
     * 淘口令
     *
     * @param taoCommand
     * @return
     */
    @POST("commodity/v1/parsing-taoCommand")
    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<SearchTaoBean>> requestSearchTaoList(@Field("taoCommand") String taoCommand);

    /**
     * 商品热搜关键字接口
     *
     * @return
     */
    @GET("commodity/v1/hot-search")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<String>>> requestSearchHotList();


}
