package com.by.lizhiyoupin.app.io.entity;

import java.io.File;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/24 17:28
 * Summary: 分享控件的分享渠道
 */
public class ChannelListBean {

    /**
     *  和 ShareDialogAdapter 的图标images 一一对应
     */
    private int code;//分享渠道 如0是微信 1微信朋友圈 2QQ 3新浪  100保存视频
    private String name;//渠道名 如微信
    private int shareType;//分享出去的类型 99表示本地需要弹框不自动执行分享操作 ,0图片地址， 1文件, 2h5链接, 3视频链接
    private String shareImgUrl;//分享网络图片
    private File shareFile;//native分享本地图片文件
    private String link;//h5链接 分享给他人
    private String videoUrl;//视频链接

    private String shareTitle;//分享出去的title
    private String desc;//分享出去的描述信息
    private String[] shareImgUrls;//分享多张网络图片

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public File getShareFile() {
        return shareFile;
    }

    public void setShareFile(File shareFile) {
        this.shareFile = shareFile;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getShareImgUrl() {
        return shareImgUrl;
    }

    public void setShareImgUrl(String shareImgUrl) {
        this.shareImgUrl = shareImgUrl;
    }

    public String[] getShareImgUrls() {
        return shareImgUrls;
    }

    public void setShareImgUrls(String[] shareImgUrls) {
        this.shareImgUrls = shareImgUrls;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isInvalid() {
        return code == -1;
    }

    public static ChannelListBean emptyItem() {
        ChannelListBean bean = new ChannelListBean();
        bean.setCode(-1);
        return bean;
    }


}
