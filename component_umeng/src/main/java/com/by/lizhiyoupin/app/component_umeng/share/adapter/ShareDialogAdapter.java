package com.by.lizhiyoupin.app.component_umeng.share.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.component_ui.activity.BaseActivity;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonBaseRecyclerViewAdapter;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewHolder;
import com.by.lizhiyoupin.app.component_umeng.R;
import com.by.lizhiyoupin.app.component_umeng.share.listener.OnDialogShareInterface;
import com.by.lizhiyoupin.app.io.entity.ChannelListBean;
import com.by.lizhiyoupin.app.io.entity.ShareParams;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;


/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2018/8/30 15:46
 * Summary: 分享 recyclerView的adapter
 */
public class ShareDialogAdapter extends CommonBaseRecyclerViewAdapter<ChannelListBean> {
    public static final String TAG = ShareDialogAdapter.class.getSimpleName();
    private Context mContext;
    // 图片顺序可 ChannelListBean 的code 一一对应
    private static final int[] images = new int[]{
            R.drawable.umeng_socialize_wechat_share,
            R.drawable.umeng_socialize_wxcircle_share,
            R.drawable.umeng_socialize_qq_share,
            R.drawable.umeng_socialize_sina_share};


    public ShareDialogAdapter(Context context, List<ChannelListBean> data, int itemLayoutId) {
        super(context, data, itemLayoutId);
        mContext = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = super.onCreateViewHolder(parent, viewType);
        View view = holder.getView(R.id.item_share_content_tv);
        if (view != null) {
            holder.setDefaultClickListener(R.id.item_share_content_tv);
        }
        return holder;
    }

    @Override
    public void convert(CommonRecyclerViewHolder holder, ChannelListBean item, int position) {
        TextView itemTv = holder.getView(R.id.shareText);
        ImageView itemImg = holder.getView(R.id.shareIcon);
        if (item == null) {
            holder.setVisibility(false);
            return;
        } else if (item.isInvalid()) {
            holder.itemView.setVisibility(View.INVISIBLE);
            holder.itemView.setClickable(false);
            return;
        }
        itemTv.setText(item.getName());
        if (item.getCode() == 100) {
            itemImg.setImageResource(R.drawable.umeng_socialize_video_share);
        } else {
            itemImg.setImageResource(images[item.getCode() > 4 ? 1 : item.getCode()]);
        }
        //当前版本 只支持4个渠道分享，如果后台返回有大于4个数量的，把没有的不显示
        if (item.getCode() > 4 && item.getCode() != 100) {
            holder.setVisibility(false);
        }
    }

    @Override
    protected void onItemChildClick(View v, CommonRecyclerViewHolder holder, int position) {
        super.onItemChildClick(v, holder, position);
        //点击去分享
        ChannelListBean channelListBean = getData().get(position);
        if (channelListBean.getShareType() != ShareParams.TYPE_NATIVE_DIALOG) {
            //不是本地处理 点击后逻辑的才loading
            if (mContext instanceof BaseActivity) {
                ((BaseActivity) mContext).showLoadingDialog();
            }
        }
        switch (channelListBean.getCode()) {
            case 0://微信
                /*UMShareConfig config = new UMShareConfig();
                config.isNeedAuthOnGetUserInfo(true);
                UMShareAPI.get(mContext).setShareConfig(config);*/
                mOnDialogShareInterface.onShare(channelListBean, SHARE_MEDIA.WEIXIN);
                break;
            case 1://微信朋友圈
                mOnDialogShareInterface.onShare(channelListBean, SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case 2://QQ
                mOnDialogShareInterface.onShare(channelListBean, SHARE_MEDIA.QQ);
                break;
            case 3://微博
                mOnDialogShareInterface.onShare(channelListBean, SHARE_MEDIA.SINA);
                break;
            case 100://视频
                mOnDialogShareInterface.onShare(channelListBean, SHARE_MEDIA.MORE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
        super.onItemClick(v, holder, position);

    }

    private OnDialogShareInterface mOnDialogShareInterface;

    public void setOnDialogShareListener(OnDialogShareInterface onDialogShareListener) {
        mOnDialogShareInterface = onDialogShareListener;
    }
}
