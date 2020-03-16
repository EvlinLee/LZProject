package com.by.lizhiyoupin.app.user.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.SplitUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.login.LoginRequestManager;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.user.SettingRequestManager;
import com.by.lizhiyoupin.app.utils.EdittextUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*
*jyx
* 更换手机号页面
* */
@Route(path = "/app/Revise_NumberActivity")
public class Revise_NumberActivity extends BaseActivity implements View.OnClickListener,
        Handler.Callback {
    private TextView mTitle,actionbar_back_tv,getcode,current_phone;
    //倒计时60秒
    public static final int TIME_INTERVAL_COUNT = 60;
    public static final int KEY_COUNT_DOWN_CODE = 2001;
    private Handler mHandler;
    private LinearLayout number_submit,number_sub;
    private EditText number_phone,number_getsms;
    private String phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise__number);
        initImmersionBar(Color.WHITE,true);
        mHandler = new Handler(Looper.getMainLooper(), this);
        initBar();
        initView();
        initListener();
    }

    private void initListener() {
        // 监听多个输入框
        TextChange textChange = new TextChange();
        number_getsms.addTextChangedListener(textChange);

        number_phone.addTextChangedListener(textChange);

    }

    private void initView() {
        actionbar_back_tv.setOnClickListener(this);
          getcode = findViewById(R.id.getcode);
          getcode.setOnClickListener(this);//获取验证码
          number_submit = findViewById(R.id.number_submit);
          number_submit.setOnClickListener(this);//提交
        number_phone = findViewById(R.id.number_phone); //手机号
          current_phone = findViewById(R.id.current_phone);
          number_getsms = findViewById(R.id.number_getsms);
          number_sub=findViewById(R.id.number_sub);
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());

         phone = accountManager.getAccountInfo().getPhone();
        current_phone.setText("当前手机号码："+phone);

        EdittextUtil.StringWatcherPhone(number_phone);//手机号校验


    }

    @Override
    protected void onResume() {
        super.onResume();
        number_phone.setSelection(number_phone.getText().toString().length());
        DeviceUtil.showInputMethodDelay(number_phone,500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DeviceUtil.hideInputMethod(number_phone);
    }

    private void initBar() {
        findViewById(R.id.actionbar).setBackgroundColor(Color.WHITE);
        mTitle = findViewById(R.id.actionbar_title_tv);
        actionbar_back_tv= findViewById(R.id.actionbar_back_tv);
        actionbar_back_tv.setText("");
        mTitle.setText("更换手机号");
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()){
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.getcode:
                boolean mobile = SplitUtils.isMobile(number_phone.getText().toString().trim());
                if(!TextUtils.isEmpty(number_phone.getText().toString())&&number_phone.getText().toString().length()==11){
                    if (mobile){
                        setTimeCodeText(TIME_INTERVAL_COUNT);
                        setNumberSms();
                    }
                }else{
                    MessageToast.showToastBottom(Revise_NumberActivity.this,"请先输入正确的手机号",Gravity.CENTER);
                }
                break;
            case R.id.number_submit:
                if (number_phone.getText().toString().equals(phone)){
                    MessageToast.showToastBottom(this,"手机号码与原手机号一致，请重新输入",Gravity.CENTER);
                    return;
                }
                setSubmit();
                break;
        }
    }

    private void setSubmit() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        String apitoken = accountManager.getAccountInfo().getApiToken();
        SettingRequestManager.requestPhone(number_phone.getText().toString(),apitoken,number_getsms.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<UserInfoBean>>() {
                    @Override
                    public void onNext(BaseBean<UserInfoBean> userInfoBeanBaseBean) {
                        super.onNext(userInfoBeanBaseBean);
                        if (!userInfoBeanBaseBean.success()) {
                            onError(new Throwable(userInfoBeanBaseBean.msg));
                            return;
                        }

                        if (accountManager!=null&&userInfoBeanBaseBean.data!=null){

                            accountManager.saveAccountInfo(userInfoBeanBaseBean.data);
                        }
                        CommonToast.showToast("修改成功");
                        finish();
//                        LZLog.i(TAG, "修改成功");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        CommonToast.showToast("修改失败");
                    }
                });
    }

    private void setNumberSms() {

        LoginRequestManager.requestPutPhoneSms(number_phone.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<Boolean>>() {
                    @Override
                    public void onNext(BaseBean<Boolean> booleanBaseBean) {
                        super.onNext(booleanBaseBean);
                        if (!booleanBaseBean.success()){
                            onError(new Throwable(booleanBaseBean.msg));
                            return;
                        }
//                        LZLog.i(TAG, "发送验证码 成功==" + booleanBaseBean.data);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
//                        LZLog.i(TAG, "发送验证码 失败==" + throwable);
                    }
                });

    }

    private void setTimeCodeText(int time) {
        getcode.setEnabled(time <= 0);
        if (time > 0) {
            if (getcode != null) {

                getcode.setText(time+"s");
            }
            Message msg = Message.obtain();
            msg.what = KEY_COUNT_DOWN_CODE;
            msg.obj = time - 1;
            mHandler.sendMessageDelayed(msg, 1000);
        } else {
            setRetryCodeText();
        }
    }


    /**
     * 重新发送
     */
    private void setRetryCodeText() {
        if (getcode != null) {
            getcode.setText(getResources().getString(R.string.login_verification_time_retry_text));
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case KEY_COUNT_DOWN_CODE:
                Integer time = (Integer) msg.obj;
                setTimeCodeText(time);
                break;
            default:
                break;
        }
        return true;
    }

    // EditText监听器
    class TextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean Sign2 = number_getsms.getText().length() > 0;
            boolean Sign3 = number_phone.getText().length() > 0;
            if (Sign2 & Sign3){
                number_sub.setVisibility(View.GONE);
                number_submit.setVisibility(View.VISIBLE);
            }else{
                number_submit.setVisibility(View.GONE);
                number_sub.setVisibility(View.VISIBLE);
            }
        }
    }
}
