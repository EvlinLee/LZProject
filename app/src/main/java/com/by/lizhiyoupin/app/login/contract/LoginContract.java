package com.by.lizhiyoupin.app.login.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/23 20:16
 * Summary:
 */
public interface LoginContract {
    interface LoginView extends BaseView {
        void changePhone();
        void toLogin();
        void requestJudgePhoneRegisterSuccess(Boolean  register);
        void requestJudgePhoneRegisterError(Throwable throwable);
    }


    interface  LoginModel extends BaseModel{

        Observable<BaseBean<Boolean>> requestJudgePhoneRegister(String code);
    }

    abstract class LoginPresenter extends BasePresenter<LoginView> {
        public abstract  void showTipDialog();

        /**
         * 请求 手机注册登录
         */
        public abstract void requestJudgePhoneRegister(String phone);
    }
}
