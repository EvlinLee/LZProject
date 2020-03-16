package com.by.lizhiyoupin.app.detail.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/28 15:40
 * Summary:荔枝推荐 三方 列表adapter
 */
public class ProductRecommendListAdapter extends CommonRecyclerViewAdapter {


    public ProductRecommendListAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_detail_recommend_list, parent, false);

        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder {
        ImageView mItemListTopIv;//顶部图片
        TextView mItemListTitleTv;//顶部title
        TextView mItemListBottomLeftTv;//优惠券 如20元券
        TextView mItemListBottomRightTv;//返红包20元
        TextView mItemListPriceTv;//券后价格  345
        TextView mItemListVolumeTv;//月销售   10万+
        int raduis;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemListTopIv = itemView.findViewById(R.id.item_list_top_iv);
            mItemListTitleTv = itemView.findViewById(R.id.item_list_title_tv);
            mItemListBottomLeftTv = itemView.findViewById(R.id.item_list_bottom_left_tv);
            mItemListBottomRightTv = itemView.findViewById(R.id.item_list_bottom_right_tv);
            mItemListPriceTv = itemView.findViewById(R.id.item_list_price_tv);
            mItemListVolumeTv = itemView.findViewById(R.id.item_list_volume_tv);
            raduis= DeviceUtil.dip2px(mContext, 8);

        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            PreciseListBean bean = (PreciseListBean) itemData;

            new GlideImageLoader(mContext, bean.getPictUrl())
                    .error(R.drawable.empty_pic_list)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)////不缓存资源
                    .skipMemoryCache(true)////禁止Glide内存缓存
                    .transform(new RoundedCornersTransformation(raduis,0,RoundedCornersTransformation.CornerType.TOP))
                    .into(mItemListTopIv);

            mItemListTitleTv.setText(bean.getTitle());
            ViewUtil.setTextViewFormat(mContext, mItemListBottomLeftTv, R.string.product_coupon_price, StringUtils.getFormattedDoubleOrInt(bean.getCouponAmount()));
            ViewUtil.setTextViewFormat(mContext, mItemListBottomRightTv, R.string.product_commission2_price, StringUtils.getFormattedDoubleOrInt(bean.getCommissionMoney()));
            ViewUtil.setMoneyText(mContext, mItemListPriceTv, "¥" + StringUtils.getFormattedDoubleOrInt(bean.getDiscountsPriceAfter()), R.style.product_money14_111111, R.style.product_money18_111111);
            ViewUtil.setTextViewFormat(mContext, mItemListVolumeTv, R.string.list_volume_text, ViewUtil.getIVolume(bean.getVolume()));
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
