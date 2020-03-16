package com.by.lizhiyoupin.app.user.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.FootprintBean;
import com.by.lizhiyoupin.app.io.bean.FootprintParentBean;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/18 10:06
 * Summary: 足迹 adapter
 */
public class FootprintParentAdapter extends CommonRecyclerViewAdapter {

    private boolean  isEditMode;
    public FootprintParentAdapter(Context context) {
        super(context);
    }

    public boolean isEdiTMode() {
        return isEditMode;
    }

    public void setEdiTMode(boolean editMode) {
        isEditMode = editMode;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_foot_print_parent_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder {
        RecyclerView mRecyclerView;
        TextView mHeadTimeTv;
        private final FootChildAdapter mFootChildAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            mHeadTimeTv = itemView.findViewById(R.id.head_time_tv);
            mRecyclerView = itemView.findViewById(R.id.child_RecyclerView);
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));
            mFootChildAdapter = new FootChildAdapter(mContext);
            mRecyclerView.setAdapter(mFootChildAdapter);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            FootprintParentBean parentBean= (FootprintParentBean) itemData;
            List<FootprintBean> voList = parentBean.getVoList();
            mHeadTimeTv.setText(parentBean.getDateStr());
            mFootChildAdapter.setListData(voList);
            mFootChildAdapter.setEdiTMode(isEdiTMode());
            mFootChildAdapter.notifyDataSetChanged();
        }
    }
}
