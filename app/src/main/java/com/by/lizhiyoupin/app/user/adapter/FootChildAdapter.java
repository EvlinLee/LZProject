package com.by.lizhiyoupin.app.user.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.FootprintBean;
import com.by.lizhiyoupin.app.user.activity.FootprintActivity;
import com.by.lizhiyoupin.app.utils.ViewUtil;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/18 10:06
 * Summary: 足迹 adapter
 */
public class FootChildAdapter extends CommonRecyclerViewAdapter {
    private boolean isEditMode;
    private int item;
    public FootChildAdapter(Context context) {
        super(context);
        item= (DeviceUtil.getScreenWidth(context)-DeviceUtil.dip2px(context,3)*2)/3;
    }


    public void setEdiTMode(boolean editMode) {
        isEditMode = editMode;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_foot_print_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder {
        ImageView mFootIv;
        ImageView mFootCheckIv;
        TextView mAfterCouponTv;
        TextView mRedCouponTv;
        TextView mExpiredTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mFootIv = itemView.findViewById(R.id.foot_iv);
            mAfterCouponTv = itemView.findViewById(R.id.after_coupon_tv);
            mRedCouponTv = itemView.findViewById(R.id.item_red_coupon_tv);
            mExpiredTv = itemView.findViewById(R.id.expired_tv);
            mFootCheckIv = itemView.findViewById(R.id.item_foot_check_iv);
            ViewGroup.LayoutParams layoutParams = mFootIv.getLayoutParams();
            layoutParams.width=item;
            layoutParams.height=item;
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            FootprintBean bean = (FootprintBean) itemData;
            if (1 == bean.getCommodityValid()) {
                mExpiredTv.setVisibility(View.GONE);
            } else {
                mExpiredTv.setVisibility(View.VISIBLE);
            }
            new GlideImageLoader(mContext, bean.getCommodityImg()).error(R.drawable.empty_pic_list)
                    .placeholder(R.drawable.empty_pic_list)
                    .into(mFootIv);
            ViewUtil.setMoneyText(mContext, mAfterCouponTv, "¥" + bean.getRealPrice(),
                    R.style.product_money14_111111, R.style.product_money18_111111);

            ViewUtil.setTextViewFormat(mContext, mRedCouponTv, R.string.foot_print_red_coupon_text, bean.getCommissionAmount());
            if (!isEditMode) {
                mFootCheckIv.setVisibility(View.GONE);
            } else {
                mFootCheckIv.setVisibility(View.VISIBLE);
                mFootCheckIv.setSelected(bean.isSelected());
            }

        }


        @Override
        public void onItemClicked() {
            super.onItemClicked();
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            FootprintBean footprintBean = (FootprintBean) getListData().get(mRealPosition);
            if (isEditMode) {
                //编辑模式
                if (footprintBean != null ) {
                    if (footprintBean.isSelected()){
                        footprintBean.setSelected(false);
                        mFootCheckIv.setSelected(footprintBean.isSelected());
                        FootprintActivity.sSelectList.remove(footprintBean);
                    }else {
                        footprintBean.setSelected(true);
                        mFootCheckIv.setSelected(footprintBean.isSelected());
                        FootprintActivity.sSelectList.add(footprintBean);
                    }
                }
            }else {
                if (1 == footprintBean.getCommodityValid()) {
                    //有效 才可跳转详情
                    //不知道本地还是三方，都按三方来，去查三方服务
                    CommonSchemeJump.showPreciseDetailActivity(mContext, footprintBean.getCommodityId(), footprintBean.getPlatformType());
                }
            }

        }

    }
}
