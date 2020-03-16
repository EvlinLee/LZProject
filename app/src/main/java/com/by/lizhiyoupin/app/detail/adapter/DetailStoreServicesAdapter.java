package com.by.lizhiyoupin.app.detail.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.entity.StoreDescbean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/9 11:50
 * Summary: 店铺商品评级
 */
public class DetailStoreServicesAdapter extends CommonRecyclerViewAdapter {

    public DetailStoreServicesAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_detail_current_store_services, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder{
        TextView descTv;
        TextView scoreTv;
        TextView levelTv;

        public ViewHolder(View itemView) {
            super(itemView);
            descTv = itemView.findViewById(R.id.detail_current_store_desc_tv);
            scoreTv = itemView.findViewById(R.id.detail_current_store_score_tv);
            levelTv = itemView.findViewById(R.id.detail_current_store_level_tv);

        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            StoreDescbean  storeDescbean= (StoreDescbean) itemData;
            descTv.setText(storeDescbean.getDesc());
            scoreTv.setText(storeDescbean.getScore());
            levelTv.setText(storeDescbean.getLevel());
            if ("高".equals(storeDescbean.getLevel())){
                levelTv.setTextColor(mContext.getResources().getColor(R.color.color_EF1818));
            }else {
                levelTv.setTextColor(mContext.getResources().getColor(R.color.color_999999));

            }
        }

    }
}
