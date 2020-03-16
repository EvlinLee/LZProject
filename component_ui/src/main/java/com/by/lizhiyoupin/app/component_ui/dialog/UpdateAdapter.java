package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/5 19:26
 * Summary: 版本更新
 */
public class UpdateAdapter extends CommonRecyclerViewAdapter {

    public UpdateAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_update_view_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder{
        private TextView tipTv;
        public ViewHolder(View itemView) {
            super(itemView);
            tipTv = itemView.findViewById(R.id.tip_tv);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            tipTv.setText((String)itemData);
        }
    }
}
