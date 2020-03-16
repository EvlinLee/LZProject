package com.by.lizhiyoupin.app.search.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/19 10:51
 * Summary: 搜索历史adapter
 */
public class SearchHistoryAdapter extends CommonRecyclerViewAdapter {
    public static final String TAG = SearchHistoryAdapter.class.getSimpleName();
    private boolean isBusiness = false;

    public void setIsBusiness(boolean type) {
        isBusiness = type;
    }

    public SearchHistoryAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_flex_box_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder {
        TextView mFlexTv;
        ImageView mFlagIv;

        public ViewHolder(View itemView) {
            super(itemView);
            mFlexTv = itemView.findViewById(R.id.item_flex_box_tv);
            mFlagIv = itemView.findViewById(R.id.flag_iv);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            //mFlagIv.setImageResource(R.drawable.new_flex_box_pic);
            mFlexTv.setText((String) itemData);
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            String bean = (String) getListData().get(mRealPosition);

            // 跳转到搜索结果页去搜索
            if (isBusiness) {
                //商学院 搜索
                CommonSchemeJump.showBusinessSearchResultActivity(mContext, bean);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString(CommonConst.KEY_SEARCH_TITLE, bean);
                CommonSchemeJump.showActivity(mContext, "/app/SearchResultActivity", bundle);
            }

        }
    }
}
