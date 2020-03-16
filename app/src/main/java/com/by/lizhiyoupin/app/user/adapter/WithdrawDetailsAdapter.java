package com.by.lizhiyoupin.app.user.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.io.bean.PresentationDetailsBean;

/**
 * data:2019/11/7
 * author:jyx
 * function:提现明细
 */
public class WithdrawDetailsAdapter extends CommissionRecyclerViewAdapter {

    public WithdrawDetailsAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View root = mInflater.inflate(R.layout.item_withdraw_details, parent, false);
        return new WithdrawDetailsAdapter.ViewHolder(root);
    }

    class ViewHolder extends CommonViewHolder {
        TextView text_type;//类型
        TextView text_time;//时间
        TextView text_money;//金额
        private int corner;

        public ViewHolder(View itemView) {
            super(itemView);
            corner = DeviceUtil.dip2px(mContext, 8);
            text_money = itemView.findViewById(R.id.text_money);
            text_time = itemView.findViewById(R.id.text_time);
            text_type = itemView.findViewById(R.id.text_type);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            PresentationDetailsBean bean = (PresentationDetailsBean) itemData;
            text_money.setText("¥ " + bean.getAmount());
            text_time.setText(TimeUtils.transForDate(Long.parseLong(bean.getCreatedTime()), "yyyy-MM-dd HH:mm:ss"));
            int status = bean.getStatus();
            Long id = bean.getId();
            if (status == 0) {
                text_type.setTextColor(Color.parseColor("#FF999999"));
                text_type.setText("提现失败");
            } else if (status == 1) {
                text_type.setTextColor(Color.parseColor("#FF333333"));
                text_type.setText("提现审核中");
            } else {
                text_type.setTextColor(Color.parseColor("#FFEF1818"));
                text_type.setText("提现成功");
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonConst.WITHDRAW_EID, String.valueOf(id));
                    CommonSchemeJump.showActivity(mContext, "/app/CashDetailsActivity", bundle);
                }
            });
        }
    }
}
