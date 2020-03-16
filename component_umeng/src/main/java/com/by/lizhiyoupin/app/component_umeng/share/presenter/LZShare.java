package com.by.lizhiyoupin.app.component_umeng.share.presenter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.component_umeng.R;
import com.by.lizhiyoupin.app.component_umeng.share.holder.ShareContents;
import com.by.lizhiyoupin.app.component_umeng.share.holder.ShareDialogHolder;
import com.by.lizhiyoupin.app.component_umeng.share.holder.base.BaseViewHolder;
import com.by.lizhiyoupin.app.component_umeng.share.impl.IShareContentCreater;
import com.by.lizhiyoupin.app.component_umeng.share.impl.IShareContentManager;
import com.by.lizhiyoupin.app.component_umeng.share.impl.IShareHolder;
import com.by.lizhiyoupin.app.component_umeng.share.impl.ISharePresenter;
import com.by.lizhiyoupin.app.component_umeng.share.listener.OnDialogShareInterface;
import com.by.lizhiyoupin.app.component_umeng.share.manager.IShareManager;
import com.by.lizhiyoupin.app.io.entity.ChannelListBean;
import com.by.lizhiyoupin.app.io.entity.ShareParams;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.Reference;
import com.by.lizhiyoupin.app.stack.ActivityStack;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/6 17:45
 * Summary: 分享 管理
 * LZShare.share(new List<ChannelListBean>())
 * .shareListener(new IShareManager.ShareListener() {
 * @Override public void onShareListener(int state, String plat) {
 * <p>
 * }
 * @Override public void onShareDismissed() {
 * <p>
 * }
 * }).show();
 */
public class LZShare implements IShareContentManager, OnDialogShareInterface {
    public static final String TAG = LZShare.class.getSimpleName();


    // 当前分享对象句柄
    private static LZShare instance;
    private IShareHolder<List<ChannelListBean>> shareHolder;
    private MessageBox shareDialog;
    private IShareManager.ShareListener shareListener;


    public static LZShare share(List<ChannelListBean> shareBean) {
        return share(shareBean, null);
    }

