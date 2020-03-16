package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.user.adapter.CommissionRecyclerViewAdapter;

/**
 * data:2019/11/11
 * author:jyx
 * function:
 */
public class EntranceDetailsAdapter extends CommissionRecyclerViewAdapter {
    public EntranceDetailsAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View root= mInflater.inflate(R.layout.item_explosionsnotice_details, parent, false);
        return new EntranceDetailsAdapter.ViewHolder(root);
    }

    class ViewHolder extends CommonViewHolder{
        TextView itemShopTv;//店铺
        TextView itemFansTv;//粉丝
        ImageView itemSaveMoneyIv;//图片
        TextView itemTitleTv;//商品名
        TextView itemCreateTimeTv;//创建时间
        TextView itemOrderNumberTv;//订单号
        TextView itemAmountTv;//实付金额 ￥22
        TextView itemPayTv;//实付金额
        private int corner;
        public ViewHolder(View itemView) {
            super(itemView);
            corner= DeviceUtil.dip2px(mContext, 8);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);

        }
    }
}
