package com.by.lizhiyoupin.app.component_umeng.share.impl;

import com.by.lizhiyoupin.app.io.entity.ChannelListBean;
import com.by.lizhiyoupin.app.component_umeng.share.manager.IShareManager;
import com.umeng.socialize.bean.SHARE_MEDIA;

public interface ISharePresenter {

    void share(ChannelListBean channelListBean, SHARE_MEDIA plat);

    void setShareListener(final IShareManager.ShareListener l);
}
