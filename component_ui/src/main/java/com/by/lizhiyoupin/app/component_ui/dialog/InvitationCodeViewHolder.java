package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/23 10:07
 * Summary: 填写邀请码弹框
 */
public class InvitationCodeViewHolder implements IContentHolder, View.OnClickListener {
    private Context mContext;
    private MessageBox mMessageBox;
    private EditText mCodeTv;

    public InvitationCodeViewHolder(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_invitation_code_layout, parent, false);
        mMessageBox = messageBox;
        initView(inflate);
        return inflate;
    }

    private void initView(View root) {
        root.findViewById(R.id.invitation_code_close).setOnClickListener(this);
        root.findViewById(R.id.invitation_code_submit_tv).setOnClickListener(this);
        mCodeTv = root.findViewById(R.id.invitation_code_tv);
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        int id = v.getId();
        if (id == R.id.invitation_code_close) {
            if (mMessageBox != null) {
                mMessageBox.dismiss();
            }
        } else if (id == R.id.invitation_code_submit_tv) {
            CharSequence text = mCodeTv.getText();
            if (TextUtils.isEmpty(text)||text.length()!=6) {
                CommonToast.showToast("请输入6位邀请码!");
                return;
            }
            IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance().getManager(IAccountManager.class.getName());
            if (accountManager != null) {
                requestBindInviter(accountManager.getUserPhone(), text.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultRx2Subscribe<BaseBean<UserInfoBean>>() {
                            @Override
                            public void onNext(BaseBean<UserInfoBean> bean) {
                                super.onNext(bean);
                                LZLog.w("invitation ==","onNext" );
                                if (bean.success() && bean.data != null) {
                                    accountManager.saveAccountInfo(bean.data);
                                } else {
                                    MessageToast.showToast(mContext,bean.msg);
                                    LZLog.w("invitation onError1==", bean.msg);
                                    //onError(new Throwable(bean.msg));
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                super.onError(throwable);
                                MessageToast.showToast(mContext,"接口请求失败");
                                LZLog.w("invitation onError==", throwable.toString());
                            }
                        });
            }
            mMessageBox.dismiss();
        }
    }

    /**
     * 填写邀请码
     *
     * @param phone
     * @param inviteCode
     * @return
     */
    public Observable<BaseBean<UserInfoBean>> requestBindInviter(String phone, String inviteCode) {
        HashMap<String, String> hashMap = new HashMap<>(3);
        hashMap.put("inviterPhone", "");//邀请人
        hashMap.put("inviteCode", inviteCode);
        hashMap.put("phone", phone);//被邀请人（自己）
        return ApiService.getNewsApi().requestBindInviter(hashMap);
    }
}
