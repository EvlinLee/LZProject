package com.by.lizhiyoupin.app.user.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonBaseRecyclerViewAdapter;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewHolder;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.io.bean.VipPowerBean;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;

import java.net.URLEncoder;
import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/5 17:50
 * Summary:
 */
public class VipToolItemAdapter extends CommonBaseRecyclerViewAdapter<VipPowerBean> {

    private Context mContext;
    private List<VipPowerBean> mData;

    public VipToolItemAdapter(Context context, List<VipPowerBean> data) {
        super(context, data, R.layout.tab_fragment_setting_tool_item);
        mContext = context;
        mData = data;
    }

    @Override
    public void convert(CommonRecyclerViewHolder holder, VipPowerBean item, int position) {
        final ImageView mIcon = holder.getView(R.id.icon_iv);
        final TextView textView = holder.getView(R.id.text_tv);
        textView.setText(item.getName());
        if (item.isNativeDraw()) {
            holder.itemView.setBackgroundColor(Color.WHITE);
            mIcon.setImageResource(item.getDrawRes());
        } else {
            new GlideImageLoader(mContext, item.getIcon()).error(R.drawable.default_face).into(mIcon);
        }
    }

    @Override
    protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
        super.onItemClick(v, holder, position);
        if (getData() != null) {
            VipPowerBean bean = getData().get(position);
            if (bean != null && !bean.isNativeDraw()) {
                jumpWeb(position);
            }
        }

    }

    private void jumpWeb(int position) {
        try {
            final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
            if (scheme != null) {
                VipPowerBean iconEntity = mData.get(position);
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
