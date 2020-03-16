package com.by.lizhiyoupin.app.main.findcircle.adapter;

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
import com.by.lizhiyoupin.app.io.bean.FindCircleChildListBean;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/20 10:41
 * Summary: 发圈-每日爆款-inner item底部
 */
public class DailyInnerBottomAdapter extends CommonRecyclerViewAdapter {
    public DailyInnerBottomAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_deily_child_bottom_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder {
        ImageView commendIv;
        TextView titleTv;
        TextView couponPriceTv;//券后价
        TextView parValueTv;//券面价
        TextView redPriceTv;//分享得红包

        public ViewHolder(View itemView) {
            super(itemView);
            commendIv = itemView.findViewById(R.id.daily_commend_iv);
            titleTv = itemView.findViewById(R.id.daily_commend_title_tv);
            couponPriceTv = itemView.findViewById(R.id.post_coupon_price_tv);
            parValueTv = itemView.findViewById(R.id.daily_par_value_tv);
            redPriceTv = itemView.findViewById(R.id.daily_red_price_tv);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            FindCircleChildListBean.CommodityInfoListBean listBean = (FindCircleChildListBean.CommodityInfoListBean) itemData;
            new GlideImageLoader(mContext, listBean.getCommodityImg())
                    .placeholder(R.drawable.empty_pic_list)
                    .transform(new RoundedCornersTransformation(DeviceUtil.dip2px(mContext, 8),0,RoundedCornersTransformation.CornerType.ALL))
                    .error(R.drawable.empty_pic_list)
                    .into(commendIv);
            titleTv.setText(listBean.getCommodityName());
            ViewUtil.setMoneyText(mContext, couponPriceTv, "¥" + StringUtils.getFormattedDoubleOrInt(listBean.getRealPrice()),
                    R.style.product_money11_FF005F, R.style.product_money15_FF005F);
            ViewUtil.setMoneyText(mContext, parValueTv, "¥" + StringUtils.getFormattedDoubleOrInt(listBean.getCouponAmount()),
                    R.style.product_money11_FF005F, R.style.product_money15_FF005F);
            ViewUtil.setTextViewFormat(mContext, redPriceTv, R.string.daily_price_text, StringUtils.getFormattedDoubleOrInt(listBean.getCouponAmount()));
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            if (TimeUtils.isFrequentOperation()){
                return;
            }
            FindCircleChildListBean.CommodityInfoListBean bean = (FindCircleChildListBean.CommodityInfoListBean) getListData().get(mRealPosition);
            if (bean == null) {
                return;
            }
            CommonSchemeJump.showPreciseDetailActivity(mContext,  bean.getCommodityId(),bean.getPlatformType());
        }
    }
}
