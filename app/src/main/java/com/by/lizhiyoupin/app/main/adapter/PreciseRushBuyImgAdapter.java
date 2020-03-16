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
import com.by.lizhiyoupin.app.main.holder.PreciseNewHeaderHolder;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/2 17:59
 * Summary:  top抢购 bottom领券
 */
public class PreciseRushBuyImgAdapter extends BaseOfDelegateAdapter {

    public PreciseRushBuyImgAdapter(Context context, LayoutHelper layoutHelper, int normalViewType) {
        super(context, layoutHelper, normalViewType);
    }


    @Override
    protected BaseOfViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_of_rush_buy_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends BaseOfViewHolder {
        int radius;

        public ViewHolder(View itemView) {
            super(itemView);
            radius = DeviceUtil.dip2px(mContext, 8);
        }

        @Override
        public void bindData(Object itemData, int viewType) {
            super.bindData(itemData, viewType);
            PreciseBannerIconBean bean = (PreciseBannerIconBean) itemData;
            GlideImageLoader imageLoader = new GlideImageLoader(mContext, bean.getImg())
                    .placeholder(R.drawable.empty_pic_list_v)
                    .error(R.drawable.empty_pic_list_v);
            if (viewType == PreciseNewHeaderHolder.ITEM_TYPE_OF_RUSH_BUY_BOTTOM) {
                //下面4个一行领券
                if (mRealPosition == 0) {
                    imageLoader.transform(new RoundedCornersTransformation(radius,
                            0, RoundedCornersTransformation.CornerType.BOTTOM_LEFT))
                            .into(getView(R.id.rush_bug_img_iv));
                } else if (mRealPosition == 3) {
                    imageLoader.transform(new RoundedCornersTransformation(radius,
                            0, RoundedCornersTransformation.CornerType.BOTTOM_RIGHT))
                            .into(getView(R.id.rush_bug_img_iv));
                } else {
                    imageLoader.into(getView(R.id.rush_bug_img_iv));
                }

            } else if (viewType == PreciseNewHeaderHolder.ITEM_TYPE_OF_RUSH_BUY_TOP){
                //top2行2列抢购
                if (mRealPosition == 0) {
                    imageLoader.transform(new RoundedCornersTransformation(radius,
                            0, RoundedCornersTransformation.CornerType.TOP_LEFT))
                            .into(getView(R.id.rush_bug_img_iv));
                } else if (mRealPosition == 1) {
                    imageLoader.transform(new RoundedCornersTransformation(radius,
                            0, RoundedCornersTransformation.CornerType.TOP_RIGHT))
                            .into(getView(R.id.rush_bug_img_iv));
                } else {
                    imageLoader
                            .into(getView(R.id.rush_bug_img_iv));
                }
            }
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            List<PreciseBannerIconBean> listData = getListData();
            final ISchemeManager schemeManager = (ISchemeManager) ComponentManager.getInstance()
                    .getManager(ISchemeManager.class.getName());
            if (schemeManager != null && listData != null) {
                PreciseBannerIconBean preciseBannerIconBean = listData.get(mRealPosition);
                if (preciseBannerIconBean != null) {
                    schemeManager.handleUrl(mContext, preciseBannerIconBean.getUrl());
                }
            }
        }
    }
}
