package com.by.lizhiyoupin.app.login.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.SplitUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.login.LoginRequestManager;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.stack.LoginStack;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.gyf.immersionbar.ImmersionBar;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/24 14:27
 * Summary: 邀请码 页面
 */
@Route(path = "/app/InvitationCodeActivity")
public class InvitationCodeActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    public static final String TAG = InvitationCodeActivity.class.getSimpleName();
    private TextView mActionbarBackIv;
    private TextView mActionbarRightTv;

    private EditText mInputEt;
    private TextView mNextTv;
    private CardView mFromCardView;
    private ImageView mFaceIv;
    private TextView mIntroductionTv;
    private String mPhone;

    private static final String TYPE_SUBMIT = "0";//提交
    private static final String TYPE_NEXT = "1";//下一步



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_code_layout);
        ImmersionBar.with(this)
                .navigationBarColorInt(Color.WHITE)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .fitsSystemWindows(false)
                .titleBar(findViewById(R.id.status_bar_view))
                .flymeOSStatusBarFontColorInt(Color.BLACK )
                .statusBarColorInt(Color.WHITE)
                .init();
        Intent intent = getIntent();
        mPhone = intent.getStringExtra(CommonConst.KEY_LOGIN_PHONE);
        initView();
    }

    private void initView() {
        mActionbarBackIv = findViewById(R.id.actionbar_back_tv);
        mActionbarRightTv = findViewById(R.id.actionbar_right_tv);
        mInputEt = findViewById(R.id.invitation_code_input_et);
        mNextTv = findViewById(R.id.invitation_code_next_tv);
        mFromCardView = findViewById(R.id.invitation_code_from_cv);
        mFaceIv = mFromCardView.findViewById(R.id.face_iv);
        mIntroductionTv = mFromCardView.findViewById(R.id.introduction_tv);
        mNextTv.setEnabled(false);
        mInputEt.addTextChangedListener(this);
        mNextTv.setOnClickListener(this);
        mActionbarRightTv.setOnClickListener(this);
        mActionbarBackIv.setOnClickListener(this);

        mFromCardView.setVisibility(View.GONE);
        mActionbarBackIv.setText("");
        ViewUtil.setDrawableOfTextView(mActionbarBackIv, R.drawable.actionbar_back, ViewUtil.DrawableDirection.LEFT);
        mActionbarRightTv.setTextColor(ContextCompat.getColor(this, R.color.color_999999));
        mActionbarRightTv.setText(getResources().getString(R.string.actionbar_jump_text));
        mNextTv.setText(getResources().getString(R.string.login_next_step));
        mNextTv.setTag(TYPE_NEXT);
        DeviceUtil.showInputMethodDelay(mInputEt, 500);
    }

    private void setCardViewAdmin() {
        mNextTv.setText(getResources().getString(R.string.login_invitation_code_next));
        mNextTv.setTag(TYPE_SUBMIT);
        ValueAnimator animator = ValueAnimator.ofInt(0, DeviceUtil.dip2px(this, 90));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {

                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mFromCardView.getLayoutParams();
                layoutParams.height = value;
                mFromCardView.setLayoutParams(layoutParams);

            }
        });
        animator.setDuration(1000);
        mFromCardView.setVisibility(View.VISIBLE);
        animator.start();
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.invitation_code_next_tv:
                if (mNextTv.getTag() == TYPE_SUBMIT) {
                    // 提交 绑定邀请人，跳转页面
                    requestBindInviter();
                } else {
                    // 下一步 查询邀请人信息
                    requestGetInvitationData();
                }
                break;
            case R.id.actionbar_back_tv:
                //CommonSchemeJump.showMainActivity(this);
                finish();
                break;
            case R.id.actionbar_right_tv:
                //跳过
                //CommonSchemeJump.showMainActivity(this);
                finish();
                break;
            default:
                break;
        }
    }

    private void requestBindInviter() {
        if (TextUtils.isEmpty(mInputEt.getText()) || TextUtils.isEmpty(mPhone)) {
            LZLog.w(TAG, "自己手机号和（他人邀请码或他人手机）均不能为空");
            return;
        }
        String inviterPhone = "";
        String inviteCode = "";
        String phonu = mInputEt.getText().toString().trim();
        if (phonu.length() < 6) {
            MessageToast.showToast(this, "请输入正确的邀请码");
            return;
        } else if (phonu.length() == 6) {
            inviteCode = phonu;
        } else if (phonu.length() == 11) {
            boolean mobile = SplitUtils.isMobile(phonu);
            if (mobile) {
                inviterPhone = phonu;
            } else {
                MessageToast.showToast(this, "请输入正确的手机号");
                return;
            }
        } else {
            MessageToast.showToast(this, "请输入正确的邀请码或手机号");
            return;
        }
        DeviceUtil.hideInputMethod(mInputEt);
        showLoadingDialog();
        LoginRequestManager.requestBindInviter(mPhone, inviteCode, inviterPhone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<UserInfoBean>>() {
                    @Override
                    public void onNext(BaseBean<UserInfoBean> inviterBeanBaseBean) {
                        super.onNext(inviterBeanBaseBean);
                        if (!inviterBeanBaseBean.success() || inviterBeanBaseBean.data == null) {
                            onError(new Throwable(inviterBeanBaseBean.msg));
                            return;
                        }
                        dismissLoadingDialog();
                        LiZhiApplication.getApplication().getAccountManager().saveAccountInfo(inviterBeanBaseBean.data);
                        LZLog.i(TAG, "绑定邀请人成功");
                        //返回主页面 todo 后期可能返回其它页面
                        //CommonSchemeJump.showMainActivity(InvitationCodeActivity.this);
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dismissLoadingDialog();
                        LZLog.i(TAG, "绑定邀请人失败" + throwable);
                        CommonToast.showToast("绑定邀请人失败");
                    }
                });
    }


    private void requestGetInvitationData() {
        if (TextUtils.isEmpty(mInputEt.getText())) {
            CommonToast.showToast("邀请码不能为空");
            LZLog.w(TAG, "邀请码不能为空");
            return;
        }
        String inviterPhone = "";
        String inviteCode = "";
        String phonu = mInputEt.getText().toString().trim();
        if (phonu.length() < 6) {
            MessageToast.showToast(this, "请输入正确的邀请码");
            return;
        } else if (phonu.length() == 6) {
            inviteCode = phonu;
        } else if (phonu.length() == 11) {
            boolean mobile = SplitUtils.isMobile(phonu);
            if (mobile) {
                inviterPhone = phonu;
            } else {
                MessageToast.showToast(this, "请输入正确的手机号");
                return;
            }
        } else {
            MessageToast.showToast(this, "请输入正确的邀请码或手机号");
            return;
        }
        DeviceUtil.hideInputMethod(mInputEt);
        showLoadingDialog();
        LoginRequestManager.requestGetInviter(inviterPhone, inviteCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<UserInfoBean>>() {
                    @Override
                    public void onNext(BaseBean<UserInfoBean> inviterBeanBaseBean) {
                        super.onNext(inviterBeanBaseBean);
                        if (!inviterBeanBaseBean.success()) {
                            onError(new Throwable(inviterBeanBaseBean.msg));
                            return;
                        }
                        dismissLoadingDialog();
                        LZLog.i(TAG, "获取邀请人 信息成功");
                        UserInfoBean result = inviterBeanBaseBean.getResult();
                        if (result == null) {
                            return;
                        }
                        new GlideImageLoader(InvitationCodeActivity.this, result.getAvatar()).into(mFaceIv);
                        ViewUtil.setTextViewFormat(InvitationCodeActivity.this, mIntroductionTv, R.string.login_inviter_li_zhi, result.getName());
                        setCardViewAdmin();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dismissLoadingDialog();
                        CommonToast.showToast("获取邀请人信息失败");
                        LZLog.i(TAG, "获取邀请人 信息失败" + throwable);
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() >= 6) {
            mNextTv.setEnabled(true);
        } else {
            mNextTv.setEnabled(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mInputEt != null) {
            DeviceUtil.hideInputMethod(mInputEt);
        }
    }

    @Override
    protected void onDestroy() {
        LoginStack.instance().removeActivity(this);
        super.onDestroy();
    }

    @Override
    public void finish() {
        LoginStack.instance().removeActivity(this);
        super.finish();
        LoginStack.instance().finishAllLoginActivity();
    }
}
