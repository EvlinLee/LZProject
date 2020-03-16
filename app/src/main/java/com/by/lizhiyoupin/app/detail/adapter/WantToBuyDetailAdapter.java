package com.by.lizhiyoupin.app.detail.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.GuideArticleDetailBean;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/25 16:10
 * Summary: 种草详情adapter
 */
public class WantToBuyDetailAdapter extends CommonRecyclerViewAdapter {


    public WantToBuyDetailAdapter(Context context) {
        super(context);
    }


    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_want_to_buy_detail_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder implements View.OnClickListener {
        ImageView mItemListTopIv;//顶部图片
        TextView mItemListTitleTv;//顶部title
        TextView mItemListBottomLeftTv;//优惠券 如20元券
        TextView mItemListBottomRightTv;//返红包20元
        TextView mItemListPriceTv;//券后价格  345
        TextView mItemListVolumeTv;//月销售   10万+

        private int corner;

        public ViewHolder(View itemView) {
            super(itemView);
            corner = DeviceUtil.dip2px(mContext, 8);
            mItemListTopIv = itemView.findViewById(R.id.item_list_top_iv);
            mItemListTitleTv = itemView.findViewById(R.id.item_list_title_tv);
            mItemListBottomLeftTv = itemView.findViewById(R.id.item_list_bottom_left_tv);
            mItemListBottomRightTv = itemView.findViewById(R.id.item_list_bottom_right_tv);
            mItemListPriceTv = itemView.findViewById(R.id.item_list_price_tv);
            mItemListVolumeTv = itemView.findViewById(R.id.item_list_volume_tv);
            itemView.findViewById(R.id.container_cl).setOnClickListener(this);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            GuideArticleDetailBean.CommodityListBean bean = (GuideArticleDetailBean.CommodityListBean) itemData;

            new GlideImageLoader(mContext, bean.getCommodityImg())
                    .error(R.drawable.empty_pic_list)
                    .transform(new RoundedCornersTransformation(corner,
                            0, RoundedCornersTransformation.CornerType.ALL))
                    .into(mItemListTopIv);

            mItemListTitleTv.setText(bean.getCommodityName());
            if (bean.getCouponAmount() == 0) {
                mItemListBottomLeftTv.setVisibility(View.GONE);
            } else {
                mItemListBottomLeftTv.setVisibility(View.VISIBLE);
                ViewUtil.setTextViewFormat(mContext, mItemListBottomLeftTv,
                        R.string.product_coupon_price, StringUtils.getFormattedDoubleOrInt(bean.getCouponAmount()));
            }
            ViewUtil.setTextViewFormat(mContext, mItemListBottomRightTv, R.string.product_commission_price, bean.getCommissionAmount());
            mItemListPriceTv.setText(bean.getRealPrice());
            ViewUtil.setTextViewFormat(mContext, mItemListVolumeTv, R.string.list_volume_text, StringUtils.getIVolume(bean.getMonthSales()));


        }


        @Override
        public void onClick(View v) {
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            GuideArticleDetailBean.CommodityListBean listBean = (GuideArticleDetailBean.CommodityListBean) getListData().get(mRealPosition);
            if (listBean==null){
                return;
            }
            CommonSchemeJump.showPreciseDetailActivity(mContext, Long.valueOf(listBean.getCommodityId()), listBean.getPlatformType());


        }
    }
}
