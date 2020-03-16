package com.by.lizhiyoupin.app.user.presenter;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.PresentationDetailsBean;
import com.by.lizhiyoupin.app.io.bean.WithDrawtiBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawDetaisBean;
import com.by.lizhiyoupin.app.io.bean.WithdrawaccountBean;
import com.by.lizhiyoupin.app.user.contract.WithdrawContract;
import com.by.lizhiyoupin.app.user.model.WithdrawModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * data:2019/11/18
 * author:jyx
 * function:
 */
public class WithdrawPresenter extends WithdrawContract.WithdrawPresenters{
    private WithdrawContract.WithdrawModel mWithdrawModel;
    private WithdrawContract.WithdrawView WithdrawView;
    public WithdrawPresenter(WithdrawContract.WithdrawView view) {
        this.WithdrawView = view;
        mWithdrawModel = new WithdrawModel();
    }
//会员信息
    @Override
    public void requestWithdraw() {
        mWithdrawModel.requestWithdrawView()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<WithdrawaccountBean>>() {
                    @Override
                    public void onNext(BaseBean<WithdrawaccountBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.getResult() != null) {
                            WithdrawView.requestWithdrawSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        WithdrawView.requestWithdrawError(throwable);
                    }
                });
    }
//提现列表
    @Override
    public void requestPresentationDetails(int start,int limit) {
        mWithdrawModel.requestPresentationDetailsView(start,limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<PresentationDetailsBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<PresentationDetailsBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.getResult() != null) {
                            WithdrawView.requestPresentationDetailsSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        WithdrawView.requestPresentationDetailsError(throwable);
                    }
                });
    }
//提现记录详情
    @Override
    public void requestWithdrawDetails(Long eid) {
        mWithdrawModel.requestWithdrawDetailsView(eid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<WithdrawDetaisBean>>() {
                    @Override
                    public void onNext(BaseBean<WithdrawDetaisBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.getResult() != null) {
                            WithdrawView.requestWithdrawDetailsSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        WithdrawView.requestWithdrawDetailsError(throwable);
                    }
                });
    }
//增加提现记录
    @Override
    public void requestWithdrawOperator(WithDrawtiBean bean) {
        mWithdrawModel.requestWithdrawOperatorView(bean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<WithdrawBean>>() {
                    @Override
                    public void onNext(BaseBean<WithdrawBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.getResult() != null) {
                            WithdrawView.requestWithdrawOperatorSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        WithdrawView.requestWithdrawOperatorError(throwable);
                    }
                });
    }
}
