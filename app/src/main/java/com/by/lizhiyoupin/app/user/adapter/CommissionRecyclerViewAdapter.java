package com.by.lizhiyoupin.app.user.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.OrderIncomeVO;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/5 20:14
 * Summary: 佣金收益
 */
public class CommissionRecyclerViewAdapter extends CommonRecyclerViewAdapter {
    public CommissionRecyclerViewAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_commission_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder implements View.OnClickListener {
        private TextView timeTv;//
        private TextView loadMoreTv;// 查看更多
        private TextView totalPaymentsTv;//总付款笔数
        private TextView numberFansPaymentsTv;//粉丝付款笔数
        private TextView totalEstimatedTv;//总预估收益
        private TextView fansTotalEstimatedTv;//粉丝预估收益

        public ViewHolder(View itemView) {
            super(itemView);
            timeTv = itemView.findViewById(R.id.time_tv);
            loadMoreTv = itemView.findViewById(R.id.load_more_tv);
            totalPaymentsTv = itemView.findViewById(R.id.total_payments_tv);
            numberFansPaymentsTv = itemView.findViewById(R.id.number_fans_payments_tv);
            totalEstimatedTv = itemView.findViewById(R.id.total_estimated_tv);
            fansTotalEstimatedTv = itemView.findViewById(R.id.fans_total_estimated_tv);
            loadMoreTv.setOnClickListener(this);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            OrderIncomeVO orderIncomeVO= (OrderIncomeVO) itemData;
            totalPaymentsTv.setText(String.valueOf(orderIncomeVO.getAllPaymentNumber()));
            numberFansPaymentsTv.setText(String.valueOf(orderIncomeVO.getFansPaymentNumber()));
            totalEstimatedTv.setText(StringUtils.getFormattedDouble(orderIncomeVO.getAllEstimate()));
            fansTotalEstimatedTv.setText(StringUtils.getFormattedDouble(orderIncomeVO.getFansEstimate()));
            switch (orderIncomeVO.getType()){
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

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.load_more_tv:
                    OrderIncomeVO orderIncomeVO= (OrderIncomeVO) getListData().get(mRealPosition);
                    Bundle bundle=new Bundle();
                    bundle.putInt(CommonConst.KEY_INCOME_TIME_TYPE,orderIncomeVO.getType()-1);
                    //查看更多
                    CommonSchemeJump.showIncomeDataReportActivity(mContext,bundle);
                    break;
            }
        }
    }
}
