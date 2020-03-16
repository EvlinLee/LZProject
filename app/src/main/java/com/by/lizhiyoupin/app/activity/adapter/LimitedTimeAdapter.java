package com.by.lizhiyoupin.app.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/13 18:46
 * Summary: 限时秒杀
 */
public class LimitedTimeAdapter extends CommonRecyclerViewAdapter {

    public LimitedTimeAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_limit_time_layout, parent, false);
        return new ViewHolder(inflate);
    }
    class ViewHolder extends CommonViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
