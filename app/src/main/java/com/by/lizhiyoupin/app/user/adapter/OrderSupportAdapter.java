package com.by.lizhiyoupin.app.user.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.OrderSupportBean;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 *
 *
 * @ jyx
 * Summary: 自营订单查询
 */
public class OrderSupportAdapter extends CommonRecyclerViewAdapter {


    public OrderSupportAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View root = mInflater.inflate(R.layout.item_order_support_layout, parent, false);
        return new ViewHolder(root);
    }



    class ViewHolder extends CommonViewHolder {
        TextView itemShopTv;//店铺
        ImageView itemSaveMoneyIv;//图片
        TextView itemTitleTv;//商品名
        TextView itemCreateTimeTv;//创建时间
        TextView itemOrderNumberTv;//订单号
        TextView itemAmountTv;//实付金额 ￥22
        TextView itemPayTv;//实际（预估）收益
        TextView itemActualIncomeTv;
        TextView see_logistics;
        private int corner;

        public ViewHolder(View itemView) {
            super(itemView);
            corner = DeviceUtil.dip2px(mContext, 8);
            itemShopTv = itemView.findViewById(R.id.item_save_money_shop_tv);
            itemSaveMoneyIv = itemView.findViewById(R.id.item_save_money_iv);
            itemTitleTv = itemView.findViewById(R.id.item_title_tv);
            itemCreateTimeTv = itemView.findViewById(R.id.item_create_time_tv);
            itemOrderNumberTv = itemView.findViewById(R.id.item_order_number_tv);
            itemAmountTv = itemView.findViewById(R.id.item_amount_tv);
            itemPayTv = itemView.findViewById(R.id.item_pay_tv);
            itemActualIncomeTv = itemView.findViewById(R.id.item_actual_income_tv);
            see_logistics= itemView.findViewById(R.id.see_logistics);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            OrderSupportBean detailsVO = (OrderSupportBean) itemData;
            itemTitleTv.setText(detailsVO.getTitle());
            ViewUtil.setTextViewFormat(mContext, itemCreateTimeTv, R.string.save_money_item_create_time_text,
                    TimeUtils.transDateToDateString(detailsVO.getCreatedTime(), 3));
            ViewUtil.setTextViewFormat(mContext, itemOrderNumberTv, R.string.save_money_item_order_number_text,
                    detailsVO.getOrderId());
            itemAmountTv.setText("¥" + StringUtils.getFormattedDouble(detailsVO.getPrice()));
            itemPayTv.setText("¥" + StringUtils.getFormattedDouble(detailsVO.getPrice()));

            new GlideImageLoader(mContext, detailsVO.getImg())
                    .error(R.drawable.empty_pic_list)
                    .placeholder(R.drawable.empty_pic_list)
                    .transform(new RoundedCornersTransformation(corner, 0, RoundedCornersTransformation.CornerType.LEFT))
                    .into(itemSaveMoneyIv);

            see_logistics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Bundle bundle=new Bundle();
                    bundle.putString(CommonConst.ORDER_SELFSUPPORADDRESS,detailsVO.getLogisticsCompany());
                    bundle.putString(CommonConst.ORDER_SELFSUPPORNUMBER,detailsVO.getLogisticsCode());

                    if (TextUtils.isEmpty(detailsVO.getLogisticsCompany())||TextUtils.isEmpty(detailsVO.getLogisticsCode())){
                        CommonToast.showToast("暂未物流信息");
                        return;
                    }
                    CommonSchemeJump.showActivity(mContext, "/app/OrderLogisticsActivity",bundle);

                }
            });
        }


    }
}
