package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.entity.PreciseStickEntity;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/24 15:02
 * Summary: 精选 粘性头
 */
@Deprecated
public class PreciseStickItemAdapter extends CommonRecyclerViewAdapter {
    public PreciseStickItemAdapter(Context context) {
        super(context);
    }

    private boolean isStickTop;//是否粘在顶部

    public void setStickType(boolean isStickTop) {
        this.isStickTop = isStickTop;

    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_stick_precise_list_layout, parent, false);
        LZLog.i("sss","createNormalViewHolder"+Thread.currentThread());
        return new StickViewHolder(inflate);
    }

    class StickViewHolder extends CommonRecyclerViewAdapter.CommonViewHolder {
        View normal_ll;
        View top_ll;
        TextView normal_tv;
        TextView normal_tip_tv;
        TextView top_tv;
        View top_indicator;
        int indicatorColor;
        int indicatorUnselectColor;
        int titleUnselectColor;

        public StickViewHolder(View itemView) {
            super(itemView);
            normal_ll = itemView.findViewById(R.id.stick_normal_ll);
            top_ll = itemView.findViewById(R.id.stick_in_top_ll);
            normal_tv = itemView.findViewById(R.id.select_normal_tv);
            normal_tip_tv = itemView.findViewById(R.id.select_tip_normal_tv);
            top_tv = itemView.findViewById(R.id.select_top_tv);
            top_indicator = itemView.findViewById(R.id.select_top_indicator);
            indicatorColor = mContext.getResources().getColor(R.color.color_FF005E);
            indicatorUnselectColor = mContext.getResources().getColor(R.color.color_555555);
            titleUnselectColor = mContext.getResources().getColor(R.color.color_333333);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            PreciseStickEntity stickEntity = (PreciseStickEntity) itemData;
            if (isStickTop) {
                top_ll.setVisibility(View.VISIBLE);
                normal_ll.setVisibility(View.GONE);
                top_tv.setText(stickEntity.getTitle());
                top_tv.setTextColor(stickEntity.isSelected() ?indicatorColor:titleUnselectColor);
                top_indicator.setBackgroundColor(stickEntity.isSelected() ? indicatorColor : indicatorUnselectColor);
            } else {
                top_ll.setVisibility(View.GONE);
                normal_ll.setVisibility(View.VISIBLE);
                normal_tv.setText(stickEntity.getTitle());
                normal_tv.setTextColor(stickEntity.isSelected() ?indicatorColor:titleUnselectColor);
                normal_tip_tv.setText(stickEntity.getTip());
                normal_tip_tv.setBackgroundResource(stickEntity.isSelected() ? R.drawable.shape_bg_ff005f_10_full_corner : 0);
            }
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            LZLog.i("onItemClicked","dd");
            for (int i = 0; i < getListData().size(); i++) {
                ((PreciseStickEntity) getListData().get(i)).setSelected(mRealPosition == i);
            }
            notifyDataSetChanged();
        }
    }
}
