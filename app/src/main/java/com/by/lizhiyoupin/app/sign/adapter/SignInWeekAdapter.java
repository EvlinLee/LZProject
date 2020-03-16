package com.by.lizhiyoupin.app.sign.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.SignInBean;

import androidx.constraintlayout.widget.Group;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/13 10:56
 * Summary: 签到 时间线
 */
public class SignInWeekAdapter extends CommonRecyclerViewAdapter {

    public SignInWeekAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_sign_in_week_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder {
        ImageView sWeekCheckIv;
        View leftLine;
        View rightLine;
        TextView weekTv;
        Group week_group;
        Group week_group7;

        public ViewHolder(View itemView) {
            super(itemView);
            week_group = itemView.findViewById(R.id.week_group);
            week_group7 = itemView.findViewById(R.id.week_group7);
            leftLine = itemView.findViewById(R.id.left_line);
            sWeekCheckIv = itemView.findViewById(R.id.week_check_iv);
            rightLine = itemView.findViewById(R.id.right_line);
            weekTv = itemView.findViewById(R.id.week_tv);

        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            SignInBean.UserSignWeekInfoVOListBean bean = (SignInBean.UserSignWeekInfoVOListBean) itemData;
            if (mRealPosition == 0) {
                leftLine.setVisibility(View.GONE);
            } else {
                leftLine.setVisibility(View.VISIBLE);
            }
            if (mRealPosition == 6) {
                week_group7.setVisibility(View.VISIBLE);
                week_group.setVisibility(View.GONE);

            } else {
                week_group7.setVisibility(View.GONE);
                week_group.setVisibility(View.VISIBLE);

            }
            switch (bean.getSignStatus()){
                case 0:// 0 未签到
                    sWeekCheckIv.setImageResource(R.drawable.sign_in_wait);
                    break;
                case 1:// 1 已签到
                    sWeekCheckIv.setImageResource(R.drawable.sign_in_check);
                    break;
                case 2:// 2断签
                    sWeekCheckIv.setImageResource(R.drawable.sign_in_close);
                    break;
            }

            switch (bean.getWeekDays()) {
                case 1:
                    weekTv.setText("周一");
                    break;
                case 2:
                    weekTv.setText("周二");
                    break;
                case 3:
                    weekTv.setText("周三");
                    break;
                case 4:
                    weekTv.setText("周四");
                    break;
                case 5:
                    weekTv.setText("周五");
                    break;
                case 6:
                    weekTv.setText("周六");
                    break;
                case 7:
                    break;
            }

        }
    }
}
