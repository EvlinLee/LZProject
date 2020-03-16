package com.by.lizhiyoupin.app.main.findcircle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.io.bean.BusinessArticleBean;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/20 17:49
 * Summary: 滚动资讯
 */
public class BusinessArticleScrollAdapter extends CommonRecyclerViewAdapter {

    public BusinessArticleScrollAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemCount() {
        if (mListData == null) {
            return 0;
        } else if (mListData.size() < 2) {
            return mListData.size();
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public Object getItemData(final int position) {
        final int size = mListData == null ? 0 : getItemCount();
        return position < size && position >= 0 ? mListData.get(position % (mListData.size() == 0 ? 1 : mListData.size())) : null;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_business_article_scroll_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder {
        TextView articleTv;

        public ViewHolder(View itemView) {
            super(itemView);
            articleTv = itemView.findViewById(R.id.article_tv);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            BusinessArticleBean bean = (BusinessArticleBean) itemData;
            articleTv.setText(bean.getArticleTitle());
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            if (TimeUtils.isFrequentOperation()){
                return;
            }
            BusinessArticleBean bean2 = (BusinessArticleBean) getListData().get(mRealPosition % (mListData.size() == 0 ? 1 : mListData.size()));
            if (bean2 != null && bean2.getViewPermitLevelList() != null) {
                List<String> viewPermitLevelList = bean2.getViewPermitLevelList();
                if (viewPermitLevelList.contains("1")||viewPermitLevelList.contains(String.valueOf(ViewUtil.transFormLevel()))) {
                    CommonWebJump.showCommonWebActivity(mContext, WebUrlManager.getBusinessArticleUrl(bean2.getId()));
                } else {
                    MessageToast.showToast(mContext, "请提升会员级别!");
                }
            }else {
                MessageToast.showToast(mContext, "请提升会员级别!");
            }
        }
    }
}
