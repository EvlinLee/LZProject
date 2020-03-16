package com.by.lizhiyoupin.app.user.presenter;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.PushEditBean;
import com.by.lizhiyoupin.app.io.bean.PushMessageDescBean;
import com.by.lizhiyoupin.app.user.contract.PushEditContract;
import com.by.lizhiyoupin.app.user.model.PushEditModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/3 17:15
 * Summary: 运营商push消息管理 Presenter
 */
public class PushEditPresenter extends PushEditContract.IPushEditPresenter {
    private PushEditContract.IPushEditModel mIPushEditModel;

    public PushEditPresenter(PushEditContract.IPushEditView iPushEditView) {
        mIPushEditModel = new PushEditModel();
    }


    @Override
    public void requestPushEdit(String time) {
        mIPushEditModel.requestPushEditTips(time)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<Integer>>() {
                    @Override
                    public void onNext(BaseBean<Integer> integerBaseBean) {
                        super.onNext(integerBaseBean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (integerBaseBean.success() && integerBaseBean.data != null) {
                            getMVPView().requestPushEditTipsSuccess(integerBaseBean.data);
                        } else {
                            onError(new Throwable(integerBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMVPView().requestPushEditTipsError(throwable);
                    }
                });
    }

    @Override
    public void requestGetPushDetailInfo(long messageId) {
        mIPushEditModel.requestGetPushDetailInfo(messageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<PushEditBean>>() {
                    @Override
                    public void onNext(BaseBean<PushEditBean> beanBaseBean) {
                        super.onNext(beanBaseBean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (beanBaseBean.success() && beanBaseBean.data != null) {
                            getMVPView().requestGetPushDetailInfoSuccess(beanBaseBean.data);
                        } else {
                            onError(new Throwable(beanBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMVPView().requestGetPushDetailInfoError(throwable);
                    }
                });
    }

    /**
     * 提交/新增 消息
     *
     * @param editBean
     */
    @Override
    public void requestAddPushDetailInfo(PushEditBean editBean) {
        mIPushEditModel.requestAddPushDetailInfo(editBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> beanBaseBean) {
                        super.onNext(beanBaseBean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (beanBaseBean.success()) {
                            getMVPView().requestAddPushDetailInfoSuccess();
                        } else {
                            onError(new Throwable(beanBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMVPView().requestAddPushDetailInfoError(throwable);
                    }
                });
    }

    @Override
    public void requestUpdatePushDetailInfo(PushEditBean editBean) {
        mIPushEditModel.requestUpdatePushDetailInfo(editBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> beanBaseBean) {
                        super.onNext(beanBaseBean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (beanBaseBean.success()) {
                            getMVPView().requestUpdatePushDetailInfoSuccess();
                        } else {
                            onError(new Throwable(beanBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMVPView().requestUpdatePushDetailInfoError(throwable);
                    }
                });
    }

    @Override
    public void requestPushMessageDescList() {
        mIPushEditModel.requestPushMessageDescList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<PushMessageDescBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<PushMessageDescBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (listBaseBean.success() && listBaseBean.data != null) {
                            getMVPView().requestPushMessageDescListSuccess(listBaseBean.data);
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
                        getMVPView().requestPushMessageDescListError(throwable);
                    }
                });
    }
}
