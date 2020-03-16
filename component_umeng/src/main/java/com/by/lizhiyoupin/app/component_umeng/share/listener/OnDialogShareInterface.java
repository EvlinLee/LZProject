package com.by.lizhiyoupin.app.component_umeng.share.listener;

import com.by.lizhiyoupin.app.io.entity.ChannelListBean;
import com.umeng.socialize.bean.SHARE_MEDIA;

public interface OnDialogShareInterface {
    /**
     * 点击分享
     * @param channelListBean
     * @param plat
     */
    void onShare(final ChannelListBean channelListBean, final SHARE_MEDIA plat);
}
