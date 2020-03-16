package com.by.lizhiyoupin.app.io.api;

import com.by.lizhiyoupin.app.io.BaseBean;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * data:2019/11/11
 * author:jyx
 * function:
 */
public interface AvatarApi {
    /**
     * @param file 上传图片文件
     * @return
     */
    @POST("save-file/v1/picture")
    @Multipart
    Observable<BaseBean<String>> requestAvatarUrl(@Part MultipartBody.Part file);
}
