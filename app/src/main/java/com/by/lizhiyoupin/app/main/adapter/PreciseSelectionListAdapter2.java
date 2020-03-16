package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.BaseOfDelegateAdapter;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.BaseOfViewHolder;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;
import com.by.lizhiyoupin.app.main.holder.PreciseNewHeaderHolder;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.dueeeke.videoplayer.player.VideoView;

import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/2 14:41
 * Summary: 好货严选 列表Adapter
 */
public class PreciseSelectionListAdapter2 extends BaseOfDelegateAdapter {
    private int preciseListType;
    private  int margin;
    public PreciseSelectionListAdapter2(Context context, LayoutHelper layoutHelper,int viewType) {
        super(context, layoutHelper,viewType);
        margin=DeviceUtil.dip2px(context,10);
    }
    private void setPreciseListType(int preciseListType){
        this.preciseListType=preciseListType;
    }
    @Override
    protected int getNormalViewType(int position) {

        return PreciseNewHeaderHolder.ITEM_TYPE_OF_BOTTOM_LIST;
        //后期添加
        /*PreciseListBean bean = (PreciseListBean) getListData().get(position);
        if (bean.getSelectionType()==1){
            return PreciseNewHeaderHolder.ITEM_TYPE_OF_BOTTOM_ARTICLE_LIST;
        }else if (bean.getSelectionType()==3){
            return PreciseNewHeaderHolder.ITEM_TYPE_OF_BOTTOM_VIDEO_LIST;
        }else {
            return PreciseNewHeaderHolder.ITEM_TYPE_OF_BOTTOM_LIST;
        }*/
    }

    @Override
    protected BaseOfViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate;
        BaseOfViewHolder holder;
        if (viewType==PreciseNewHeaderHolder.ITEM_TYPE_OF_BOTTOM_ARTICLE_LIST){
            inflate= mInflater.inflate(R.layout.item_precise_article_list, parent, false);
            holder=new ArticleHolder(inflate);
        }else if (viewType==PreciseNewHeaderHolder.ITEM_TYPE_OF_BOTTOM_VIDEO_LIST){
            inflate= mInflater.inflate(R.layout.item_precise_video_list, parent, false);
            holder=new VideoHolder(inflate);
        }else {
            inflate = mInflater.inflate(R.layout.item_precise_selection_list, parent, false);
            holder=new ViewHolder(inflate);
        }

