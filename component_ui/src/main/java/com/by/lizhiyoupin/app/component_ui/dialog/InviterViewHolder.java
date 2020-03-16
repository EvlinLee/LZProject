package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.RoundImageView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/12 13:36
 * Summary:
 */
public class InviterViewHolder implements IContentHolder, View.OnClickListener {
    public static final String TAG = InviterViewHolder.class.getSimpleName();
    private RoundImageView mInviterPhotoIv;
    private TextView mInviterDetailNameTv;
    private TextView mInviterDetailCodeTv;
    private TextView mInviterCopyTv;
    private ImageView mInviterCloseIv;
    private Context context;
    private MessageBox mMessageBox;
    private Disposable mDisposable;

    public InviterViewHolder(Context context) {
        this.context = context;

    }

    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        mMessageBox = messageBox;
        View content = LayoutInflater.from(context).inflate(R.layout.messagebox_inviter_layout, parent, false);
        initView(content);
        return content;
    }

    private void initView(View content) {
        mInviterPhotoIv = content.findViewById(R.id.inviter_photo_iv);
        mInviterDetailNameTv = content.findViewById(R.id.inviter_detail_name_tv);
        mInviterDetailCodeTv = content.findViewById(R.id.inviter_detail_code_tv);
        mInviterCopyTv = content.findViewById(R.id.inviter_copy_tv);
        mInviterCloseIv = content.findViewById(R.id.inviter_close_iv);
        mInviterCopyTv.setOnClickListener(this);
        mInviterCloseIv.setOnClickListener(this);
        requestData();
    }

    private void requestData() {
        HashMap<String, String> hashMap = new HashMap<>(2);
        hashMap.put("inviterPhone", "");//邀请人手机号
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager != null) {
            String parentCode = accountManager.getAccountInfo().getParentCode();
            if (TextUtils.isEmpty(parentCode)){
                parentCode="";
            }
            hashMap.put("inviteCode", parentCode); //邀请码
        }
        ApiService.getNewsApi().requestGetInviter(hashMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<UserInfoBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(BaseBean<UserInfoBean> bean) {
                        super.onNext(bean);
                        if (!bean.success() || bean.data == null) {
                            onError(new Throwable(bean.msg));
                            return;
                        }
                        Log.i(TAG, "requestGetInviter onNext ");
                        refreshData(bean.data);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.i(TAG, "requestGetInviter onError ");
                        MessageToast.showToast(context,"加载数据失败，请稍后重试");
                    }
                });
    }


    public void refreshData(UserInfoBean userInfoBean) {
        mInviterDetailNameTv.setText(userInfoBean.getName());
        mInviterDetailCodeTv.setText(userInfoBean.getWechat());
        new GlideImageLoader(context, userInfoBean.getAvatar())
                .error(R.drawable.default_face)
                .placeholder(R.drawable.default_face)
                .into(mInviterPhotoIv);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.inviter_close_iv) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
            mMessageBox.dismiss();
        } else if (v.getId() == R.id.inviter_copy_tv) {
            String clip = mInviterDetailCodeTv.getText().toString();
            if (!TextUtils.isEmpty(clip)) {
                DeviceUtil.putClipboardText(context, clip);
                MessageToast.showToast(context, "复制成功");
            }
        }
    }
}
