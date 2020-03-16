package com.by.lizhiyoupin.app.component_umeng.share.presenter;

import android.content.Context;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ThreadUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.component_umeng.R;
import com.by.lizhiyoupin.app.component_umeng.share.UmengSocialSDKUtils;
import com.by.lizhiyoupin.app.component_umeng.share.impl.IShareContentManager;
import com.by.lizhiyoupin.app.io.entity.ChannelListBean;
import com.by.lizhiyoupin.app.component_umeng.share.manager.IShareManager;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class BaseSharePresenter implements UMShareListener {

    private static final String TAG = "BaseSharePresenter";

    private IShareContentManager shareContentManager;

    private AppCompatActivity mContext;

    private IShareManager.ShareListener mShareListener;


    public BaseSharePresenter(IShareContentManager shareContentManager) {
        this.shareContentManager = shareContentManager;
        this.mContext = shareContentManager.getAppContext();
    }



    public IShareContentManager getShareContentManager() {
        return shareContentManager;
    }

    /**
     * 给外面的分享回调监听
     *
     * @param l IShareManager.ShareListener
     */
    public void setShareListener(final IShareManager.ShareListener l) {
        this.mShareListener = l;
    }

    void onThirdAuthComplete(ChannelListBean channelListBean, SHARE_MEDIA plat, Map<String, String> map) {

    }

    void onThirdAuthCancel(ChannelListBean channelListBean, SHARE_MEDIA plat) {

    }

    void onThirdAuthError(ChannelListBean channelListBean, SHARE_MEDIA plat, Throwable throwable) {
        if (plat != null) {
            CommonToast.showToast("分享失败,请重试!");
        }
    }

    AppCompatActivity getContext() {
        return shareContentManager != null ? shareContentManager.getAppContext() : null;
    }

    String getString(int resId) {
        Context context = getContext();
        return context != null ? context.getResources().getString(resId) : "";
    }

    boolean isDestoryed() {
        return mContext == null || mContext.isFinishing();
    }

    void dismissLoading() {
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).dismissLoadingDialog();
        }
    }

    void showLoading() {
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).showLoadingDialog();
        }
    }


    @Override
    public void onStart(SHARE_MEDIA plat) {
        LZLog.i(TAG, "onStart plat : " + plat);
    }

    @Override
    public void onResult(SHARE_MEDIA plat) {

        LZLog.i(TAG, "onResult plat : " + plat);
        if (mContext != null && !mContext.isFinishing() && plat == SHARE_MEDIA.SINA) {
            CommonToast.showToast(R.string.sina_weibo_share_success);
        }
        if (mShareListener != null) {
            mShareListener.onShareListener(IShareManager.STATE_SUCCESS, getPlatName(plat));
        }
        if (shareContentManager != null) {
            shareContentManager.dismissDialog();
        }
    }

    @Override
    public void onError(final SHARE_MEDIA plat, final Throwable t) {
        LZLog.i(TAG, "onError plat : " + plat);
        ThreadUtils.runOnUiThread(() -> errorEvent(plat, t));

    }

    @Override
    public void onCancel(final SHARE_MEDIA plat) {
        LZLog.i(TAG, "onCancel platform : " + plat);
        if (mShareListener != null) {
            mShareListener.onShareListener(IShareManager.STATE_CANCEL, getPlatName(plat));
        }

        if (shareContentManager != null) {
            shareContentManager.dismissDialog();
        }
    }

    private void errorEvent(SHARE_MEDIA plat, Throwable t) {
        if ((SHARE_MEDIA.WEIXIN.equals(plat) || SHARE_MEDIA.WEIXIN_CIRCLE.equals(plat))
                && t instanceof UmengSocialSDKUtils.WeChatNotInstalledException) {
            CommonToast.showToast(R.string.not_install_wechat);
        } else if ((SHARE_MEDIA.QQ.equals(plat))
                && t instanceof UmengSocialSDKUtils.QQNotInstalledException) {
            CommonToast.showToast(R.string.not_install_qq);
        } else {
            CommonToast.showToast(R.string.share_back_error_text);
        }

        LZLog.i(TAG, plat + "onError throwable : " + t);
        if (mShareListener != null) {
            mShareListener.onShareListener(IShareManager.STATE_ERROR, getPlatName(plat));
        }

        if (shareContentManager != null) {
            shareContentManager.dismissDialog();
        }
    }

    private static String getPlatName(final SHARE_MEDIA plat) {
        if (plat==null){
            return "";
        }
        return plat.toString();
    }
}
