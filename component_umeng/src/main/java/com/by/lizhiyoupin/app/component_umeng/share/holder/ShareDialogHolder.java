package com.by.lizhiyoupin.app.component_umeng.share.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.component_umeng.R;
import com.by.lizhiyoupin.app.component_umeng.share.holder.base.BaseViewHolder;
import com.by.lizhiyoupin.app.component_umeng.share.impl.IShareContentCreater;
import com.by.lizhiyoupin.app.component_umeng.share.impl.IShareHolder;
import com.by.lizhiyoupin.app.io.entity.ChannelListBean;
import com.by.lizhiyoupin.app.message_box.MessageBox;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 分享弹框UI逻辑抽离
 */
public class ShareDialogHolder extends BaseViewHolder implements IShareHolder< List<ChannelListBean>> {

    private static final String TAG = ShareDialogHolder.class.getSimpleName();

    private  List<ChannelListBean> mShareBean;
    private RecyclerView mShareRecyclerView;
    private FrameLayout mTopCustomLayout;
    private IShareContentCreater< List<ChannelListBean>> shareContentCreater;


    public ShareDialogHolder(@NonNull Context context,
                             @NonNull IShareContentCreater<List<ChannelListBean>> shareContent,
                             @NonNull ViewGroup viewGroup) {
        super(context, viewGroup);
        this.shareContentCreater = shareContent;
    }

    public void setShareContentCreater(IShareContentCreater<List<ChannelListBean>> creater) {
        this.shareContentCreater = creater;
    }


    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        return mViewParent;
    }

    @Override
    public void initView(ViewGroup parent) {
        Context context = getContext();

        mTopCustomLayout = parent.findViewById(R.id.topCustomsLayout);
        mShareRecyclerView = parent.findViewById(R.id.dialog_share_rv);


    }

    public void updateShare( List<ChannelListBean> beanList) {
        if (beanList == null) {
            CommonToast.showToast("网络异常请稍后再试!");
            return;
        }
        this.mShareBean = beanList;
        shareContentCreater.initCustomsContent(mTopCustomLayout,beanList);

        // 初始化分享渠道
        shareContentCreater.initShareChannelList(mViewParent, mShareRecyclerView, beanList);

        // 创建成功回调
        shareContentCreater.onShareCreated(mViewParent, beanList);
    }

    public  List<ChannelListBean> getShareValue() {
        return mShareBean;
    }


    @Override
    public void onViewDetachedFromWindow(View v) {
        super.onViewDetachedFromWindow(v);
        shareContentCreater = null;
    }
}
