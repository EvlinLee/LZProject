package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonBaseRecyclerViewAdapter;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewHolder;
import com.by.lizhiyoupin.app.io.bean.HomeIconBean;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;
import com.by.lizhiyoupin.app.main.weight.HomeToolsLayout;

import java.util.List;


/**
 * (Hangzhou) <br/>
 * Author: wzm<br/>
 * Date :  2018/6/25 19:25 </br>
 * Summary:首页icon 的Adapter
 */
public class HomeToolItemAdapter extends CommonBaseRecyclerViewAdapter<HomeIconBean> {

    private Context mContext;
    private List<HomeIconBean> mData;
    private String mToolsType= HomeToolsLayout.TOOLS_TYPE_NORMAL;
    public HomeToolItemAdapter(Context context, List<HomeIconBean> data) {
        super(context, data, R.layout.tab_fragment_setting_tool_item);
        mContext = context;
        mData = data;
    }
    public void setHomeToolsType(String type){
        this.mToolsType=type;
    }
    @Override
    public void convert(CommonRecyclerViewHolder holder, HomeIconBean item, int position) {
        final ImageView mIcon = holder.getView(R.id.icon_iv);
        final TextView textView = holder.getView(R.id.text_tv);
        textView.setText(item.getTitle());
        new GlideImageLoader(mContext,item.getIcon()).error(R.drawable.default_face).into(mIcon);
    }
    
    @Override
    protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
        super.onItemClick(v, holder, position);
      /*  if (mToolsType.equals(HomeToolsLayout.TOOLS_TYPE_OHTHER)){
            //根据 icon的id跳转二级类目
            Bundle bundle=new Bundle();
            HomeIconBean iconEntity=mData.get(position);
            bundle.putLong(CommonConst.KEY_SECOND_COMMODITY_ID,iconEntity.getKindId());
            CommonSchemeJump.showSecondSortListActivity(mContext,bundle);
        }else {
            //精选 icon跳转

        }*/
        jumpWeb(position);
       // notifyItemChanged(position); // 更新new标签
    }

    private void jumpWeb(int position) {
        try {
            final ISchemeManager scheme=(ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
            if (scheme != null) {
                HomeIconBean iconEntity=mData.get(position);
                int i=scheme.handleUrl(mContext, iconEntity.getLink());
                LZLog.i("icon jump callback i==", i + ",url==" + iconEntity.getLink());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
}
