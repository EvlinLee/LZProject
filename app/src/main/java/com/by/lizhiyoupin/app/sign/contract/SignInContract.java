package com.by.lizhiyoupin.app.sign.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.SignInBean;
import com.by.lizhiyoupin.app.io.bean.SignInRedPaperBean;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/9 14:24
 * Summary:
 */
public interface SignInContract {

    interface ISignInView extends BaseView {
        void requestSignInInfoSuccess(SignInBean bean);
        void requestSignInInfoError(Throwable throwable);

        void requestSignInRedPaperSuccess(SignInRedPaperBean bean);
        void requestSignInRedPaperError(Throwable throwable);

        void requestSignInEveryDaySuccess(Object bean);
        void requestSignInEveryDayError(Throwable throwable);

        void requestSignInPushSwitchSuccess(String msg);
        void requestSignInPushSwitchError(Throwable throwable);
    }

    interface ISignInModel extends BaseModel {
        Observable<BaseBean<SignInBean>> requestSignInInfo(int remindType);
        Observable<BaseBean<SignInRedPaperBean>> requestSignInRedPaper(int popupType, String equipmentId);
        Observable<BaseBean<Object>> requestSignInEveryDay(double signBonusAmount);
        Observable<BaseBean<Object>> requestSignInPushSwitch(int remindType );

    }

    abstract class ISignInPresenter extends BasePresenter<ISignInView> {


        public abstract void requestSignInInfo(int remindType);

        /**
         * 是否弹出框并获取显示红包框金额
         * @param popupType 弹窗类型 0 首页 1 签到页
         */
        public abstract void requestSignInRedPaper(int popupType);

        /**
         * 签到领红包
         * @param signBonusAmount
         */
        public abstract void requestSignInEveryDay(double signBonusAmount);

        /**
         * 签到推送开关
         * @param remindType 提醒类型 0 不提醒 1 提醒
         * @return
         */
        public abstract void requestSignInPushSwitch(int remindType );

    }
}
