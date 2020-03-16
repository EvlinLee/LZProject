package com.by.lizhiyoupin.app.user.presenter;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.user.contract.OrderRetrieveContract;
import com.by.lizhiyoupin.app.user.model.OrderRetrieveModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/13 10:04
 * Summary:
 */
public class OrderRetrievePresenter extends OrderRetrieveContract.OrderRetrievePresenters {
    private OrderRetrieveContract.OrderRetrieveModel mRetrieveModel;

    public OrderRetrievePresenter(OrderRetrieveContract.OrderRetrieveView view) {
        mRetrieveModel = new OrderRetrieveModel();
    }

    @Override
    public void requestIncomeRecord(String orderId) {
        mRetrieveModel.requestOrderRetrieve(orderId).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> objectBaseBean) {
                        super.onNext(objectBaseBean);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMVPView().requestOrderRetrieveSuccess(objectBaseBean.success());


                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        getMVPView().requestOrderRetrieveError(throwable);
                    }
                });
    }
}
