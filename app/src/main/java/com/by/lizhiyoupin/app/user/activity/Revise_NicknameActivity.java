package com.by.lizhiyoupin.app.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.user.SettingRequestManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*
 * jyx
 * 修改昵称页面
 * */
@Route(path = "/app/Revise_NicknameActivity")
public class Revise_NicknameActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTitle, actionbar_back_tv;
    private EditText nickname_edit;
    private LinearLayout name_sub, name_submit;
    private String nickname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise__nickname);
        initImmersionBar(Color.WHITE,true);
        nickname_edit = findViewById(R.id.nickname_edit);
        name_sub = findViewById(R.id.name_sub);
        name_submit = findViewById(R.id.name_submit);
        initBar();
        initView();

    }

    private void initView() {

        actionbar_back_tv.setOnClickListener(this);
        name_submit.setOnClickListener(this);
        TextChange textChange = new TextChange();
        nickname_edit.addTextChangedListener(textChange);

    }


    @Override
    protected void onResume() {
        super.onResume();

        nickname_edit.setSelection(nickname_edit.getText().toString().length());
        DeviceUtil.showInputMethodDelay(nickname_edit,500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DeviceUtil.hideInputMethod(nickname_edit);
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
            boolean Sign1 = nickname_edit.getText().length() > 0;

            if (Sign1) {
                name_sub.setVisibility(View.GONE);
                name_submit.setVisibility(View.VISIBLE);
            } else {
                name_sub.setVisibility(View.VISIBLE);
                name_submit.setVisibility(View.GONE);
            }
        }
    }

    private void initBar() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        findViewById(R.id.actionbar).setBackgroundColor(Color.WHITE);
        mTitle = findViewById(R.id.actionbar_title_tv);
        actionbar_back_tv = findViewById(R.id.actionbar_back_tv);
        actionbar_back_tv.setText("");
        Intent intent = getIntent();
        nickname = intent.getStringExtra(CommonConst.WITHDRAW_NAME);
        if (nickname.equals("name")) {
            mTitle.setText("昵称");
            nickname_edit.setHint("请输入您要修改的昵称");
            nickname_edit.setText(accountManager.getAccountInfo().getName());
            nickname_edit.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(5)});
        } else {
            nickname_edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            mTitle.setText("修改微信号");
            nickname_edit.setHint("请输入微信绑定的手机号");
            nickname_edit.setText(accountManager.getAccountInfo().getWechat());
            nickname_edit.setInputType(InputType.TYPE_CLASS_PHONE);
        }
    }
    //限制输入空格和换行
    private InputFilter filter=new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if(source.equals(" ")||source.toString().contentEquals("\n"))return "";
            else return null;
        }
    };

    private void requestNickname() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        String apiToken = accountManager.getAccountInfo().getApiToken();
        int gender = accountManager.getAccountInfo().getGender();
        String avatar = accountManager.getAccountInfo().getAvatar();
        String wechat = accountManager.getAccountInfo().getWechat();
        String userBirthday = accountManager.getAccountInfo().getUserBirthday();
        if (wechat==null){
            wechat="";
        }
        if (userBirthday==null){
            userBirthday="";
        }
        SettingRequestManager.requestBirth(apiToken, String.valueOf(gender),userBirthday,
                nickname_edit.getText().toString(),avatar,wechat)
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

                        if (accountManager != null && userInfoBeanBaseBean.data != null) {

                            accountManager.saveAccountInfo(userInfoBeanBaseBean.data);
                        }
                        CommonToast.showToast("修改成功");
                        LZLog.i(TAG, "修改成功");
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        CommonToast.showToast("修改失败");
                    }
                });
    }

    private void requestWx() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        String apiToken = accountManager.getAccountInfo().getApiToken();
        String name = accountManager.getAccountInfo().getName();
        int gender = accountManager.getAccountInfo().getGender();
        String avatar = accountManager.getAccountInfo().getAvatar();
        String userBirthday = accountManager.getAccountInfo().getUserBirthday();
        if (userBirthday==null){
            userBirthday="";
        }
        SettingRequestManager.requestBirth(apiToken, String.valueOf(gender),userBirthday, name, avatar,
                nickname_edit.getText().toString())
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

                        if (accountManager != null && userInfoBeanBaseBean.data != null) {

                            accountManager.saveAccountInfo(userInfoBeanBaseBean.data);
                        }
                        CommonToast.showToast("修改成功");
                        LZLog.i(TAG, "修改成功");
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        CommonToast.showToast("修改失败");
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.name_submit:
                if (nickname.equals("name")) {
                    requestNickname();

                } else {
                    requestWx();
                }

                break;
        }
    }


}
