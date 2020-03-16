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
 * Summary: 首页 抢购列表  adapter 4
 */
public class RushBuyBottomAdapter extends CommonRecyclerViewAdapter {

    public RushBuyBottomAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getNormalViewType(int position) {

        return super.getNormalViewType(position);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_of_rush_buy_bottom, parent, false);

        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder {
        private TextView rushBugTitleTv;
        private TextView rushBugHintTv;
        private ImageView rushBugIv;

        public ViewHolder(View itemView) {
            super(itemView);
            rushBugTitleTv = itemView.findViewById(R.id.item_rush_bug_title_tv);
            rushBugHintTv = itemView.findViewById(R.id.item_rush_bug_hint_tv);
            rushBugIv = itemView.findViewById(R.id.item_rush_bug_bottom_iv);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            switch (mRealPosition){
                case 0:
                    itemView.setBackgroundResource(R.drawable.shape_bg_bottom_left_corner);
                    break;
                case 3:
                    itemView.setBackgroundResource(R.drawable.shape_bg_bottom_right_corner);
                    break;
            }
            rushBugTitleTv.setText("领券中心");
            rushBugHintTv.setText("提示语限时抢购");
            rushBugIv.setImageResource(R.drawable.default_face);
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
        }
    }
}
