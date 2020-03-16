package com.by.lizhiyoupin.app.user.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/13 10:03
 * Summary:
 */
public interface OrderRetrieveContract {
    interface OrderRetrieveView extends BaseView {
        void requestOrderRetrieveSuccess(boolean b);

        void requestOrderRetrieveError(Throwable throwable);

    }

    interface OrderRetrieveModel extends BaseModel {

        Observable<BaseBean<Object>> requestOrderRetrieve(String orderId);

    }

    abstract class OrderRetrievePresenters extends BasePresenter<OrderRetrieveView> {

        public abstract void requestIncomeRecord(String orderId);

    }
}
