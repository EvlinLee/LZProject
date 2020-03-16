package com.by.lizhiyoupin.app.user.presenter;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.FansCountBean;
import com.by.lizhiyoupin.app.user.contract.FansContrat;
import com.by.lizhiyoupin.app.user.model.FansMainModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/9 15:26
 * Summary:
 */
public class FansMainPresenter extends FansContrat.FansMainPresenters {
    public static final String TAG = FansMainPresenter.class.getSimpleName();
    private FansContrat.FansMainModel mFansMainModel;

    public FansMainPresenter(FansContrat.FansMainView view) {
        mFansMainModel = new FansMainModel();
    }

    @Override
    public void requestFansMain() {
        mFansMainModel.requestFansMain().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<FansCountBean>>() {
                    @Override
                    public void onNext(BaseBean<FansCountBean> fansCountBeanBaseBean) {
                        super.onNext(fansCountBeanBaseBean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (fansCountBeanBaseBean.success() && fansCountBeanBaseBean.getResult() != null) {
                            getMVPView().requestFansMainSuccess(fansCountBeanBaseBean.data);
                        } else {
                            onError(new Throwable(fansCountBeanBaseBean.msg));
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMVPView().requestFansMainError(throwable);
                    }
                });
    }
}
