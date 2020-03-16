package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.BaseOfDelegateAdapter;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.BaseOfViewHolder;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBean;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/24 20:02
 * Summary: 精选 抖券
 */
public class PreciseShakeCouponListAdapter extends BaseOfDelegateAdapter {
    private int iLimit=20;
    private  int margin;
    public PreciseShakeCouponListAdapter(Context context, LayoutHelper layoutHelper, int normalViewType,int limit) {
        super(context, layoutHelper, normalViewType);
        this.iLimit=limit;
        margin=DeviceUtil.dip2px(context,10);
    }

    @Override
    protected BaseOfViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_shake_coupon_list, parent, false);

        return new ViewHolder(inflate);
    }
    class ViewHolder extends BaseOfViewHolder {
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
        public void bindData(Object itemData,int viewType) {
            super.bindData(itemData,viewType);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (mRealPosition==0||mRealPosition==1){
                layoutParams.setMargins(0,margin,0,0);
            }else {
                layoutParams.setMargins(0,0,0,0);
            }
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
            if (TimeUtils.isFrequentOperation()){
                return;
            }
            ShakeCouponBean bean = (ShakeCouponBean) getListData().get(mRealPosition);
            Bundle bundle = new Bundle();
            bundle.putSerializable(CommonConst.KEY_SHAKE_COUPON_VALUE, bean);
            bundle.putInt(CommonConst.KEY_INDICATOR_TYPE, 0);
            bundle.putLong(CommonConst.KEY_THREE_DETAIL_ID, bean.getItemId());
            bundle.putLong(CommonConst.KEY_SHAKE_COUPON_FROM, 1);
            bundle.putInt(CommonConst.KEY_SHAKE_COUPON_PAGE, getListData().size() < iLimit ? 1 : getListData().size() / (iLimit>0?iLimit:1));
            CommonSchemeJump.showShakeCouponMainRvActivity(mContext, bundle);
        }
    }
}