    /**
     * 调起分享弹框，然后使用onResult回调处理 逻辑，不会去三方分享
     * @return
     */
    public static List<ChannelListBean> getChannelListNativeDeal(){
        List<ChannelListBean> list=new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ChannelListBean channelListBean = new ChannelListBean();
            channelListBean.setShareType(ShareParams.TYPE_NATIVE_DIALOG);
            channelListBean.setName(i==0?"微信":"微信朋友圈");
            channelListBean.setCode(i);
            list.add(channelListBean);
        }
        return list;
    }
    /**
     * 分享 h5链接
     * @param h5url
     * @return
     */
    public static List<ChannelListBean> getChannelListH5(String h5url,String title,String desc){
        List<ChannelListBean> list=new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ChannelListBean channelListBean = new ChannelListBean();
            channelListBean.setDesc(desc);
            channelListBean.setShareType(ShareParams.TYPE_H5_URL);
            channelListBean.setName(i==0?"微信":"微信朋友圈");
            channelListBean.setCode(i);
            channelListBean.setLink(h5url);
            channelListBean.setShareTitle(i==0? (!TextUtils.isEmpty(title)?title:"荔枝优品"):desc);
            list.add(channelListBean);
        }
        return list;
    }

    /**
     * 分享网络图片
     * @param picUrl 图片地址
     * @return
     */
    public static List<ChannelListBean> getChannelListPic(String picUrl){
        List<ChannelListBean> list=new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ChannelListBean channelListBean = new ChannelListBean();
            channelListBean.setDesc("分享");
            channelListBean.setShareType(ShareParams.TYPE_PIC_URL);
            channelListBean.setName(i==0?"微信":"微信朋友圈");
            channelListBean.setCode(i);
            channelListBean.setShareImgUrl(picUrl);
            channelListBean.setShareTitle("荔枝优品");
            list.add(channelListBean);
        }
        return list;
    }

    /**
     * 分享本地文件图片或文件视频等
     * @param file
     * @return
     */
    public static List<ChannelListBean> getChannelListNativeFile(File file){
        List<ChannelListBean> list=new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ChannelListBean channelListBean = new ChannelListBean();
            channelListBean.setDesc("分享");
            channelListBean.setShareType(ShareParams.TYPE_FILE);
            channelListBean.setName(i==0?"微信":"微信朋友圈");
            channelListBean.setCode(i);
            channelListBean.setShareFile(file);
            channelListBean.setShareTitle("荔枝优品");
            list.add(channelListBean);
        }
        return list;
    }

    /**
     * 只分享文字
     * @param
     * @return
     */
    public static List<ChannelListBean> getChannelListText(String text){
        List<ChannelListBean> list=new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ChannelListBean channelListBean = new ChannelListBean();
            channelListBean.setDesc(text);
            channelListBean.setShareType(ShareParams.TYPE_TEXT);
            channelListBean.setName(i==0?"微信":"微信朋友圈");
            channelListBean.setCode(i);
            channelListBean.setShareTitle(i==0?"荔枝优品":text);
            list.add(channelListBean);
        }
        return list;
    }
    /**
     * 抖券分享视频
     * @param file
     * @return
     */
    public static List<ChannelListBean> getChannelListNativeVideo(File file){
        List<ChannelListBean> list=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ChannelListBean channelListBean = new ChannelListBean();
            channelListBean.setDesc("分享");
            channelListBean.setShareType(ShareParams.TYPE_NATIVE_DIALOG);
            if (i==0){
                channelListBean.setName("保存视频");
                channelListBean.setCode(100);
            }else {
                channelListBean.setName(i-1==0?"微信":"微信朋友圈");
                channelListBean.setCode(i-1);
            }
            channelListBean.setShareFile(file);
            channelListBean.setShareTitle("荔枝优品");
            list.add(channelListBean);
        }
        return list;
    }

    /**
     * 微信可以分享多图单图（本地图片文件），使用下面方法
     * 分享本地图片 到微信
     * @param context
     * @param share_media
     * @param urlList
     */
    public static void showNativePicToWeichat(Activity context, String share_media,ArrayList<Uri> urlList) {
        if ("WEIXIN_CIRCLE".equals(share_media) || "WEIXIN".equals(share_media)) { //多图分享到微信及朋友圈
            //微信分享多图只支持本地图片, 所以先要进行图片缓存, 记得删除缓存文件
            Intent intent = new Intent();
            if ("WEIXIN_CIRCLE".equals(share_media)) {
                intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
            } else {
                intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
            }
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("image/*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, urlList);
            context.startActivityForResult(intent, 1600);

         /*   //权限判断
            if (!PermissionsUtil.checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // 请求权限
                } else {
                    MessageToast.showToast(context, "请打开相关权限");
                }
            }*/
        }
    }



    /**
     * 创建一个分享链接对象，
     *
     * @param beanList           {@link LZShare}
     * @param shareContentCreate {@link IShareContentCreater} 内容构建器
     * @return SscfShare
     */
    @MainThread
    public static LZShare share(List<ChannelListBean> beanList, IShareContentCreater<List<ChannelListBean>> shareContentCreate) {
        if (instance == null) {
            instance = new LZShare();
        }
        instance.updateShareInfo(beanList, shareContentCreate);
        return instance;
    }

    /**
     * 更新显示内容
     *
     * @param beanList
     * @param creater
     */
    public void updateShareInfo(List<ChannelListBean> beanList, IShareContentCreater<List<ChannelListBean>> creater) {

        IShareContentCreater<List<ChannelListBean>> contentCreater;
        if (creater == null) {
            contentCreater = new ShareContents(getAppContext());
        } else {
            contentCreater = creater;
        }

        contentCreater.setDialogShareInterface(this);

        // UI Controller
        updateShareContent(beanList, contentCreater);
    }

    private void updateShareContent(List<ChannelListBean> beanList, IShareContentCreater<List<ChannelListBean>> contentCreater) {
        Context context = getAppContext();
        if (shareHolder == null) {
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.layout_dialog_share_v_v2, null);
            shareHolder = new ShareDialogHolder(context, contentCreater, viewGroup);
        } else {
            shareHolder.setShareContentCreater(contentCreater);
        }
        ((BaseViewHolder) shareHolder).setClickListener(v -> dismiss());

        shareHolder.updateShare(beanList);
    }

    /**
     * 显示分享面板
     */
    public void show() {
        LZLog.d("LZShare", "==============>>>>>> " + getAppContext().getClass().getSimpleName());
        FragmentManager manager = getAppContext().getSupportFragmentManager();
        shareDialog = MessageBox.builder()
                .contentHolder(shareHolder)
                .bgColor(Color.TRANSPARENT)
                .bgContentColor(Color.TRANSPARENT)
                .fullScreen(true)
                .animationStyleRes(Reference.Animation.Animation_Dialog_Fade)
                .styleRes(R.style.dialog_share_theme)
                .cancelOnTouchOutside(false)
                .onDismissDialogListener((messagebox, bundle, holder, initiativeDiasmiss) -> {
                    instance = null;
                    shareDialog = null;
                    if (shareListener != null) {
                        shareListener.onShareDismissed();
                    }
                })
                .onKeyPressClickListener((messageBox, keyCode, event) -> {
                    dismissDialog();
                })
                .build("lz_share", manager);
        shareDialog.show(manager);
    }


    /**
     * 再次调起分享动作
     * 点击分享
     *
     * @param bean ChannelListBean
     * @param plat SHARE_MEDIA
     */
    public static void onShareAgain(ChannelListBean bean, SHARE_MEDIA plat) {
        if (instance != null) {
            instance.onShare(bean, plat);
        }
    }

    /**
     * 点击去分享
     *
     * @param bean
     * @param plat
     */
    @Override
    public void onShare(ChannelListBean bean, SHARE_MEDIA plat) {
        if (shareHolder == null) {
            LZLog.d(LZShare.class.getSimpleName(), "=====================>>>> 未创建视图");
            return;
        }
        ISharePresenter sharePresenter;
        // 链接分享
        if (bean.getShareType() == ShareParams.TYPE_H5_URL ||bean.getShareType()==ShareParams.TYPE_PIC_URL) {
            sharePresenter = new UrlSharePresenter(this);
        } else {
            sharePresenter = new OtherSharePresenter(this);
        }

        if (shareListener != null) {
            //监听
            sharePresenter.setShareListener(shareListener);
        }
        //调启分享面板
        sharePresenter.share(bean, plat);
    }

    /**
     * 给外面的分享回调监听
     *
     * @param l IShareManager
     */
    public LZShare shareListener(final IShareManager.ShareListener l) {
        this.shareListener = l;
        return this;
    }


    @Override
    public AppCompatActivity getAppContext() {
        AppCompatActivity appCompatActivity = (AppCompatActivity) ActivityStack.instance().currentActivity();
        return appCompatActivity;
    }

    /**
     * 销毁分享对象
     */
    public static void dismissShare() {
        if (instance != null) {
            instance.dismiss();
        }
        instance = null;
    }

    @Override
    public void dismissDialog() {
        dismiss();
    }

    public void dismiss() {
        if (shareDialog != null && !shareDialog.isDetached()) {
            shareDialog.dismissAllowingStateLoss();
        }
        shareDialog = null;
    }

}
