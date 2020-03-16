package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.PreciseBannerIconBean;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/28 11:21
 * Summary: 推荐模块 adapter
 */
public class PreciseSelectionRecommendAdapter extends CommonRecyclerViewAdapter {

    public PreciseSelectionRecommendAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_of_precise_selected_recommend, parent, false);

        return new ViewHolder(inflate);
    }
    class ViewHolder extends CommonViewHolder{
        private ImageView recommendIv;
        public ViewHolder(View itemView) {
            super(itemView);
            recommendIv = itemView.findViewById(R.id.item_recommend_iv);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            PreciseBannerIconBean data= (PreciseBannerIconBean) itemData;
            new GlideImageLoader(mContext,data.getImg())
                    .placeholder(R.drawable.empty_pic_list_h)
                    .error(R.drawable.empty_pic_list_h)
                    .transform(new RoundedCornersTransformation(DeviceUtil.dip2px(mContext, 8),0,RoundedCornersTransformation.CornerType.ALL))
                    .into(recommendIv);
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            final ISchemeManager schemeManager = (ISchemeManager) ComponentManager.getInstance()
                    .getManager(ISchemeManager.class.getName());
            if (schemeManager!=null){
                List<PreciseBannerIconBean> listData = getListData();
                PreciseBannerIconBean bean = listData.get(mRealPosition);
                if (bean!=null){
                    schemeManager.handleUrl(mContext,bean.getUrl());
                }
            }
        }
    }

}