        return holder;
    }

    class VideoHolder extends BaseOfViewHolder{
        VideoView mItemVideoView;//
        ImageView mItemTopIv;//顶部图片
        TextView mItemTitleTv;//顶部title
        public VideoHolder(View itemView) {
            super(itemView);
            mItemVideoView = itemView.findViewById(R.id.item_video_view);
            mItemTopIv = itemView.findViewById(R.id.item_video_top_iv);
            mItemTitleTv = itemView.findViewById(R.id.item_video_title_tv);
        }
        @Override
        public void bindData(Object itemData, int viewType) {
            super.bindData(itemData, viewType);
            PreciseListBean bean = (PreciseListBean) itemData;
            new GlideImageLoader(mContext, bean.getPictUrl())
                    .error(R.drawable.empty_pic_list)
                    .transform(new RoundedCornersTransformation(DeviceUtil.dip2px(mContext, 8),0,RoundedCornersTransformation.CornerType.TOP))
                    .into(mItemTopIv);
            mItemTitleTv.setText(bean.getTitle());
        }

    }
    class ArticleHolder extends BaseOfViewHolder{
        ImageView mItemTopIv;//顶部图片
        TextView mItemTitleTv;//顶部title
        public ArticleHolder(View itemView) {
            super(itemView);
            mItemTopIv = itemView.findViewById(R.id.item_article_top_iv);
            mItemTitleTv = itemView.findViewById(R.id.item_article_title_tv);
        }

        @Override
        public void bindData(Object itemData, int viewType) {
            super.bindData(itemData, viewType);
            PreciseListBean bean = (PreciseListBean) itemData;
            new GlideImageLoader(mContext, bean.getPictUrl())
                    .error(R.drawable.empty_pic_list)
                    .transform(new RoundedCornersTransformation(DeviceUtil.dip2px(mContext, 8),0,RoundedCornersTransformation.CornerType.ALL))
                    .into(mItemTopIv);
            mItemTitleTv.setText(bean.getTitle());
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            if (TimeUtils.isFrequentOperation()){
                return;
            }
            PreciseListBean bean = (PreciseListBean) getListData().get(mRealPosition);
            if (bean==null){
                return;
            }
            CommonWebJump.showCommonWebActivity(mContext, bean.getUrl());
        }
    }



    class ViewHolder extends BaseOfViewHolder   {
        ImageView mItemListTopIv;//顶部图片
        TextView mItemListTitleTv;//顶部title
        TextView mItemListBottomLeftTv;//优惠券 如20元券
        TextView mItemListBottomRightTv;//返红包20元
        TextView mItemListPriceTv;//券后价格  345
        TextView mItemListVolumeTv;//月销售   10万+
        TextView mItemListNameTv;//商户名
        ImageView mItemListNameIconIv;//icon

        public ViewHolder(View itemView) {
            super(itemView);
            mItemListTopIv = itemView.findViewById(R.id.item_list_top_iv);
            mItemListTitleTv = itemView.findViewById(R.id.item_list_title_tv);
            mItemListBottomLeftTv = itemView.findViewById(R.id.item_list_bottom_left_tv);
            mItemListBottomRightTv = itemView.findViewById(R.id.item_list_bottom_right_tv);
            mItemListPriceTv = itemView.findViewById(R.id.item_list_price_tv);
            mItemListVolumeTv = itemView.findViewById(R.id.item_list_volume_tv);
            mItemListNameTv = itemView.findViewById(R.id.item_list_name_tv);
            mItemListNameIconIv = itemView.findViewById(R.id.item_list_name_icon_iv);

        }

        @Override
        public void bindData(Object itemData,int viewType) {
            super.bindData(itemData, viewType);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (mRealPosition==0||mRealPosition==1){
                layoutParams.setMargins(0,margin,0,0);
            }else {
                layoutParams.setMargins(0,0,0,0);
            }
            PreciseListBean bean = (PreciseListBean) itemData;
            new GlideImageLoader(mContext, bean.getPictUrl())
                    .error(R.drawable.empty_pic_list)
                    .transform(new RoundedCornersTransformation(DeviceUtil.dip2px(mContext, 8),0,RoundedCornersTransformation.CornerType.TOP))
                    .into(mItemListTopIv);

            mItemListTitleTv.setText(bean.getTitle());
            if (bean.getCouponAmount() == 0) {
                mItemListBottomLeftTv.setVisibility(View.GONE);
            } else {
                mItemListBottomLeftTv.setVisibility(View.VISIBLE);
                ViewUtil.setTextViewFormat(mContext, mItemListBottomLeftTv, R.string.product_coupon_price, StringUtils.getFormattedDoubleOrInt(bean.getCouponAmount()));
            }
            ViewUtil.setTextViewFormat(mContext, mItemListBottomRightTv, R.string.product_commission_price, StringUtils.getFormattedDoubleOrInt(bean.getCommissionMoney()));
            mItemListPriceTv.setText(StringUtils.getFormattedDoubleOrInt(bean.getDiscountsPriceAfter()));
            ViewUtil.setTextViewFormat(mContext, mItemListVolumeTv, R.string.list_volume_text, ViewUtil.getIVolume(bean.getVolume()));
            mItemListNameTv.setText(bean.getShopTitle());

            mItemListNameIconIv.setImageResource(ViewUtil.getIconImg(bean.getIcon()));
        }



        @Override
        public void onItemClicked() {
            super.onItemClicked();
            if (TimeUtils.isFrequentOperation()){
                return;
            }
            PreciseListBean bean = (PreciseListBean) getListData().get(mRealPosition);
            if (bean==null){
                return;
            }
            CommonSchemeJump.showPreciseDetailActivity(mContext, bean.getItemId(),bean.getIcon());
        }

    }
}
