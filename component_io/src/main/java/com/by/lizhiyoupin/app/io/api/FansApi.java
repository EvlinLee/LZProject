package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.FansDataBean;
import com.by.lizhiyoupin.app.io.bean.FansDetailBean;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/11 15:39
 * Summary:
 */
public interface FansApi {


    /**
     * 获取粉丝列表
     *
     * @param type     粉丝类型 0全部 1专属 2普通【如果0时是用于搜索】
     * @param key      关键字匹配【手机号、昵称】
     * @param sortType 排序类型 1时间 2收益 3粉丝数
     * @param sortDesc 排序 0降序 1升序
     * @param start
     * @param limit
     * @return
     */
    @GET("fans/v1/fans-list")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<FansDataBean>> requestGetFansList(@Query("type") int type,
                                                          @Query("key") String key,
                                                          @Query("sortType") int sortType,
                                                          @Query("sortDesc") int sortDesc,
                                                          @Query("start") int start,
                                                          @Query("limit") int limit);

    /**
     * 获取粉丝列表
     * @param uid
     * @return
     */
    @GET("fans/v1/fans-details")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<FansDetailBean>> requestGetFansDetail(@Query("uid") Long uid);


    /**
     * 添加push粉丝时 查询橱窗商品列表
     * @param start
     * @param limit
     * @return
     */
    @GET("recommend-dou-commodity/v1/push-list")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<BaseBean<List<PreciseListBean>>> requestGetPushGoodsList(@Query("start") int start, @Query("limit") int limit);



}
