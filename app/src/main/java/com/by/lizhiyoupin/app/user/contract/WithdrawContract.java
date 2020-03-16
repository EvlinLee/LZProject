package com.by.lizhiyoupin.app.user.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.PresentationDetailsBean;
import com.by.lizhiyoupin.app.io.bean.WithDrawtiBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawDetaisBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawaccountBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * data:2019/11/18
 * author:jyx
 * function:
 */
public interface WithdrawContract {
    interface  WithdrawView extends BaseView {
        void requestWithdrawSuccess(WithdrawaccountBean bean);
        void requestWithdrawError(Throwable throwable);

        void requestPresentationDetailsSuccess(List<PresentationDetailsBean> list);
        void requestPresentationDetailsError(Throwable throwable);

        void requestWithdrawDetailsSuccess(WithdrawDetaisBean bean);
        void requestWithdrawDetailsError(Throwable throwable);

        void requestWithdrawOperatorSuccess(WithdrawBean bean);
        void requestWithdrawOperatorError(Throwable throwable);





    }

    interface WithdrawModel extends BaseModel {
        Observable<BaseBean<WithdrawaccountBean>> requestWithdrawView();
        Observable<BaseBean<List<PresentationDetailsBean>>> requestPresentationDetailsView(int start,int limit);
        Observable<BaseBean<WithdrawDetaisBean>> requestWithdrawDetailsView(Long eid);
        Observable<BaseBean<WithdrawBean>> requestWithdrawOperatorView(WithDrawtiBean bean);


    }
    abstract class WithdrawPresenters extends BasePresenter<WithdrawContract.WithdrawView> {

        public abstract void requestWithdraw();
        public abstract void requestPresentationDetails(int start,int limit);
        public abstract void requestWithdrawDetails(Long eid);
        public abstract void requestWithdrawOperator(WithDrawtiBean bean);

    }
}
