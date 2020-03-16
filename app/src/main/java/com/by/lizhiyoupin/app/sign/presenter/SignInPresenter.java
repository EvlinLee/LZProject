package com.by.lizhiyoupin.app.sign.presenter;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.SignInBean;
import com.by.lizhiyoupin.app.io.bean.SignInRedPaperBean;
import com.by.lizhiyoupin.app.sign.contract.SignInContract;
import com.by.lizhiyoupin.app.sign.model.SignInModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/9 14:32
 * Summary:
 */
public class SignInPresenter extends SignInContract.ISignInPresenter {
    private SignInContract.ISignInModel mISignInModel;

    public SignInPresenter(SignInContract.ISignInView iSignInView) {
        mISignInModel = new SignInModel();
    }
    /**
     * 获取会员昨天今天明天的 签到信息
     * @param remindType 提醒类型(手机APP是否允许通知) 0 提醒关闭 1 提醒成功
     * @return
     */
    @Override
    public void requestSignInInfo(int remindType) {
        mISignInModel.requestSignInInfo(remindType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<SignInBean>>() {
                    @Override
                    public void onNext(BaseBean<SignInBean> bean) {
                        super.onNext(bean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (bean.success()&&bean.data!=null){
                            getMVPView().requestSignInInfoSuccess(bean.getResult());
                        }else {
                            onError(new Throwable(bean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMVPView().requestSignInInfoError(throwable);
                    }
                });
    }

    @Override
    public void requestSignInRedPaper(int popupType) {
        String uniqueId = DeviceUtil.getUniqueId(LiZhiApplication.getApplication());
        mISignInModel.requestSignInRedPaper(popupType,uniqueId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<SignInRedPaperBean>>() {
                    @Override
                    public void onNext(BaseBean<SignInRedPaperBean> bean) {
                        super.onNext(bean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (bean.success()&&bean.data!=null){
                            bean.getResult().setShowDialog(true);
                            getMVPView().requestSignInRedPaperSuccess(bean.getResult());
                        }else if (BaseBean.CODE_PRODUCT_RED_NO_DIALOG_SUCCESS.equals(bean.code)){
                            //不用弹出 红包签到框
                            if (bean.data!=null){
                                bean.data.setShowDialog(false);
                            }else {
                                bean.data=new SignInRedPaperBean(false);
                            }
                            getMVPView().requestSignInRedPaperSuccess(bean.getResult());
                        }else {
                            onError(new Throwable(bean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMVPView().requestSignInRedPaperError(throwable);

                    }
                });
    }

    @Override
    public void requestSignInEveryDay(double signBonusAmount) {
        //签到领取红包接口 即签到 收下红包
        mISignInModel.requestSignInEveryDay(signBonusAmount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> bean) {
                        super.onNext(bean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (bean.success()){
                            getMVPView().requestSignInEveryDaySuccess(bean.getResult());
                        }else {
                            onError(new Throwable(bean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMVPView().requestSignInEveryDayError(throwable);

                    }
                });
    }

    @Override
    public void requestSignInPushSwitch(int remindType) {
        mISignInModel.requestSignInPushSwitch(remindType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> bean) {
                        super.onNext(bean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (bean.success()){
                            getMVPView().requestSignInPushSwitchSuccess(bean.msg);
                        }else {
                            onError(new Throwable(bean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMVPView().requestSignInPushSwitchError(throwable);

                    }
                });
    }
}
