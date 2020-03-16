package com.by.lizhiyoupin.app.user.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/3 10:47
 * Summary: push 橱窗商品列表 adapter
 */
public class PushAddGoodsAdapter extends CommonRecyclerViewAdapter {
    private PreciseListBean selectBean;
    private int currentIndex = 0;
    private int lastIndex = -1;
    private TextView mSubmitTv;


    public PushAddGoodsAdapter(Context context, TextView submitTv,
                               @Nullable PreciseListBean selectBean) {
        super(context);
        this.mSubmitTv = submitTv;
        this.selectBean = selectBean;
    }

    /**
     * 获取已选中bean
     *
     * @return 可能为null
     */
    public PreciseListBean getSelectBean() {
        return selectBean;
    }

    public void setSelectBean(PreciseListBean selectBean) {
        this.selectBean = selectBean;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_push_add_goods_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder implements View.OnClickListener {
        ImageView selectIv;
        ImageView mItemListTopIv;//顶部图片
        TextView mItemListTitleTv;//顶部title
        TextView mItemListBottomLeftTv;//优惠券 如20元券
        TextView mItemListBottomRightTv;//佣金比例 30%
        TextView mItemListPriceTv;//券后价格  345
        TextView mItemListVolumeTv;//月销售   10万+


        public ViewHolder(View itemView) {
            super(itemView);
            selectIv = itemView.findViewById(R.id.select_iv);
            mItemListTopIv = itemView.findViewById(R.id.item_list_top_iv);
            mItemListTitleTv = itemView.findViewById(R.id.item_list_title_tv);
            mItemListBottomLeftTv = itemView.findViewById(R.id.item_list_bottom_left_tv);
            mItemListBottomRightTv = itemView.findViewById(R.id.item_list_bottom_right_tv);
            mItemListPriceTv = itemView.findViewById(R.id.item_list_price_tv);
            mItemListVolumeTv = itemView.findViewById(R.id.item_list_volume_tv);
            selectIv.setOnClickListener(this);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            PreciseListBean bean = (PreciseListBean) itemData;
            new GlideImageLoader(mContext, bean.getPictUrl())
                    .placeholder(R.drawable.empty_pic_list)
                    .error(R.drawable.empty_pic_list)
                    .into(mItemListTopIv);
            mItemListTitleTv.setText(bean.getTitle());

            if (bean.getCouponAmount() == 0) {
                mItemListBottomLeftTv.setVisibility(View.GONE);
            } else {
                mItemListBottomLeftTv.setVisibility(View.VISIBLE);
            }
            ViewUtil.setTextViewFormat(mContext, mItemListBottomLeftTv,
                    R.string.product_coupon_price,
                    StringUtils.getFormattedDoubleOrInt(bean.getCouponAmount()));
            ViewUtil.setTextViewFormat(mContext, mItemListBottomRightTv,
                    R.string.product_commission_rate_price,
                    StringUtils.getFormattedDoubleOrInt(bean.getCommissionRate()), "%");
            mItemListPriceTv.setText(StringUtils.getFormattedDoubleOrInt(bean.getDiscountsPriceAfter()));
            ViewUtil.setTextViewFormat(mContext, mItemListVolumeTv, R.string.list_volume_text,
                    ViewUtil.getIVolume(bean.getVolume()));

            if (selectBean == null) {
                selectIv.setSelected(false);
                mSubmitTv.setEnabled(false);
            } else {
              boolean select=  selectBean.getItemId() == bean.getItemId()
                        && selectBean.getIcon() == bean.getIcon();
                selectIv.setSelected(select);
                if (select){
                    currentIndex=mRealPosition;
                }
            }


        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.select_iv) {
                lastIndex = currentIndex;
                currentIndex = mRealPosition;
                if (lastIndex == currentIndex) {
                    boolean selected = selectIv.isSelected();
                    if (selected) {
                        //当前是选中状态，则取消选中
                        selectBean = null;
                        mSubmitTv.setEnabled(false);
                    } else {
                        //未选中则，设为选中
                        selectBean = (PreciseListBean) getListData().get(mRealPosition);
                        mSubmitTv.setEnabled(true);
                    }
                    selectIv.setSelected(!selected);//新的
                } else {
                    if (selectIv.isSelected()){
                        selectBean=null;
                        mSubmitTv.setEnabled(false);
                        selectIv.setSelected(false);
                    }else {
                        selectBean = (PreciseListBean) getListData().get(mRealPosition);
                        selectIv.setSelected(true);
                        mSubmitTv.setEnabled(true);
                    }
                    notifyNormalItemChanged(lastIndex);//更新老的

                }

            }
        }
    }
}
