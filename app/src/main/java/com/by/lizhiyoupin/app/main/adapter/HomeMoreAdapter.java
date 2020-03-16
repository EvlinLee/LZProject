package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.CommonCategoryBean;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;
import com.by.lizhiyoupin.app.message_box.MessageBox;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/27 17:07
 * Summary: 首页更多
 */
public class HomeMoreAdapter extends CommonRecyclerViewAdapter {
    private MessageBox messageBox;
    public HomeMoreAdapter(Context context,MessageBox messageBox) {
        super(context);
        this.messageBox=messageBox;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_of_home_more_grid, parent, false);
        return new MoreViewHolder(inflate);
    }

    class MoreViewHolder extends CommonViewHolder {
        TextView itemTv;
        ImageView iconIv;

        public MoreViewHolder(View itemView) {
            super(itemView);
            itemTv = itemView.findViewById(R.id.home_more_item_tv);
            iconIv = itemView.findViewById(R.id.icon_iv);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            CommonCategoryBean bean = (CommonCategoryBean) itemData;
            itemTv.setText(bean.getKindName());
            new GlideImageLoader(mContext, bean.getKindImg())
                    .error(R.drawable.empty_pic_list)
                    .placeholder(R.drawable.empty_pic_list)
                    .into(iconIv);
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            CommonCategoryBean bean = (CommonCategoryBean) getListData().get(mRealPosition);
            final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
            if (bean != null && scheme != null) {
                scheme.handleUrl(mContext,bean.getUrl());
            }
            messageBox.dismiss();
        }
    }
}
