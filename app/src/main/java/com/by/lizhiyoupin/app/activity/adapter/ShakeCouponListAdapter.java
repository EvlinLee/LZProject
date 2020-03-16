package com.by.lizhiyoupin.app.activity.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBean;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/14 20:22
 * Summary:抖券列表Adapter
 */
public class ShakeCouponListAdapter extends CommonRecyclerViewAdapter {
    private int mTabType;

    public ShakeCouponListAdapter(Context context, int tabType) {
        super(context);
        mTabType = tabType;
    }


    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_shake_coupon_list, parent, false);

        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder {
        ImageView mItemListTopIv;//顶部图片
        TextView mItemListTitleTv;//顶部title
        TextView mItemListBottomLeftTv;//优惠券 如20元券
        TextView mItemListBottomRightTv;//返红包20元
        TextView mItemListPriceTv;//券后价格  345
        TextView mItemListVolumeTv;//月销售   10万+
        TextView mItemListNameTv;//商户名
        ImageView mItemListNameIconIv;//icon
        TextView mThumbsTv;//点赞

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
            mThumbsTv = itemView.findViewById(R.id.thumbs_tv);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            ShakeCouponBean bean = (ShakeCouponBean) itemData;

            new GlideImageLoader(mContext, bean.getPictUrl())
                    .error(R.drawable.empty_pic_list)
                    .transform(new RoundedCornersTransformation(DeviceUtil.dip2px(mContext, 8),0,RoundedCornersTransformation.CornerType.TOP))
                    .into(mItemListTopIv);

            mItemListTitleTv.setText(bean.getTitle());
            if (bean.getCouponAmount() == 0) {
                mItemListBottomLeftTv.setVisibility(View.GONE);
            } else {
                mItemListBottomLeftTv.setVisibility(View.VISIBLE);
                ViewUtil.setTextViewFormat(mContext, mItemListBottomLeftTv,
                        R.string.product_coupon_price, StringUtils.getFormattedDoubleOrInt(bean.getCouponAmount()));
            }
            ViewUtil.setTextViewFormat(mContext, mItemListBottomRightTv, R.string.product_commission_price,
                    StringUtils.getFormattedDoubleOrInt(bean.getCommissionMoney()));
            mItemListPriceTv.setText(StringUtils.getFormattedDoubleOrInt(bean.getDiscountsPriceAfter()));
            ViewUtil.setTextViewFormat(mContext, mItemListVolumeTv, R.string.list_payment_text, ViewUtil.getIVolume(bean.getVolume()));
            mItemListNameTv.setText(bean.getShopTitle());
            mItemListNameIconIv.setImageResource(ViewUtil.getIconImg(bean.getIcon()));
            mThumbsTv.setText(StringUtils.getThumbsCount(bean.getDyThumbsCount()));

        }


        @Override
        public void onItemClicked() {
            super.onItemClicked();
            ShakeCouponBean bean = (ShakeCouponBean) getListData().get(mRealPosition);
            Bundle bundle = new Bundle();
            bundle.putSerializable(CommonConst.KEY_SHAKE_COUPON_VALUE, bean);
            bundle.putInt(CommonConst.KEY_INDICATOR_TYPE, mTabType);
            bundle.putLong(CommonConst.KEY_THREE_DETAIL_ID, bean.getItemId());
            bundle.putLong(CommonConst.KEY_SHAKE_COUPON_FROM, 0);
            bundle.putInt(CommonConst.KEY_SHAKE_COUPON_PAGE, getListData().size() < 10 ? 1 : getListData().size() / 10);
            CommonSchemeJump.showShakeCouponMainRvActivity(mContext, bundle);
        }
    }


}
