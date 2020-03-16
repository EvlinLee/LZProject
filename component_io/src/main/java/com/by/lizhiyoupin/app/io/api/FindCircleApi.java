package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.BusinessArticleBean;
import com.by.lizhiyoupin.app.io.bean.BusinessIconBean;
import com.by.lizhiyoupin.app.io.bean.FindCircleChildListBean;
import com.by.lizhiyoupin.app.io.bean.FindCircleTabListBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/19 15:10
 * Summary:
 */
public interface FindCircleApi {
    /**
     * 查询发圈一级类目数据
     *
     * @return
     */
    @GET("ring-kind/v1/first-level")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<FindCircleTabListBean>>> requestFindCircleTabList();

    /**
     * 查询发圈二级类目数据接口
     *
     * @param superiorId 上级类目id
     * @return
     */
    @GET("ring-kind/v1/second-level")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<FindCircleTabListBean>>> requestTabSecondList(@Query("superiorId") long superiorId);

    /**
     * 根据一级类目和二级类目id分页查询发圈内容数据接口
     * 列表接口
     *
     * @param ringFirstKindId
     * @param ringSecondKindId
     * @param start
     * @param limit
     * @return
     */
    @GET("ring-info/v1/page")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<FindCircleChildListBean>>> requestTabChildList(@Query("ringFirstKindId") long ringFirstKindId,
                                                                            @Query("ringSecondKindId") long ringSecondKindId,
                                                                            @Query("start") int start,
                                                                            @Query("limit") int limit);

    /**
     * 每日爆款 列表
     * @param currentPage 页码
     * @return
     */
    @GET("ring-info/v1/day-bomb")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<FindCircleChildListBean>>> requestDailyExplosionsList(@Query("currentPage") String currentPage);

    /**
     * 商学院 一级类目即icon列表
     *
     * @return
     */
    @GET("business-kind/v1/first-kind")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<BusinessIconBean>>> requestBusinessIconList();

    /**
     *  商学院 --滚动资讯,资讯列表
     * 获取荔枝资讯商学院文章列表
     *
     * @param start
     * @param limit
     * @return
     */
    @GET("business-article/v1/information-page")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<BusinessArticleBean>>> requestBusinessScrollArticleList(@Query("start") int start,
                                                                                     @Query("limit") int limit);



    /**
     *  商学院 --推荐列表
     * 查询为你推荐商学院文章
     * @param start
     * @param limit
     * @return
     */
    @GET("business-article/v1/recommend-page")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<BusinessArticleBean>>> requestBusinessGuessList(@Query("start") int start,
                                                                             @Query("limit") int limit);

    /**
     *  商学院根据一级类目id获取二级类目信息接口
     *  即获取文章分类
     * @param superiorId  上级id
     * @return
     */

    @GET("business-kind/v1/second-kind")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<BusinessIconBean>>> requestBusinessSecondTabList(@Query("superiorId") long superiorId);

    /**
     * 商学院 二级文章列表
     * @param schoolFirstKindId
     * @param schoolSecondKindId
     * @param start
     * @param limit
     * @return
     */
    @GET("business-article/v1/second-article")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<BusinessArticleBean>>> requestBusinessSecondList(@Query("schoolFirstKindId") long schoolFirstKindId,
                                                                              @Query("schoolSecondKindId") long schoolSecondKindId,
                                                                              @Query("start") int start,
                                                                              @Query("limit") int limit);

    /**
     * 商学院 搜索结果
     * @param articleTitle
     * @param start
     * @param limit
     * @return
     */
    @GET("business-article/v1/search-article")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<BusinessArticleBean>>> requestBusinessSearchList(@Query("articleTitle") String articleTitle,
                                                                           @Query("start") int start,
                                                                           @Query("limit") int limit);

    /**
     * 根据文章主键id添加 发圈的分享次数
     * @param id
     * @return
     */
    @PUT("ring-info/v1/share-number")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    @FormUrlEncoded
    Observable<BaseBean<Object>> requestAddShareTimes(@Field("id") long id);


}
