package com.by.lizhiyoupin.app.main.presenter;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.CommonSecondBean;
import com.by.lizhiyoupin.app.main.contract.HomeCommonContract;
import com.by.lizhiyoupin.app.main.model.HomeCommonModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/16 17:23
 * Summary:
 */
public class HomeCommonPresenter extends HomeCommonContract.HomeCommonPresenter {
    private HomeCommonContract.HomeCommonModel mCommonModel;
    private HomeCommonContract.HomeCommonView mCommonView;

    public HomeCommonPresenter(HomeCommonContract.HomeCommonView view) {
        mCommonModel = new HomeCommonModel();
        this.mCommonView = view;
    }

    @Override
    public void requestSecondBannerIcon(Long id) {
        mCommonModel.requestSecondBannerIcon(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<CommonSecondBean>>() {
                    @Override
                    public void onNext(BaseBean<CommonSecondBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (!listBaseBean.success()) {
                            onError(new Throwable(listBaseBean.msg));
                            return;
                        }
                        if (mCommonView != null) {
                            mCommonView.requestSecondBannerIconSuccess(listBaseBean.data);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mCommonView.requestSecondBannerIconError(throwable);
                    }
                });
    }

}
