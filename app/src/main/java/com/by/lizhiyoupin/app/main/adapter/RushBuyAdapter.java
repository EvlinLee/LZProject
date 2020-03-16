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
 * @date :  2019/10/16 16:42
 * Summary:
 */
public class RushBuyAdapter extends CommonRecyclerViewAdapter {
    private int mType = 0; //根据样式 区分加载错误图

    public RushBuyAdapter(Context context) {
        super(context);
    }

    public void setCountType(int countType) {
        this.mType = countType;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_of_rush_buy_layout, parent, false);

        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder {
        private ImageView rushBugIv;
        private int radius;

        public ViewHolder(View itemView) {
            super(itemView);
            rushBugIv = itemView.findViewById(R.id.rush_bug_img_iv);
            radius = DeviceUtil.dip2px(mContext, 8);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            PreciseBannerIconBean bean = (PreciseBannerIconBean) itemData;
            GlideImageLoader imageLoader = new GlideImageLoader(mContext, bean.getImg())
                    .placeholder(R.drawable.empty_pic_list_v)
                    .error(R.drawable.empty_pic_list_v);
            if (mType == 4) {
                //领券
                if (mRealPosition == 0) {
                    imageLoader.transform(new RoundedCornersTransformation(radius,
                            0, RoundedCornersTransformation.CornerType.BOTTOM_LEFT))
                            .into(rushBugIv);
                } else if (mRealPosition == 3) {
                    imageLoader.transform(new RoundedCornersTransformation(radius,
                            0, RoundedCornersTransformation.CornerType.BOTTOM_RIGHT))
                            .into(rushBugIv);
                } else {
                    imageLoader.into(rushBugIv);
                }
            } else {
                //抢购
                if (mRealPosition == 0) {
                    imageLoader.transform(new RoundedCornersTransformation(radius,
                            0, RoundedCornersTransformation.CornerType.TOP_LEFT))
                            .into(rushBugIv);
                } else if (mRealPosition == 1) {
                    imageLoader.transform(new RoundedCornersTransformation(radius,
                            0, RoundedCornersTransformation.CornerType.TOP_RIGHT))
                            .into(rushBugIv);
                } else {
                    imageLoader.into(rushBugIv);
                }
            }
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            List<PreciseBannerIconBean> listData = getListData();
            PreciseBannerIconBean preciseBannerIconBean = listData.get(mRealPosition);
            final ISchemeManager schemeManager = (ISchemeManager) ComponentManager.getInstance()
                    .getManager(ISchemeManager.class.getName());
            if (schemeManager != null) {
                if (preciseBannerIconBean != null) {
                    schemeManager.handleUrl(mContext, preciseBannerIconBean.getUrl());
                }
            }
        }
    }
}
