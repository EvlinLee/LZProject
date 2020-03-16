package com.by.lizhiyoupin.app.main.findcircle.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonBaseRecyclerViewAdapter;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewHolder;
import com.by.lizhiyoupin.app.io.bean.BusinessIconBean;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/20 15:56
 * Summary:
 */
public class ToolsItemAdapter extends CommonBaseRecyclerViewAdapter<BusinessIconBean> {


    private Context mContext;
    private List<BusinessIconBean> mData;

    public ToolsItemAdapter(Context context, List<BusinessIconBean> data) {
        super(context, data, R.layout.item_tools_adapter_layout);
        mContext = context;
        mData = data;
    }

    @Override
    public void convert(CommonRecyclerViewHolder holder, BusinessIconBean item, int position) {
        final ImageView mIcon = holder.getView(R.id.icon_iv);
        final TextView textView = holder.getView(R.id.text_tv);
        textView.setText(item.getKindName());
        new GlideImageLoader(mContext, item.getBusinessSchoolKindImg()).error(R.drawable.empty_pic_list).into(mIcon);
    }

    @Override
    protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
        super.onItemClick(v, holder, position);
        if (TimeUtils.isFrequentOperation()){
            return;
        }
        BusinessIconBean iconEntity = mData.get(position);
        if (iconEntity == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.KEY_FIND_CIRCLE_FIRST_TAB_ID, iconEntity.getId());
        bundle.putString(CommonConst.KEY_ACTIONBAR_TITLE, iconEntity.getKindName());
        CommonSchemeJump.showBusinessSecondListActivity(mContext, bundle);
    }


    private void jumpWeb(int position) {
        try {
            final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
            if (scheme != null) {
                BusinessIconBean iconEntity = mData.get(position);

                int i = scheme.handleUrl(mContext, iconEntity.getUrl());
                LZLog.i("icon jump callback i==", i + ",url==" + iconEntity.getUrl());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}
