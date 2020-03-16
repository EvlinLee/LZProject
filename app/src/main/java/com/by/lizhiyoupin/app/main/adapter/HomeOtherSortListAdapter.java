package com.by.lizhiyoupin.app.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.BaseOfDelegateAdapter;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.BaseOfViewHolder;
import com.by.lizhiyoupin.app.io.bean.RecordsBean;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/29 12:02
 * Summary: 本地服务器数据
 */
public class HomeOtherSortListAdapter  extends BaseOfDelegateAdapter {
    public static final int TYPE_SINGLE = 0;
    public static final int TYPE_OTHER = 1;
    public int type = TYPE_OTHER;
    private LayoutHelper mLayoutHelper;
    public HomeOtherSortListAdapter(Context context, LayoutHelper layoutHelper, int normalViewType) {
        super(context, layoutHelper, normalViewType);
        this.mLayoutHelper=layoutHelper;
    }



    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    public void setType(int itemType) {
        this.type = itemType;
    }

    public int getType() {
        return type;
    }

    @Override
    public int getNormalViewType(int position) {
        return type;
    }

    @Override
    protected BaseOfViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate;
        /*if (viewType == TYPE_OTHER) {
            inflate = mInflater.inflate(R.layout.item_precise_selection_list, parent, false);
        } else {
            inflate = mInflater.inflate(R.layout.item_precise_single_list, parent, false);
        }*/
        inflate = mInflater.inflate(R.layout.item_sleect_single_double_list, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends BaseOfViewHolder {
        ImageView mItemListTopIv,mItemListTopIv2;//顶部图片
        TextView mItemListTitleTv,mItemListTitleTv2;//顶部title
        TextView mItemListBottomLeftTv,mItemListBottomLeftTv2;//优惠券 如20元券
        TextView mItemListBottomRightTv,mItemListBottomRightTv2;//返红包20元
        TextView mItemListPriceTv,mItemListPriceTv2;//券后价格  345
        TextView mItemListVolumeTv,mItemListVolumeTv2;//月销售   10万+
        TextView mItemListNameTv,mItemListNameTv2;//商户名
        ImageView mItemListNameIconIv,mItemListNameIconIv2;//icon
        private final View mDoubleCl;
        private final View mSingleCl;

        public ViewHolder(View itemView) {
            super(itemView);
            mDoubleCl = itemView.findViewById(R.id.double_cl);
            mSingleCl = itemView.findViewById(R.id.single_cl);
            mSingleCl.setVisibility(View.GONE);
            //双列
            mItemListTopIv2 = mDoubleCl.findViewById(R.id.item_list_top_iv);
            mItemListTitleTv2 = mDoubleCl.findViewById(R.id.item_list_title_tv);
            mItemListBottomLeftTv2 = mDoubleCl.findViewById(R.id.item_list_bottom_left_tv);
            mItemListBottomRightTv2 = mDoubleCl.findViewById(R.id.item_list_bottom_right_tv);
            mItemListPriceTv2 = mDoubleCl.findViewById(R.id.item_list_price_tv);
            mItemListVolumeTv2 = mDoubleCl.findViewById(R.id.item_list_volume_tv);
            mItemListNameTv2 = mDoubleCl.findViewById(R.id.item_list_name_tv);
            mItemListNameIconIv2 = mDoubleCl.findViewById(R.id.item_list_name_icon_iv);
            //单列
            mItemListTopIv = mSingleCl.findViewById(R.id.item_list_top_iv);
            mItemListTitleTv = mSingleCl.findViewById(R.id.item_list_title_tv);
            mItemListBottomLeftTv = mSingleCl.findViewById(R.id.item_list_bottom_left_tv);
            mItemListBottomRightTv = mSingleCl.findViewById(R.id.item_list_bottom_right_tv);
            mItemListPriceTv = mSingleCl.findViewById(R.id.item_list_price_tv);
            mItemListVolumeTv = mSingleCl.findViewById(R.id.item_list_volume_tv);
            mItemListNameTv = mSingleCl.findViewById(R.id.item_list_name_tv);
            mItemListNameIconIv = mSingleCl.findViewById(R.id.item_list_name_icon_iv);
        }

        @Override
        public void bindData(Object itemData,int viewType) {
            super.bindData(itemData,viewType);
            RecordsBean bean = (RecordsBean) itemData;
            int drawRes=ViewUtil.getIconImg(bean.getIcon());

            if (type == TYPE_OTHER) {//双列
                mDoubleCl.setVisibility(View.VISIBLE);
                mSingleCl.setVisibility(View.GONE);
                new GlideImageLoader(mContext, bean.getMainImg())
                        .error(R.drawable.empty_pic_list)
                        .transform(new RoundedCornersTransformation(DeviceUtil.dip2px(mContext, 8),0,RoundedCornersTransformation.CornerType.TOP))
                        .into(mItemListTopIv2);
                mItemListTitleTv2.setText(bean.getName());
                if (bean.getCouponAmount()==0){
                    mItemListBottomLeftTv2.setVisibility(View.GONE);
                }else {
                    mItemListBottomLeftTv2.setVisibility(View.VISIBLE);
                }
                ViewUtil.setTextViewFormat(mContext, mItemListBottomLeftTv2, R.string.product_coupon_price,StringUtils.getFormattedDoubleOrInt(bean.getCouponAmount()));
                ViewUtil.setTextViewFormat(mContext, mItemListBottomRightTv2, R.string.product_commission_price, StringUtils.getFormattedDoubleOrInt(bean.getCommissionAmount()));
                mItemListPriceTv2.setText(StringUtils.getFormattedDoubleOrInt(bean.getRealPrice()));
                ViewUtil.setTextViewFormat(mContext, mItemListVolumeTv2, R.string.list_volume_text, ViewUtil.getIVolume(bean.getMonthSales()));
                mItemListNameTv2.setText(bean.getShopName());
                mItemListNameIconIv2.setImageResource(drawRes);
            } else {
                mDoubleCl.setVisibility(View.GONE);
                mSingleCl.setVisibility(View.VISIBLE);
                new GlideImageLoader(mContext, bean.getMainImg())
                        .error(R.drawable.empty_pic_list)
                        .into(mItemListTopIv);
                mItemListTitleTv.setText(bean.getName());
                if (bean.getCouponAmount()==0){
                    mItemListBottomLeftTv.setVisibility(View.GONE);
                }else {
                    mItemListBottomLeftTv.setVisibility(View.VISIBLE);
                }
                ViewUtil.setTextViewFormat(mContext, mItemListBottomLeftTv, R.string.product_coupon_price, StringUtils.getFormattedDoubleOrInt(bean.getCouponAmount()));
                ViewUtil.setTextViewFormat(mContext, mItemListBottomRightTv, R.string.product_commission_price, StringUtils.getFormattedDoubleOrInt(bean.getCommissionAmount()));
                mItemListPriceTv.setText(StringUtils.getFormattedDoubleOrInt(bean.getRealPrice()));
                ViewUtil.setTextViewFormat(mContext, mItemListVolumeTv, R.string.list_volume_text, ViewUtil.getIVolume(bean.getMonthSales()));
                mItemListNameTv.setText(bean.getShopName());
                mItemListNameIconIv.setImageResource(drawRes);
            }
        }


        @Override
        public void onItemClicked() {
            super.onItemClicked();
            if (TimeUtils.isFrequentOperation()){
                return;
            }
            RecordsBean bean = (RecordsBean) getListData().get(mRealPosition);

            CommonSchemeJump.showPreciseDetailActivity(mContext, bean.getCommodityId(), bean.getIcon());
        }
    }

}
