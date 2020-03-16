package com.by.lizhiyoupin.app.user.presenter;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.ChildcountBean;
import com.by.lizhiyoupin.app.io.bean.PlusTimeBean;
import com.by.lizhiyoupin.app.user.contract.UpgradeContract;
import com.by.lizhiyoupin.app.user.model.UpgradeModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * data:2019/11/19
 * author:jyx
 * function:
 */
public class UpgradePresenter extends UpgradeContract.UpgradePresenters {

    private UpgradeContract.UpgradeModel mUpgradeModel;
    private UpgradeContract.UpgradeView mUpgradeView;
    public UpgradePresenter(UpgradeContract.UpgradeView view) {
        this.mUpgradeView = view;
        mUpgradeModel = new UpgradeModel();
    }


    @Override
    public void requestUpgrade(int type) {
        mUpgradeModel.requestUpgradeView(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<ChildcountBean>>() {
                    @Override
                    public void onNext(BaseBean<ChildcountBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.getResult() != null) {
                            mUpgradeView.requestUpgradeSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mUpgradeView.requestUpgradeError(throwable);
                    }
                });
    }
//plus会员任务开启
    @Override
    public void requestPlus() {
        mUpgradeModel.requestPlusView()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<String>>() {
                    @Override
                    public void onNext(BaseBean<String> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() ) {
                            mUpgradeView.requestPlusSuccess(true);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mUpgradeView.requestPlusError(throwable);
                    }
                });
    }
//运营商plus任务开启
    @Override
    public void requestOperator() {
        mUpgradeModel.requestOperatorView()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<String>>() {
                    @Override
                    public void onNext(BaseBean<String> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()) {
                            mUpgradeView.requestOperatorSuccess(true);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mUpgradeView.requestOperatorError(throwable);
                    }
                });
    }
//查询运营商plus会员日期
    @Override
    public void requestOperatorTime() {
        mUpgradeModel.requestOperatorTimeView()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<PlusTimeBean>>() {
                    @Override
                    public void onNext(BaseBean<PlusTimeBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.getResult() != null) {
                            mUpgradeView.requestOperatorTimeSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mUpgradeView.requestOperatorTimeError(throwable);
                    }
                });
    }
    //查询plus会员日期
    @Override
    public void requestPlusTime() {
        mUpgradeModel.requestPlusTimeView()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<PlusTimeBean>>() {
                    @Override
                    public void onNext(BaseBean<PlusTimeBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.getResult() != null) {
                            mUpgradeView.requestPlusTimeSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mUpgradeView.requestPlusTimeError(throwable);
                    }
                });
    }
}
