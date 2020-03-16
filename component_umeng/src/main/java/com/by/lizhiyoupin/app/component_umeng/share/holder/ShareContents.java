package com.by.lizhiyoupin.app.component_umeng.share.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.component_umeng.R;
import com.by.lizhiyoupin.app.component_umeng.share.adapter.ShareDialogAdapter;
import com.by.lizhiyoupin.app.component_umeng.share.impl.IShareContentCreater;
import com.by.lizhiyoupin.app.component_umeng.share.listener.OnDialogShareInterface;
import com.by.lizhiyoupin.app.io.entity.ChannelListBean;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 内容构造器，拆分便于UI自定义扩展
 */
public class ShareContents implements IShareContentCreater< List<ChannelListBean>>, View.OnAttachStateChangeListener, View.OnClickListener{

    private static final String TAG =ShareContents.class.getSimpleName();

    private ShareDialogAdapter shareAdapter;
    private OnDialogShareInterface dialogShareInterface;

    private ViewGroup dialogContent;

    protected Context context;

    protected  List<ChannelListBean> shareBean;


    public ShareContents(@NonNull Context context) {
        this.context = context;
    }

    public void setDialogShareInterface(OnDialogShareInterface callback) {
        this.dialogShareInterface = callback;

    }

    @Override
    public boolean initCustomsContent(FrameLayout customsTopLayout,  List<ChannelListBean> value) {
        //todo 这里后期如果需要添加 分享面板提前显示图 可在这添加布局

        return false;
    }

    @Override
    public void initShareChannelList(ViewGroup dialogContent, RecyclerView channelListRv,  List<ChannelListBean> value) {
        this.shareBean = value;
        updateShareTitleText(dialogContent, "");

            try {
                initVRecyclerView(channelListRv, value);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

    }

    @Override
    public void onShareCreated(ViewGroup dialogContent,  List<ChannelListBean> value) {
       LZLog.d("DefaultContentHolder", "==============>>>> share content created");
        this.shareBean = value;
        this.dialogContent = dialogContent;
        dialogContent.addOnAttachStateChangeListener(this);
    }

    /**
     *  渠道选择
     * @param mShareRecyclerView RecyclerView
     * @param data
     */
    private void initVRecyclerView(RecyclerView mShareRecyclerView,  List<ChannelListBean> data) {
        ArrayList<ChannelListBean> mArrayList;
        if (data == null || data.size() <= 0) {
            mArrayList = new ArrayList<>();
            mShareRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        } else {
            mArrayList = (ArrayList<ChannelListBean>) data;
            mShareRecyclerView.setLayoutManager(new GridLayoutManager(context, mArrayList.size() < 4 ? mArrayList.size() : 4));
        }
        shareAdapter = new ShareDialogAdapter(context, mArrayList, R.layout.dialog_share_item_v);
        mShareRecyclerView.setAdapter(shareAdapter);
        shareAdapter.setItemClickable(true);
        if (dialogShareInterface != null) {
            shareAdapter.setOnDialogShareListener(dialogShareInterface);
        }
    }


    /**
     * 更新分享标题
     * @param viewGroup ViewGroup
     * @param middleChar String
     */
    private void updateShareTitleText(ViewGroup viewGroup, String middleChar) {
        // Title
        TextView shareTitle = viewGroup.findViewById(R.id.share_title_tv);
        if (shareTitle != null) {
            shareTitle.setText(TextUtils.isEmpty(middleChar) ? "请选择分享平台" : middleChar);
            shareTitle.setOnClickListener(this);
        }
    }

    @Override
    public void onViewAttachedToWindow(View v) {
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        dialogContent.removeOnAttachStateChangeListener(this);
        dialogContent = null;
    }

    @Override
    public void onClick(View v) {

    }

}
