package com.by.lizhiyoupin.app.user.adapter;

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
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.IncomeDetailsVO;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/6 17:00
 * Summary: 收益明细
 */
public class IncomeDetailAdapter extends CommonRecyclerViewAdapter {
    private int mProfitType = 1;

    public IncomeDetailAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View root = mInflater.inflate(R.layout.item_detail_money_layout, parent, false);
        return new ViewHolder(root);
    }

    /**
     * 收益 类型
     *
     * @param profitType 收益类型 1礼包 2导购
     */
    public void setProfitType(int profitType) {
        mProfitType = profitType;
    }

    class ViewHolder extends CommonViewHolder {
        TextView itemShopTv;//店铺
        TextView itemFansRightTv;//右侧显示的粉丝标识
        ImageView itemSaveMoneyIv;//图片
        TextView itemTitleTv;//商品名
        TextView itemCreateTimeTv;//创建时间
        TextView itemOrderNumberTv;//订单号
        TextView itemAmountTv;//实付金额 ￥22
        TextView itemPayTv;//实际（预估）收益
        TextView itemActualIncomeTv;
        private int corner;
        public ViewHolder(View itemView) {
            super(itemView);
            corner = DeviceUtil.dip2px(mContext, 8);
            itemShopTv = itemView.findViewById(R.id.item_save_money_shop_tv);
            itemFansRightTv = itemView.findViewById(R.id.item_fans_tv);
            itemSaveMoneyIv = itemView.findViewById(R.id.item_save_money_iv);
            itemTitleTv = itemView.findViewById(R.id.item_title_tv);
            itemCreateTimeTv = itemView.findViewById(R.id.item_create_time_tv);
            itemOrderNumberTv = itemView.findViewById(R.id.item_order_number_tv);
            itemAmountTv = itemView.findViewById(R.id.item_amount_tv);
            itemPayTv = itemView.findViewById(R.id.item_pay_tv);
            itemActualIncomeTv = itemView.findViewById(R.id.item_actual_income_tv);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            IncomeDetailsVO detailsVO = (IncomeDetailsVO) itemData;
            itemTitleTv.setText(detailsVO.getTitle());
            ViewUtil.setTextViewFormat(mContext, itemCreateTimeTv, R.string.save_money_item_create_time_text,
                    TimeUtils.transDateToDateString(detailsVO.getCreateTime(), 3));
            ViewUtil.setTextViewFormat(mContext, itemOrderNumberTv, R.string.save_money_item_order_number_text,
                    detailsVO.getOrderId());
            itemAmountTv.setText("¥" + StringUtils.getFormattedDouble(detailsVO.getPayment()));
            switch (detailsVO.getStatus()) {
                case 1:
                    //1未结算 预估
                    itemActualIncomeTv.setText(R.string.save_money_item_estimate_actual_income_text);
                    break;
                case 2:
                    //2已结算 实际
                    itemActualIncomeTv.setText(R.string.save_money_item_actual_income_text);
                    break;
            }
            itemPayTv.setText("¥" + StringUtils.getFormattedDouble(detailsVO.getProfit()));
           /* if (mProfitType == 2) {
                itemFansRightTv.setVisibility(View.VISIBLE);
            } else {
                itemFansRightTv.setVisibility(View.GONE);
            }*/
            switch (detailsVO.getFansType()) {
                case 1:
                    itemFansRightTv.setVisibility(View.VISIBLE);
                    //1专属
                    itemFansRightTv.setText("专属粉丝");
                    break;
                case 2:
                    itemFansRightTv.setVisibility(View.VISIBLE);
                    // 2普通
                    itemFansRightTv.setText("普通粉丝");
                    break;
                default:
                    itemFansRightTv.setVisibility(View.GONE);
                    break;
            }

            int platformType = detailsVO.getPlatformType();
            switch (platformType) {
                case 0:
                    //全部
                    itemShopTv.setText(" ");
                    break;
                case 1:
                    //1淘宝 2京东 3拼多多
                    itemShopTv.setText(R.string.search_clip_platform_taobao);
                    ViewUtil.setDrawableOfTextView(itemShopTv, R.drawable.temp_detail_tao_big_pic, ViewUtil.DrawableDirection.LEFT);
                    break;
                case 2:
                    itemShopTv.setText(R.string.search_clip_platform_jingd);
                    ViewUtil.setDrawableOfTextView(itemShopTv, R.drawable.temp_detail_jingd_pic, ViewUtil.DrawableDirection.LEFT);
                    break;
                case 3:
                    itemShopTv.setText(R.string.search_clip_platform_pindd);
                    ViewUtil.setDrawableOfTextView(itemShopTv, R.drawable.temp_detail_pingdd_big_pic, ViewUtil.DrawableDirection.LEFT);
                    break;
                default:
                    itemShopTv.setText(R.string.search_clip_platform_lz);
                    ViewUtil.setDrawableOfTextView(itemShopTv, R.drawable.temp_detail_lz_pic, ViewUtil.DrawableDirection.LEFT);
                    break;
            }

            new GlideImageLoader(mContext, detailsVO.getImgages())
                    .error(R.drawable.empty_pic_list)
                    .placeholder(R.drawable.empty_pic_list)
                    .transform(new RoundedCornersTransformation(corner, 0, RoundedCornersTransformation.CornerType.LEFT))
                    .into(itemSaveMoneyIv);
        }


    }
}
