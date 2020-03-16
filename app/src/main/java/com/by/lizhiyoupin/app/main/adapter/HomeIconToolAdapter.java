package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.HomeIconBean;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;

import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/30 15:24
 * Summary: 首页icon 金刚区adapter
 */
public class HomeIconToolAdapter extends CommonRecyclerViewAdapter {
    private Context mContext;
    private int width;

    public HomeIconToolAdapter(Context context) {
        super(context);
        mContext = context;
        width = DeviceUtil.getScreenWidth(mContext) / 5;
    }


    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        final View convertView = mInflater.inflate(R.layout.tab_fragment_icon_tool_item, parent, false);
        return new ViewHolder(convertView);
    }

    class ViewHolder extends CommonViewHolder implements View.OnClickListener {
        ImageView mIcon;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.icon_iv);
            textView = itemView.findViewById(R.id.text_tv);
            View tool = itemView.findViewById(R.id.tool_item_ll);
            tool.setOnClickListener(this);

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) tool.getLayoutParams();
            layoutParams.width = width;
            tool.setLayoutParams(layoutParams);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            HomeIconBean item = (HomeIconBean) itemData;
            textView.setText(item.getTitle());
            new GlideImageLoader(mContext, item.getIcon()).error(R.drawable.default_face).into(mIcon);
        }

        @Override
        public void onClick(View v) {
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            switch (v.getId()) {
                case R.id.tool_item_ll:
                    jumpWeb(mRealPosition);
                    break;
                default:
                    break;
            }
        }

        private void jumpWeb(int position) {
            try {
                final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                HomeIconBean iconEntity = (HomeIconBean) getListData().get(position);
                if (scheme != null && iconEntity != null) {
                    scheme.handleUrl(mContext, iconEntity.getLink());
                    LZLog.i("icon jump callback i==", "url==" + iconEntity.getLink());
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
