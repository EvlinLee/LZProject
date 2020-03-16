package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.LayoutHelper;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.BaseOfDelegateAdapter;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.BaseOfViewHolder;
import com.by.lizhiyoupin.app.io.bean.PreciseBannerIconBean;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/2 17:59
 * Summary: 推荐多张图
 */
public class PreciseRecommendImgAdapter extends BaseOfDelegateAdapter {


    public PreciseRecommendImgAdapter(Context context, LayoutHelper layoutHelper, int normalViewType) {
        super(context, layoutHelper, normalViewType);
    }

    @Override
    protected BaseOfViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_of_precise_selected_recommend, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends BaseOfViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData,int viewType) {
            super.bindData(itemData, viewType);
            PreciseBannerIconBean data = (PreciseBannerIconBean) itemData;
            new GlideImageLoader(mContext, data.getImg())
                    .placeholder(R.drawable.empty_pic_list_h)
                    .error(R.drawable.empty_pic_list_h)
                    .transform(new RoundedCornersTransformation(DeviceUtil.dip2px(mContext, 8),0,RoundedCornersTransformation.CornerType.ALL))
                    .into(getView(R.id.item_recommend_iv));
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            final ISchemeManager schemeManager = (ISchemeManager) ComponentManager.getInstance()
                    .getManager(ISchemeManager.class.getName());
            List<PreciseBannerIconBean> listData = getListData();
            if (schemeManager != null && listData != null) {
                PreciseBannerIconBean bean = listData.get(mRealPosition);
                if (bean != null) {
                    schemeManager.handleUrl(mContext, bean.getUrl());
                }
            }
        }
    }
}
