package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.bean.AliDetailPicBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/29 15:15
 * Summary:
 */
public interface DetailPicApi {
    //https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdesc/6.0/?data={"id":"596550857587","type":"0"}
    @GET("h5/mtop.taobao.detail.getdesc/6.0/")
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
    Observable<AliDetailPicBean> requestDetailPic(@Query("data") String json);

}
