package com.by.lizhiyoupin.app.component_umeng.share.impl;


import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

/**
 * 分享对话框内容处理
 * {@link ShareDialogHolder  }
 */
public interface IShareHolder<T> extends IContentHolder {

    void updateShare(T value);

    T getShareValue();

    void setShareContentCreater(IShareContentCreater<T> creater);
}
