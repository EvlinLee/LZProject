package com.by.lizhiyoupin.app.component_umeng.share.impl;

import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.by.lizhiyoupin.app.component_umeng.share.listener.OnDialogShareInterface;

import androidx.recyclerview.widget.RecyclerView;

/**
 *  创建分享内容
 */
public interface IShareContentCreater<T> {

    /**
     * 创建分享顶部视图
     * @param value
     * @return BOOLEAN TRUE / FALSE 返回FALSE 表示无自定义视图添加
     */
    boolean initCustomsContent(FrameLayout customsTopLayout, T value);

    /**
     * 更新渠道信息
     * @param dialogContent
     * @param channelListRv
     * @param value
     */
    void initShareChannelList(ViewGroup dialogContent, RecyclerView channelListRv, T value);

    /**
     * 视图创建完成
     * @param value T
     */
    void onShareCreated(ViewGroup dialogContent, T value);

    /**
     * 设置点击事件
     * @param callback
     */
    void setDialogShareInterface(OnDialogShareInterface callback);

}
