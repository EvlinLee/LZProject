package com.by.lizhiyoupin.app.manager;

import com.by.lizhiyoupin.app.common.ResponseCallback;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.SearchTaoBean;
import com.by.lizhiyoupin.app.io.service.ApiService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/14 10:17
 * Summary:
 */
public class RequestDataManager {

    public static void requestSearchTaoList(String clip, ResponseCallback<SearchTaoBean> callback) {
        ApiService.getSearchApi().requestSearchTaoList(clip)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<SearchTaoBean>>() {
                    @Override
                    public void onNext(BaseBean<SearchTaoBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.data != null) {
                            callback.success(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        callback.error(throwable);
                    }
                });
    }
}
