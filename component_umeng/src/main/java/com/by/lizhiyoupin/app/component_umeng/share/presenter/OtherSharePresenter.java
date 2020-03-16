package com.by.lizhiyoupin.app.component_umeng.share.presenter;

import android.text.TextUtils;

import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_umeng.R;
import com.by.lizhiyoupin.app.component_umeng.share.impl.IShareContentManager;
import com.by.lizhiyoupin.app.component_umeng.share.impl.ISharePresenter;
import com.by.lizhiyoupin.app.io.entity.ChannelListBean;
import com.by.lizhiyoupin.app.io.entity.ShareParams;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 其他方式分享， 如文件
 */
public class OtherSharePresenter extends BaseSharePresenter implements ISharePresenter {

    public OtherSharePresenter(IShareContentManager shareContentManager) {
        super(shareContentManager);
    }

    @Override
    public void share(ChannelListBean bean, SHARE_MEDIA plat) {
        shareToChannel(bean,plat);
    }


    private void shareToChannel(ChannelListBean bean, SHARE_MEDIA plat) {
        if (bean.getShareType()==ShareParams.TYPE_NATIVE_DIALOG){
            //需要回调处理逻辑，不自动调用分享
            onResult(plat);
            return;
        }
        if (plat == SHARE_MEDIA.SINA) {
            shareToSina(plat, this, bean);
        } else {
            shareToOther(plat, this, bean);
        }
    }

    /**
     * 分享到微博
     *
     * @param plat     SHARE_MEDIA
     * @param listener UMShareListener
     * @param bean     ChannelListBean
     */
    private void shareToSina(final SHARE_MEDIA plat, @NonNull final UMShareListener listener, final ChannelListBean bean) {
        // sina微博没有title，没有url，所以把title和url添加到text里
        final StringBuilder text = new StringBuilder(bean.getShareTitle());
        text.append(' ').append(bean.getDesc()).append(' ');

        AppCompatActivity context = getContext();

        text.append(bean.getLink());
        UMImage umImage;
        UMImage[] umImages;
        ShareAction shareAction = new ShareAction(context);
        if (bean.getShareImgUrls() != null && bean.getShareImgUrls().length > 0) {
            int length = bean.getShareImgUrls().length;
            String[] shareImgUrls = bean.getShareImgUrls();
            umImages = new UMImage[length];
            for (int i = 0; i < length; i++) {
                umImages[i] = new UMImage(context, shareImgUrls[i]);
            }
            shareAction.withMedias(umImages);
        }
        if (!TextUtils.isEmpty(bean.getShareImgUrl())) {
            umImage = new UMImage(context, bean.getShareImgUrl());
            shareAction.withMedia(umImage);
        } else {
            umImage = new UMImage(context,R.mipmap.share_logo);
            shareAction.withMedia(umImage);
        }
        shareAction.setPlatform(plat)
                .setCallback(listener)
                .withText(text.toString())
                .share();

        dismissLoading();
    }

    /**
     * 分享其他渠道
     *
     * @param plat     SHARE_MEDIA
     * @param listener UMShareListener
     * @param bean     ChannelListBean
     */
    private void shareToOther(final SHARE_MEDIA plat, @NonNull final UMShareListener listener, final ChannelListBean bean) {
        // 注意上传多图需要带文字描述.withText("hello")
        AppCompatActivity context = getContext();

        UMImage thumb =  new UMImage(context, R.mipmap.share_logo);
        if (bean.getShareType() == ShareParams.TYPE_H5_URL) {
            final UMWeb web = new UMWeb(bean.getLink());
            web.setTitle(bean.getShareTitle());
            if (!TextUtils.isEmpty(bean.getShareImgUrl())) {
                web.setThumb(new UMImage(context, bean.getShareImgUrl()));
            } else {
                web.setThumb(thumb);
            }
            web.setDescription(bean.getDesc());
            new ShareAction(context)
                    .setPlatform(plat)
                    .setCallback(listener)
                    .withText(bean.getDesc())
                    .withMedia(web)
                    .share();
        } else if (bean.getShareType() == ShareParams.TYPE_PIC_URL) {
            UMImage image = new UMImage(context, bean.getShareImgUrl());//网络图片
            image.setThumb(new UMImage(context, bean.getShareImgUrl()));
            new ShareAction(context)
                    .setPlatform(plat)
                    .setCallback(listener)
                    .withText(bean.getDesc())
                    .withMedia(image)
                    .share();
        } else if (bean.getShareType() == ShareParams.TYPE_FILE) {
            if (bean.getShareFile() != null) {
                UMImage umImage = new UMImage(context, bean.getShareFile());


                String absolutePath = bean.getShareFile().getAbsolutePath();

                if (absolutePath.endsWith("jpg")||absolutePath.endsWith("png")){
                    umImage.setThumb(new UMImage(context,bean.getShareFile()));
                }else{
                    umImage.setThumb(thumb);
                }

                new ShareAction(context)
                        .setPlatform(plat)
                        .setCallback(listener)
                        .withMedia(umImage)
                        .share();
            }

        }else if (bean.getShareType()==ShareParams.TYPE_TEXT){
            new ShareAction(context)
                    .setPlatform(plat)
                    .setCallback(listener)
                    .withText(bean.getDesc())//分享内容
                    .share();
        }
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).dismissLoadingDialog();
        }
    }

}
