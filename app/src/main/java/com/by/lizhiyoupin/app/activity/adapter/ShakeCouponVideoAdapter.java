package com.by.lizhiyoupin.app.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBean;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.stack.ActivityStack;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import androidx.appcompat.app.AppCompatActivity;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/15 20:27
 * Summary:  抖券视频静态图
 * 与 TikTokController2 配合 adapter来滑动与现实静态图片，TikTokController2 现实动态视频
 */
public class ShakeCouponVideoAdapter extends CommonRecyclerViewAdapter {

    public ShakeCouponVideoAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.layout_tiktok_s_controller, parent, false);

        return new VideoViewHolder(inflate);
    }

    //注意這個是静态页面 动态在 TikTokController2
    public class VideoViewHolder extends CommonViewHolder implements View.OnClickListener {
        ImageView mImageView;

        TextView mShakeCouponLoveTv;
        TextView mShakeCouponTextTv;
        TextView mShakeCouponShareTv;
        ImageView mItemListTopIv;
        TextView mItemListTitleTv;
        TextView mItemListYTv;
        TextView mItemListPriceTv;
        TextView mItemListVolumeTv;
        TextView mItemListBottomLeftTv;
        TextView mItemListBottomRightTv;
        TextView mShakeCouponBuyTv;


        VideoViewHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.iv_thumb);
            mShakeCouponLoveTv = itemView.findViewById(R.id.shake_coupon_love_tv);
            mShakeCouponTextTv = itemView.findViewById(R.id.shake_coupon_text_tv);
            mShakeCouponShareTv = itemView.findViewById(R.id.shake_coupon_share_tv);
            mItemListTopIv = itemView.findViewById(R.id.item_list_top_iv);
            mItemListTitleTv = itemView.findViewById(R.id.item_list_title_tv);
            mItemListYTv = itemView.findViewById(R.id.item_list_y_tv);
            mItemListPriceTv = itemView.findViewById(R.id.item_list_price_tv);
            mItemListVolumeTv = itemView.findViewById(R.id.item_list_volume_tv);
            mItemListBottomLeftTv = itemView.findViewById(R.id.item_list_bottom_left_tv);
            mItemListBottomRightTv = itemView.findViewById(R.id.item_list_bottom_right_tv);
            mShakeCouponBuyTv = itemView.findViewById(R.id.shake_coupon_buy_tv);

            mShakeCouponTextTv.setOnClickListener(this);
            mShakeCouponLoveTv.setOnClickListener(this);
            mShakeCouponShareTv.setOnClickListener(this);
            mShakeCouponBuyTv.setOnClickListener(this);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            //第一帧
            ShakeCouponBean mShakeCouponBean = (ShakeCouponBean) mListData.get(mRealPosition);
            refreshProductView(mShakeCouponBean);
        }

        public void refreshProductView(ShakeCouponBean mShakeCouponBean) {
            if (mShakeCouponBean == null) {
                return;
            }
            new GlideImageLoader(mContext, mShakeCouponBean.getDyFirstFrame())
                    .error(android.R.color.white)
                    .placeholder(android.R.color.white)
                    .into(mImageView);
            mShakeCouponLoveTv.setText(StringUtils.getThumbsCount(mShakeCouponBean.getDyThumbsCount()));

            mItemListTitleTv.setText(mShakeCouponBean.getTitle());
            if (mShakeCouponBean.getCouponAmount() == 0) {
                mItemListBottomLeftTv.setVisibility(View.GONE);
            } else {
                mItemListBottomLeftTv.setVisibility(View.VISIBLE);
                ViewUtil.setTextViewFormat(mContext, mItemListBottomLeftTv,
                        R.string.product_coupon_price, StringUtils.getFormattedDoubleOrInt(mShakeCouponBean.getCouponAmount()));
            }
            ViewUtil.setTextViewFormat(mContext, mItemListBottomRightTv, R.string.product_commission_price,
                    StringUtils.getFormattedDoubleOrInt(mShakeCouponBean.getCommissionMoney()));
            mItemListPriceTv.setText(StringUtils.getFormattedDoubleOrInt(mShakeCouponBean.getZkFinalPrice()));
            ViewUtil.setTextViewFormat(mContext, mItemListVolumeTv, R.string.list_payment_text, StringUtils.getIVolume(mShakeCouponBean.getVolume()));

            ViewUtil.setDrawableOfTextView(mItemListTitleTv, ViewUtil.getIconImg(mShakeCouponBean.getIcon()), ViewUtil.DrawableDirection.LEFT);

            new GlideImageLoader(this, mShakeCouponBean.getPictUrl())
                    .error(R.drawable.empty_pic_list)
                    .placeholder(R.drawable.empty_pic_list)
                    .into(mItemListTopIv);
            mShakeCouponShareTv.setText(String.valueOf(mShakeCouponBean.getDyShareCount()));

        }


        @Override
        public void onClick(View v) {
            if (TimeUtils.isFrequentOperation()){
                return;
            }
            switch (v.getId()) {
                case R.id.shake_coupon_love_tv:
                    //点赞
                    break;
                case R.id.shake_coupon_text_tv:
                    //文案
                    ShakeCouponBean mShakeCouponBean1 = (ShakeCouponBean) mListData.get(mRealPosition);
                    if (mShakeCouponBean1 == null) {
                        return;
                    }
                    DiaLogManager.showCopyShakeCouponDialog((AppCompatActivity) ActivityStack.currentActivity(), mShakeCouponBean1.getDyTitle());
                    break;
                case R.id.shake_coupon_share_tv:
                    //分享
                     //静态区不分享，去TikTokController2 内容的shake_coupon_share_tv click事件分享
                    break;
                case R.id.shake_coupon_buy_tv:
                    ShakeCouponBean mShakeCouponBean = (ShakeCouponBean) mListData.get(mRealPosition);
                    if (mShakeCouponBean == null) {
                        return;
                    }
                    //去购买 跳转商品详情页
                    CommonSchemeJump.showPreciseDetailActivity(mContext, mShakeCouponBean.getItemId(),mShakeCouponBean.getIcon());
                    break;
                default:
                    break;
            }
        }

    }

}
