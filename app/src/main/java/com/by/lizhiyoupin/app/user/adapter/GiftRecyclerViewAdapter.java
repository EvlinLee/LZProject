package com.by.lizhiyoupin.app.user.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.GiftIncomeVO;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/5 20:14
 * Summary: 礼包收益
 */
public class GiftRecyclerViewAdapter extends CommonRecyclerViewAdapter {
    public GiftRecyclerViewAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_gift_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder   {
        private TextView timeTv;//
        private TextView totalPaymentsTv;//总付款笔数
        private TextView totalEstimatedTv;//总预估收益
        private TextView exclusiveNumberFansPaymentsTv;//专属粉丝付款笔数
        private TextView exclusiveFansPaymentsTv;//专属粉丝预估收益
        private TextView normalNumberFansPaymentsTv;//普通粉丝付款笔数
        private TextView normalFansEstimatedTv;//普通粉丝预估收益

        public ViewHolder(View itemView) {
            super(itemView);
            timeTv = itemView.findViewById(R.id.time_tv);
            totalPaymentsTv = itemView.findViewById(R.id.total_payments_tv);
            totalEstimatedTv = itemView.findViewById(R.id.total_estimated_tv);
            exclusiveNumberFansPaymentsTv = itemView.findViewById(R.id.exclusive_number_fans_payments_tv);
            exclusiveFansPaymentsTv = itemView.findViewById(R.id.exclusive_fans_total_estimated_tv);
            normalNumberFansPaymentsTv = itemView.findViewById(R.id.normal_number_fans_payments_tv);
            normalFansEstimatedTv = itemView.findViewById(R.id.normal_fans_estimated_tv);

        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            GiftIncomeVO giftIncomeVO= (GiftIncomeVO) itemData;
            totalPaymentsTv.setText(String.valueOf(giftIncomeVO.getAllPaymentNumber()));
            totalEstimatedTv.setText(StringUtils.getFormattedDouble(giftIncomeVO.getAllEstimate()));
            exclusiveNumberFansPaymentsTv.setText(String.valueOf(giftIncomeVO.getFansPaymentNumber()));
            exclusiveFansPaymentsTv.setText(StringUtils.getFormattedDouble(giftIncomeVO.getFansEstimate()));
            normalNumberFansPaymentsTv.setText(String.valueOf(giftIncomeVO.getCommonlyFansPaymentNumber()));
            normalFansEstimatedTv.setText(StringUtils.getFormattedDouble(giftIncomeVO.getCommonlyFansEstimate()));

            switch (giftIncomeVO.getType()){
                case 1:
                    timeTv.setText("今日");
                    break;
                case 2:
                    timeTv.setText("本周");
                    break;
                case 3:
                    timeTv.setText("本月");
                    break;
            }
        }
    }
}
