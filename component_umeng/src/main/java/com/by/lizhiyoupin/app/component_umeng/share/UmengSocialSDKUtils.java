package com.by.lizhiyoupin.app.component_umeng.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;

import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.component_umeng.share.impl.IShareContentManager;
import com.by.lizhiyoupin.app.component_umeng.share.impl.ISharePresenter;
import com.by.lizhiyoupin.app.component_umeng.share.manager.IShareManager;
import com.by.lizhiyoupin.app.component_umeng.share.presenter.OtherSharePresenter;
import com.by.lizhiyoupin.app.component_umeng.share.presenter.UrlSharePresenter;
import com.by.lizhiyoupin.app.io.entity.ChannelListBean;
import com.by.lizhiyoupin.app.io.entity.ShareParams;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.SocializeException;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 *
 */
public final class UmengSocialSDKUtils {


    public static void release(final Context context) {
        UMShareAPI.get(context).release();
    }

    /**
     * 删除第三方登录信息
     *
     * @param activity Activity
     * @param plat     SHARE_MEDIA
     */
    public static void removeAccount(final Activity activity, final SHARE_MEDIA plat) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            removeAccountOnUI(activity, plat);
        } else {
            if (activity != null) {
                activity.runOnUiThread(() -> removeAccountOnUI(activity, plat));
            }
        }
    }

    private static void removeAccountOnUI(final Activity activity, final SHARE_MEDIA plat) {
        final UMShareAPI api = UMShareAPI.get(activity);
        if (api.isAuthorize(activity, plat)) {
            api.deleteOauth(activity, plat, null);
        }
    }

    public static void onActivityResult(final Activity activity, final int requestCode, final int resultCode, final Intent data) {
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 第三方登录
     * 建议使用下面的
     *
     * @param activity Activity
     * @param plat     SHARE_MEDIA
     * @param l        UMAuthListener
     * @see #getPlatformInfo 获取平台的信息 返回值更全
     */
    public static void loginByThirdParty(final Activity activity, final SHARE_MEDIA plat, final UMAuthListener l) {
        final UMShareAPI api = UMShareAPI.get(activity);
        if (plat == SHARE_MEDIA.WEIXIN || plat == SHARE_MEDIA.WEIXIN_CIRCLE) {
            if (!api.isInstall(activity, plat)) {
                l.onError(plat, 0, new WeChatNotInstalledException("wechat is not installed"));
                return;
            }
        }
        final UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        api.setShareConfig(config);
        api.doOauthVerify(activity, plat, l);
    }

    /**
     * 获得对应平台的信息
     *
     * @param activity Activity
     * @param plat     SHARE_MEDIA
     * @param l        UMAuthListener
     */
    public static void getPlatformInfo(final Activity activity, final SHARE_MEDIA plat, final UMAuthListener l) {
        final UMShareAPI api = UMShareAPI.get(activity);
        if (plat == SHARE_MEDIA.WEIXIN || plat == SHARE_MEDIA.WEIXIN_CIRCLE) {
            if (!api.isInstall(activity, plat)) {
                l.onError(plat, 0, new WeChatNotInstalledException("wechat is not installed"));
                return;
            }
        }
        final UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        api.setShareConfig(config);
        api.getPlatformInfo(activity, plat, l);
    }

    private static Throwable checkInstall(Activity activity, final SHARE_MEDIA plat) {
        if (plat == SHARE_MEDIA.WEIXIN || plat == SHARE_MEDIA.WEIXIN_CIRCLE) {
            if (!UMShareAPI.get(activity).isInstall(activity, plat)) {
                return new WeChatNotInstalledException("wechat is not installed");
            }
        } else if (plat == SHARE_MEDIA.QQ) {
            if (!UMShareAPI.get(activity).isInstall(activity, plat)) {
                return new QQNotInstalledException("qq is not installed");
            }
        }
        return null;
    }

    /**
     * 分享
     *
     * @param activity Activity
     * @param plat     SHARE_MEDIA
     * @param params   ShareParams
     * @param l        UMShareListener
     */
    public static void share(final Activity activity, final SHARE_MEDIA plat, final UMShareListener l, ShareParams params) {
        Throwable error = checkInstall(activity, plat);
        if (error != null) {
            l.onError(plat, error);
            return;
        }

        if (plat == SHARE_MEDIA.SINA) {
            // sina微博没有title，没有url，所以把title和url添加到text里
            final StringBuilder text = new StringBuilder(params.title);
            text.append(' ').append(params.msg).append(' ');
            switch (params.shareType) {
                case ShareParams.TYPE_PIC_URL:
                    text.append(params.url);
                    new ShareAction(activity).setPlatform(plat).setCallback(l).withText(text.toString())
                            .withMedia(new UMImage(activity, params.imageUrl)).share();
                    break;
                case ShareParams.TYPE_FILE:
                    if (params.shareFile != null) {
                        new ShareAction(activity).setPlatform(plat).setCallback(l).withText(text.toString())
                                .withMedia(new UMImage(activity, params.shareFile)).share();
                    }
                    break;
                default:
            }
        } else {
            switch (params.shareType) {
                case ShareParams.TYPE_PIC_URL:
                    final UMWeb web = new UMWeb(params.url);
                    web.setTitle(params.title);
                    web.setThumb(new UMImage(activity, params.imageUrl));
                    web.setDescription(params.msg);
                    new ShareAction(activity).setPlatform(plat).setCallback(l)
                            .withText(params.msg).withMedia(web).share();
                    break;
                case ShareParams.TYPE_FILE:
                    if (params.shareFile != null) {
                        new ShareAction(activity).setPlatform(plat).setCallback(l)
                                .withMedia(new UMImage(activity, params.shareFile)).share();
                    }
                    break;
                default:
            }
        }
    }

    /**
     * 分享
     *
     * @param contentManager IShareContentManager
     * @param plat           SHARE_MEDIA
     * @param l              UMShareListener
     * @param bean           ChannelListBean
     */
    public static void share(final IShareContentManager contentManager, final SHARE_MEDIA plat, @NonNull final UMShareListener l, final ChannelListBean bean) {
        AppCompatActivity context = contentManager.getAppContext();
        if (context == null || context.isFinishing())
            return;

        Throwable error = checkInstall(context, plat);
        if (error != null) {
            l.onError(plat, error);
            return;
        }

        /**
         * 基于场景拆分分享请求
         * {@link   UrlSharePresenter#share(ChannelListBean, SHARE_MEDIA)} Uri分享
         */
        ISharePresenter sharePresenter;

        int type = bean.getShareType();

        if (type == ShareParams.TYPE_PIC_URL || type == ShareParams.TYPE_H5_URL) {
            sharePresenter = new UrlSharePresenter(contentManager);
        }
        // 预留填充场景
        else {
            sharePresenter = new OtherSharePresenter(contentManager);
        }

        sharePresenter.share(bean, plat);
    }

    public static final class WeChatNotInstalledException extends SocializeException {
        public WeChatNotInstalledException(int i, String s) {
            super(i, s);
        }

        public WeChatNotInstalledException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public WeChatNotInstalledException(String s) {
            super(s);
        }
    }

    public static final class QQNotInstalledException extends SocializeException {
        public QQNotInstalledException(int i, String s) {
            super(i, s);
        }

        public QQNotInstalledException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public QQNotInstalledException(String s) {
            super(s);
        }
    }

    /**
     * 调启 分享
     *
     * @param activity
     * @param shareBean
     */
    public static void shareShow(final Activity activity, final List<ChannelListBean> shareBean) {
        final IShareManager shareManager = (IShareManager) ComponentManager.getInstance()
                .getManager(IShareManager.class.getName());
        if (shareManager == null) {
            return;
        }
        shareManager.showShareDialog(activity, shareBean);
    }

    public static void shareShow(final Activity activity, final List<ChannelListBean> shareBean, IShareManager.ShareListener listener) {
        final IShareManager shareManager = (IShareManager) ComponentManager.getInstance()
                .getManager(IShareManager.class.getName());
        if (shareManager == null) {
            return;
        }
        shareManager.showShareDialog(activity, shareBean, listener);
    }
}
