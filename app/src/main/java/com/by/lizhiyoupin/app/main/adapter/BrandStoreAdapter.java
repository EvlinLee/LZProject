package com.by.lizhiyoupin.app.main.adapter;

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
import com.by.lizhiyoupin.app.component_ui.weight.MoneyTextView;
import com.by.lizhiyoupin.app.io.bean.ShopGoodsBean;
import com.by.lizhiyoupin.app.user.adapter.CommissionRecyclerViewAdapter;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * data:2019/11/12
 * author:jyx
 * function:
 */
public class BrandStoreAdapter extends CommissionRecyclerViewAdapter {
    public BrandStoreAdapter(Context context) {
        super(context);
    }

    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View root = mInflater.inflate(R.layout.item_brandstore_details, parent, false);
        return new BrandStoreAdapter.ViewHolder(root);
    }

    class ViewHolder extends CommonViewHolder {
        private ImageView thumb_1;
        private TextView goods_result, goods_num, goods_quan, goods_red;
        private int corner;
        private MoneyTextView goods_price;

        public ViewHolder(View itemView) {
            super(itemView);
             corner = DeviceUtil.dip2px(mContext, 8);
            goods_red = itemView.findViewById(R.id.goods_red);
            thumb_1 = itemView.findViewById(R.id.thumb_1);//商品主图
            goods_result = itemView.findViewById(R.id.goods_result);//商品标题
            goods_num = itemView.findViewById(R.id.goods_num);//月销量
            goods_red = itemView.findViewById(R.id.goods_red);//返红包
            goods_quan = itemView.findViewById(R.id.goods_quan);//领劵金额
            goods_price = itemView.findViewById(R.id.goods_price);//劵后价
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            ShopGoodsBean bean = (ShopGoodsBean) itemData;
            new GlideImageLoader(mContext, bean.getPictUrl())
                    .transform(new RoundedCornersTransformation(corner, 0,
                            RoundedCornersTransformation.CornerType.TOP))
                    .error(R.drawable.empty_pic_list)
                    .placeholder(R.drawable.empty_pic_list)
                    .into(thumb_1);
            goods_result.setText(bean.getTitle());
            ViewUtil.setTextViewFormat(mContext, goods_num, R.string.list_volume_text, ViewUtil.getIVolume(bean.getVolume()));
            goods_red.setText("返红包" + StringUtils.getFormattedDoubleOrInt(bean.getCommissionMoney()) + "元");
            goods_quan.setText(StringUtils.getFormattedDoubleOrInt(bean.getCouponAmount()) + "元劵");
            goods_price.setText("¥" + StringUtils.getFormattedDoubleOrInt(bean.getDiscountsPriceAfter()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TimeUtils.isFrequentOperation()) {
                        return;
                    }
                    CommonSchemeJump.showPreciseDetailActivity(mContext, bean.getItemId(),
                            bean.getIcon());
                }
            });

        }
    }
}
