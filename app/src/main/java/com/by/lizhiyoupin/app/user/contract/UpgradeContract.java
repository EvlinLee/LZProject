package com.by.lizhiyoupin.app.user.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.ChildcountBean;
import com.by.lizhiyoupin.app.io.bean.PlusTimeBean;

import io.reactivex.Observable;

/**
 * data:2019/11/19
 * author:jyx
 * function:
 */
public interface UpgradeContract {
    interface  UpgradeView extends BaseView {
        void requestUpgradeSuccess(ChildcountBean bean);
        void requestUpgradeError(Throwable throwable);

        void requestPlusSuccess(boolean bean);
        void requestPlusError(Throwable throwable);

        void requestOperatorSuccess(boolean bean);
        void requestOperatorError(Throwable throwable);

        void requestOperatorTimeSuccess(PlusTimeBean bean);
        void requestOperatorTimeError(Throwable throwable);

        void requestPlusTimeSuccess(PlusTimeBean bean);
        void requestPlusTimeError(Throwable throwable);

    }

    interface UpgradeModel extends BaseModel {
        Observable<BaseBean<ChildcountBean>> requestUpgradeView(int type);
        Observable<BaseBean<String>> requestPlusView();
        Observable<BaseBean<String>> requestOperatorView();
        Observable<BaseBean<PlusTimeBean>> requestOperatorTimeView();
        Observable<BaseBean<PlusTimeBean>> requestPlusTimeView();


    }
    abstract class UpgradePresenters extends BasePresenter<UpgradeContract.UpgradeView> {

        public abstract void requestUpgrade(int type);
        public abstract void requestPlus();
        public abstract void requestOperator();
        public abstract void requestOperatorTime();
        public abstract void requestPlusTime();

    }
}
