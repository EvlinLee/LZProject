package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/28 13:42
 * Summary:首页 抢购列表  adapter 4
 */
public class RushBuyTopAdapter extends CommonRecyclerViewAdapter {

    public RushBuyTopAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_of_rush_buy_top, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder{
        private TextView rushBugTopTv;
        private TextView rushBugTipLeftTv;
        private TextView rushBugTipRightTv;
        private ImageView rushBugBottomLeftIv;
        private ImageView rushBugBottomRightIv;

        public ViewHolder(View itemView) {
            super(itemView);
            rushBugTopTv = itemView.findViewById(R.id.rush_bug_top_tv);
            rushBugTipLeftTv = itemView.findViewById(R.id.rush_bug_tip_left_tv);
            rushBugTipRightTv = itemView.findViewById(R.id.rush_bug_tip_right_tv);
            rushBugBottomLeftIv = itemView.findViewById(R.id.rush_bug_bottom_left_iv);
            rushBugBottomRightIv = itemView.findViewById(R.id.rush_bug_bottom_right_iv);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            switch (mRealPosition) {
                case 0:
                    itemView.setBackgroundResource(R.drawable.shape_bg_top_left_corner);
                    break;
                case 1:
                    itemView.setBackgroundResource(R.drawable.shape_bg_top_right_corner);
                    break;
                default:
                    itemView.setBackgroundResource(R.color.white_100);
                    break;

            }
            rushBugTopTv.setText("限时抢购");
            rushBugTopTv.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.temp_round_pic),null,null,null);
            rushBugTipLeftTv.setText("限时抢购");
            rushBugTipRightTv.setText("限时抢购");
            rushBugBottomLeftIv.setImageResource(R.drawable.default_face);
            rushBugBottomRightIv.setImageResource(R.drawable.default_face);
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
        }
    }
}
