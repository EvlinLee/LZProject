package com.by.lizhiyoupin.app.activity.presenter;

import com.by.lizhiyoupin.app.activity.constract.ShakeCouponMainConstract;
import com.by.lizhiyoupin.app.activity.model.ShakeCouponMainModel;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBean;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBuyBean;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/15 17:59
 * Summary:
 */
public class ShakeCouponMainPresenter extends ShakeCouponMainConstract.ShakeCouponMainPresenters {
    private ShakeCouponMainConstract.ShakeCouponMainModel mCouponMainModel;

    public ShakeCouponMainPresenter(ShakeCouponMainConstract.ShakeCouponMainView shakeCouponMainView) {
        mCouponMainModel = new ShakeCouponMainModel();
    }

    @Override
    public void requestShakeCouponMainList2(int hourType, int start, int limit, long itemId) {
        mCouponMainModel.requestShakeCouponMainList2(hourType, start, limit, itemId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<ShakeCouponBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<ShakeCouponBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (listBaseBean.success() && listBaseBean.data != null) {
                            getMVPView().requestShakeCouponMainSuccess(listBaseBean.getResult());
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMVPView().requestShakeCouponMainError(throwable);
                    }
                });
    }

    @Override
    public void requestPreciseShakeCouponList(int hourType, int start, int limit, long itemId, int type) {
        mCouponMainModel.requestPreciseShakeCouponList(hourType, start, limit, itemId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<ShakeCouponBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<ShakeCouponBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (listBaseBean.success() && listBaseBean.data != null) {
                            getMVPView().requestShakeCouponMainSuccess(listBaseBean.getResult());
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMVPView().requestShakeCouponMainError(throwable);
                    }
                });
    }

    @Override
    public void requestShakeCouponBarrageList() {
        mCouponMainModel.requestShakeCouponBarrageList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<ShakeCouponBuyBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<ShakeCouponBuyBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (listBaseBean.success() && listBaseBean.data != null) {
                            getMVPView().requestShakeCouponBarrageListSuccess(listBaseBean.getResult());
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMVPView().requestShakeCouponBarrageListError(throwable);
                    }
                });
    }
}
