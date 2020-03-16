package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.RecordsBean;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/29 12:02
 * Summary: 本地服务器数据
 */
public class ProductSortListAdapter extends CommonRecyclerViewAdapter {
    public static final int TYPE_SINGLE = 0;
    public static final int TYPE_OTHER = 1;
    public int type = TYPE_OTHER;

    public ProductSortListAdapter(Context context) {
        super(context);
    }

    public void setType(int itemType) {
        this.type = itemType;
    }

    public int getType() {
        return type;
    }

    @Override
    public int getNormalViewType(int position) {
        return type;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate;
        if (viewType == TYPE_OTHER) {
            inflate = mInflater.inflate(R.layout.item_precise_selection_list, parent, false);
        } else {
            inflate = mInflater.inflate(R.layout.item_precise_single_list, parent, false);
        }
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
        public void bindData(Object itemData) {
            super.bindData(itemData);
            RecordsBean bean = (RecordsBean) itemData;
            if (type == TYPE_OTHER) {
                new GlideImageLoader(mContext, bean.getMainImg())
                        .error(R.drawable.empty_pic_list)
                        .transform(new RoundedCornersTransformation(DeviceUtil.dip2px(mContext, 8),0,RoundedCornersTransformation.CornerType.TOP))
                        .into(mItemListTopIv);
            } else {
                new GlideImageLoader(mContext, bean.getMainImg())
                        .error(R.drawable.empty_pic_list)
                        .into(mItemListTopIv);
            }
            mItemListTitleTv.setText(bean.getName());
            if (bean.getCouponAmount()==0){
                mItemListBottomLeftTv.setVisibility(View.GONE);
            }else {
                mItemListBottomLeftTv.setVisibility(View.VISIBLE);
            }
            ViewUtil.setTextViewFormat(mContext, mItemListBottomLeftTv, R.string.product_coupon_price, StringUtils.getFormattedDoubleOrInt(bean.getCouponAmount()));
            ViewUtil.setTextViewFormat(mContext, mItemListBottomRightTv, R.string.product_commission_price, StringUtils.getFormattedDoubleOrInt(bean.getCommissionAmount()));
            mItemListPriceTv.setText(StringUtils.getFormattedDoubleOrInt(bean.getRealPrice()));
            ViewUtil.setTextViewFormat(mContext, mItemListVolumeTv, R.string.list_volume_text, getIVolume(bean.getMonthSales()));
            mItemListNameTv.setText(bean.getShopName());

            mItemListNameIconIv.setImageResource(ViewUtil.getIconImg(bean.getIcon()));

        }

        private String getIVolume(long volume) {
            if (volume >= 10000) {
                return volume / 10000 + "万+";
            } else {
                return String.valueOf(volume);
            }
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            RecordsBean bean = (RecordsBean) getListData().get(mRealPosition);

            CommonSchemeJump.showPreciseDetailActivity(mContext, bean.getCommodityId(), bean.getIcon());
        }
    }

}
