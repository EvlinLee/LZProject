package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.io.bean.VipPowerBean;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/29 17:25
 * Summary:
 */
public class PowerDescAdapter extends CommonRecyclerViewAdapter {

    public PowerDescAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.tab_fragment_setting_tool_item,parent,false);
        inflate.findViewById(R.id.tool_item_ll).setBackgroundColor(Color.TRANSPARENT);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder{
         ImageView mIcon;
         TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
         mIcon = itemView.findViewById(R.id.icon_iv);
          textView = itemView.findViewById(R.id.text_tv);
            textView.setTextColor(Color.BLACK);

        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            VipPowerBean bean = (VipPowerBean) itemData;
            textView.setText(bean.getName());
            new GlideImageLoader(mContext,bean.getIcon()).error(R.drawable.empty_pic_list).into(mIcon);
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            jumpWeb(mRealPosition);
        }
        private void jumpWeb(int position) {
            try {
                final ISchemeManager scheme=(ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                if (scheme != null) {
                    VipPowerBean iconEntity= (VipPowerBean) mListData.get(position);
                    String name = iconEntity.getName();
                    String url = iconEntity.getUrl();
                    int i=scheme.handleUrl(mContext, WebUrlManager.getLitchiPowerUrl(name,url));
                    LZLog.i("icon jump callback i==", i + ",url==" + iconEntity.getUrl());
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
