package com.by.lizhiyoupin.app.detail.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.ShoppingCartListBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.stack.ActivityStack;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/5 13:56
 * Summary: 购物车adapter
 */
public class ShoppingCartAdapter extends CommonRecyclerViewAdapter {
    private LoadMoreHelperRx<ShoppingCartListBean, Integer> mHelperRx;

    public ShoppingCartAdapter(Context context) {
        super(context);
    }

    public void setHelperRx(LoadMoreHelperRx<ShoppingCartListBean, Integer> mHelperRx) {
        this.mHelperRx = mHelperRx;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_shopping_cart_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder implements View.OnClickListener {
        ImageView mItemListTopIv;//顶部图片
        TextView mItemListTitleTv;//顶部title
        TextView mItemListBottomLeftTv;//优惠券 如20元券
        TextView mItemListBottomRightTv;//返红包20元
        TextView mItemListPriceTv;//券后价格  345
        TextView mItemListVolumeTv;//月销售   10万+
        TextView mItemListNameTv;//商户名
        ImageView mItemListNameIconIv;//icon
        TextView mShoppingBuyTv; //立即购买
        TextView mShoppingDeleteTv; //删除
        TextView expiredTv; //已过期
        private View mContentCl;
        private int corner;

        public ViewHolder(View itemView) {
            super(itemView);
            corner = DeviceUtil.dip2px(mContext, 8);
            mContentCl = itemView.findViewById(R.id.content_cl);
            expiredTv = itemView.findViewById(R.id.expired_tv);
            mItemListTopIv = itemView.findViewById(R.id.item_list_top_iv);
            mItemListTitleTv = itemView.findViewById(R.id.item_list_title_tv);
            mItemListBottomLeftTv = itemView.findViewById(R.id.item_list_bottom_left_tv);
            mItemListBottomRightTv = itemView.findViewById(R.id.item_list_bottom_right_tv);
            mItemListPriceTv = itemView.findViewById(R.id.item_list_price_tv);
            mItemListVolumeTv = itemView.findViewById(R.id.item_list_volume_tv);
            mItemListNameTv = itemView.findViewById(R.id.item_list_name_tv);
            mItemListNameIconIv = itemView.findViewById(R.id.item_list_name_icon_iv);
            mShoppingBuyTv = itemView.findViewById(R.id.item_shopping_buy_tv);
            mShoppingDeleteTv = itemView.findViewById(R.id.item_shopping_delete_tv);
            mShoppingBuyTv.setOnClickListener(this);
            mShoppingDeleteTv.setOnClickListener(this);
            mContentCl.setOnClickListener(this);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            ShoppingCartListBean bean = (ShoppingCartListBean) itemData;

            new GlideImageLoader(mContext, bean.getCommodityImg())
                    .error(R.drawable.empty_pic_list)
                    .transform(new RoundedCornersTransformation(corner,
                            0, RoundedCornersTransformation.CornerType.ALL))
                    .into(mItemListTopIv);

            mItemListTitleTv.setText(bean.getCommodityName());
            if (bean.getCouponAmount() == 0) {
                mItemListBottomLeftTv.setVisibility(View.GONE);
            } else {
                mItemListBottomLeftTv.setVisibility(View.VISIBLE);
                ViewUtil.setTextViewFormat(mContext, mItemListBottomLeftTv,
                        R.string.product_coupon_price, StringUtils.getFormattedDoubleOrInt(bean.getCouponAmount()));
            }
            ViewUtil.setTextViewFormat(mContext, mItemListBottomRightTv, R.string.product_commission_price,
                    StringUtils.getFormattedDoubleOrInt(bean.getCommissionAmount()));
            mItemListPriceTv.setText(StringUtils.getFormattedDoubleOrInt(bean.getRealPrice()));
            ViewUtil.setTextViewFormat(mContext, mItemListVolumeTv, R.string.list_volume_text, StringUtils.getIVolume(bean.getMonthSales()));
            mItemListNameTv.setText(bean.getShopName());

            mItemListNameIconIv.setImageResource(ViewUtil.getIconImg(bean.getCommoditySource()));
            if (bean.getCommodityValid() == 1) {
                //商品有效，不显示 已失效 提示
                expiredTv.setVisibility(View.GONE);
            } else {
                expiredTv.setVisibility(View.VISIBLE);
            }
        }


        @Override
        public void onClick(View v) {
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            switch (v.getId()) {
                case R.id.item_shopping_buy_tv:
                case R.id.content_cl:
                    //立即购买
                    ShoppingCartListBean bean = (ShoppingCartListBean) getListData().get(mRealPosition);
                    if (1 == bean.getCommodityValid()) {
                        //有效 才可购买
                        CommonSchemeJump.showPreciseDetailActivity(mContext, bean.getCommodityId(), bean.getCommoditySource());
                    }

                    break;
                case R.id.item_shopping_delete_tv:
                    //删除
                    ShoppingCartListBean cartListBean = (ShoppingCartListBean) mListData.get(mRealPosition);
                    ApiService.getShoppingApi().requestDeleteShoppingCartInfo(cartListBean.getId())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new DefaultRx2Subscribe<BaseBean<Object>>() {
                                @Override
                                public void onNext(BaseBean<Object> baseBean) {
                                    super.onNext(baseBean);
                                    if (!ActivityStack.isActivityDestoryed(mContext)) {
                                        MessageToast.showToast(mContext, "删除成功");
                                    }
                                }
                            });
                    mListData.remove(cartListBean);
                    if (mHelperRx != null) {
                        //两边数据都删除，然後刷新
                        mHelperRx.getData().remove(cartListBean);
                        mHelperRx.notifyDataSetChange();
                    } else {
                        notifyDataSetChanged();
                    }
                    break;
            }
        }
    }
}
