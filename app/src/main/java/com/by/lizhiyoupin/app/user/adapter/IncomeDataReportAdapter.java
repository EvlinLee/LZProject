package com.by.lizhiyoupin.app.user.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.weight.MoneyTextView;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.IncomeReport;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.weight.week.OnTimeWeekSelectListener;

import java.util.Calendar;
import java.util.Date;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/7 19:32
 * Summary: 收益报表 Adapter
 */
public class IncomeDataReportAdapter extends CommonRecyclerViewAdapter {
    private OnTimeWeekSelectListener mWeekSelectListener;
    private int mTimeType;//0日报，1周报，2月报
    private int mTabType;//0上月（上周），1近3月（近3周），2 近6月（近6周）

    public IncomeDataReportAdapter(Context context, OnTimeWeekSelectListener weekSelectListener, int timeType, int tabType) {
        super(context);
        this.mWeekSelectListener = weekSelectListener;
        mTimeType = timeType;
        mTabType = tabType;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_income_data_record_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder implements View.OnClickListener {
        TextView mDataTimeTv;
        ImageView mIncomeSelectIv;
        MoneyTextView mIncomeDataTotalTv;
        TextView mDataShoppingGuideTv;
        TextView mDataTaobaoOrderTv;
        TextView mDataJingdOrderTv;
        TextView mDataPinddOrderTv;
        MoneyTextView mDataOptimizationTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mDataTimeTv = itemView.findViewById(R.id.data_time_tv);
            mIncomeSelectIv = itemView.findViewById(R.id.income_select_iv);
            mIncomeDataTotalTv = itemView.findViewById(R.id.income_data_total_tv);
            mDataShoppingGuideTv = itemView.findViewById(R.id.data_shopping_guide_tv);
            mDataTaobaoOrderTv = itemView.findViewById(R.id.data_taobao_order_tv);
            mDataJingdOrderTv = itemView.findViewById(R.id.data_jingd_order_tv);
            mDataPinddOrderTv = itemView.findViewById(R.id.data_pindd_order_tv);
            mDataOptimizationTv = itemView.findViewById(R.id.data_optimization_tv);
            mIncomeSelectIv.setOnClickListener(this);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            IncomeReport incomeReport = (IncomeReport) itemData;
            if (mTabType ==1 || mTabType == 2) {
                mIncomeSelectIv.setVisibility(View.GONE);
            } else {
                mIncomeSelectIv.setVisibility(View.VISIBLE);
            }
            mDataTimeTv.setText(incomeReport.getTitle());

            mIncomeDataTotalTv.setText(StringUtils.getFormattedDouble(incomeReport.getAllEstimate()));
            mDataShoppingGuideTv.setText(StringUtils.getFormattedDouble(incomeReport.getAllShoppingEstimate()));
            mDataTaobaoOrderTv.setText(StringUtils.getFormattedDouble(incomeReport.getAllTbEstimate()));
            mDataJingdOrderTv.setText(StringUtils.getFormattedDouble(incomeReport.getAllJdEstimate()));
            mDataPinddOrderTv.setText(StringUtils.getFormattedDouble(incomeReport.getAllPddEstimate()));
            mDataOptimizationTv.setText(StringUtils.getFormattedDouble(incomeReport.getAllGiftEstimate()));
        }

        @Override
        public void onClick(View v) {
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            switch (v.getId()) {
                case R.id.income_select_iv:
                    switch (mTimeType) {
                        case 0://日报
                            if (dayPickerView==null){
                                dayPickerView = DiaLogManager.showYearMonthDayPicker(mContext, new OnTimeSelectListener() {
                                    @Override
                                    public void onTimeSelect(Date date, View v) {
                                        String time = TimeUtils.DateToString(date, 0);
                                        mWeekSelectListener.onTimeWeekSelect(time, 1, null);
                                        Calendar instance = Calendar.getInstance();
                                        instance.setTime(date);
                                        dayPickerView.setDate(instance);
                                    }
                                });
                            }else {
                                dayPickerView.show();
                            }
                            break;
                        case 1://周报
                            DiaLogManager.showTimeWeekPicker(mContext, mWeekSelectListener);
                            break;
                        case 2://月报
                            if (IncomeDataReportAdapter.this.monthPickerView == null) {
                                IncomeDataReportAdapter.this.monthPickerView = DiaLogManager.showYearMonthPicker(mContext, Calendar.getInstance(), new OnTimeSelectListener() {
                                    @Override
                                    public void onTimeSelect(Date date, View v) {
                                        String time = TimeUtils.DateToString(date, 0);
                                        mWeekSelectListener.onTimeWeekSelect(time, 1, null);
                                        Calendar instance = Calendar.getInstance();
                                        instance.setTime(date);
                                        IncomeDataReportAdapter.this.monthPickerView.setDate(instance);
                                    }
                                });
                            }else {
                                IncomeDataReportAdapter.this.monthPickerView.show();
                            }
                            break;
                    }
                    break;
            }
        }
    }

    private TimePickerView monthPickerView;
    private TimePickerView dayPickerView;
}
