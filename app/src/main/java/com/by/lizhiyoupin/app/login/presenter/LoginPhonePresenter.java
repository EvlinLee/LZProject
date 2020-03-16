package com.by.lizhiyoupin.app.login.presenter;

import android.graphics.Color;
import android.view.Gravity;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.login.LoginModel;
import com.by.lizhiyoupin.app.login.contract.LoginContract;
import com.by.lizhiyoupin.app.message_box.MessageBox;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/23 20:10
 * Summary:
 */
public class LoginPhonePresenter extends LoginContract.LoginPresenter {
    public static final String TAG=LoginPhonePresenter.class.getSimpleName();
    private AppCompatActivity mContext;
    private LoginContract.LoginView mLoginView;
    private LoginContract.LoginModel mLoginModel;
    public LoginPhonePresenter(AppCompatActivity context, LoginContract.LoginView view) {
        this.mContext=context;
        this.mLoginView=view;
        mLoginModel=new LoginModel();

    }
    @Override
    public void requestJudgePhoneRegister(String phone) {
        mLoginModel.requestJudgePhoneRegister(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<Boolean>>() {
                    @Override
                    public void onNext(BaseBean<Boolean> bean) {
                        super.onNext(bean);
                        if (bean.success()){
                            mLoginView.requestJudgePhoneRegisterSuccess(false);
                        } else if ("208".equals(bean.code)){
                            //已绑定，需要弹框
                            mLoginView.requestJudgePhoneRegisterSuccess(true);
                        }else {
                            onError(new Throwable("请求失败"));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mLoginView.requestJudgePhoneRegisterError(throwable);
                    }
                });
    }

    public void showTipDialog(){
        MessageBox.builder()
                .contentMessage("该手机号已绑定其他微信", 10f, 1.5f)
                .contentGravity(Gravity.CENTER)
                .bgColor(Color.WHITE)
                .radius(20, 20, 20, 20)
                .Ok("去登录")
                .okTextBold(true)
                .okTextColorRes(R.color.color_D60050)
                .Cancel("换个号码")
                .cancelTextColorRes(R.color.color_999999)
                .okClickListener((buttonView, arguments, tag, text, position) -> {
                    mLoginView.toLogin();
                })
                .cancelClickListener((buttonView, arguments, tag, text, position) -> {
                    mLoginView.changePhone();
                })

                .build("login")
                .show(mContext.getSupportFragmentManager());
    }
}
