package com.by.lizhiyoupin.app.user.adapter;

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
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.bean.SaveMoneyBean;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/5 13:56
 * Summary: 累计省钱adapter
 */
public class SaveMoneyAdapter extends CommonRecyclerViewAdapter {
    public SaveMoneyAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_save_money_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder {
        TextView itemShopTv;//店铺
        ImageView itemSaveMoneyIv;//图片
        TextView itemTitleTv;//商品名
        TextView itemCreateTimeTv;//创建时间
        TextView itemOrderNumberTv;//订单号
        TextView itemAmountTv;//实付金额 ￥22
        TextView itemPayTv;//实付金额
        private int corner;

        public ViewHolder(View itemView) {
            super(itemView);
            corner = DeviceUtil.dip2px(mContext, 8);
            itemShopTv = itemView.findViewById(R.id.item_save_money_shop_tv);
            itemSaveMoneyIv = itemView.findViewById(R.id.item_save_money_iv);
            itemTitleTv = itemView.findViewById(R.id.item_title_tv);
            itemCreateTimeTv = itemView.findViewById(R.id.item_create_time_tv);
            itemOrderNumberTv = itemView.findViewById(R.id.item_order_number_tv);
            itemAmountTv = itemView.findViewById(R.id.item_amount_tv);
            itemPayTv = itemView.findViewById(R.id.item_pay_tv);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            SaveMoneyBean.DetailsBean detailsBean = (SaveMoneyBean.DetailsBean) itemData;
            ViewUtil.setTextViewFormat(mContext, itemCreateTimeTv, R.string.save_money_item_create_time_text,
                    TimeUtils.transDateToDateString(detailsBean.getCreateTime(), 3));
            ViewUtil.setTextViewFormat(mContext, itemOrderNumberTv, R.string.save_money_item_order_number_text,
                    detailsBean.getOrderId());
            itemTitleTv.setText(detailsBean.getTitle());
            itemAmountTv.setText(StringUtils.getMoneyFormattedDouble(detailsBean.getPayment()));
            itemPayTv.setText(StringUtils.getMoneyFormattedDouble(detailsBean.getSave()));
            new GlideImageLoader(mContext, detailsBean.getImgages())
                    .error(R.drawable.empty_pic_list)
                    .placeholder(R.drawable.empty_pic_list)
                    .transform(new RoundedCornersTransformation(corner,0,RoundedCornersTransformation.CornerType.LEFT))
                    .into(itemSaveMoneyIv);
            switch (detailsBean.getPlatformType()) {
                case 0://全部
                    itemShopTv.setText(" ");
                    break;
                case 1:
                    //1淘宝 2京东 3拼多多
                    itemShopTv.setText(R.string.search_clip_platform_taobao);
                    ViewUtil.setDrawableOfTextView(itemShopTv, R.drawable.temp_detail_tao_big_pic, ViewUtil.DrawableDirection.LEFT);
                    break;
                case 2:
                    itemShopTv.setText(R.string.search_clip_platform_jingd);
                    ViewUtil.setDrawableOfTextView(itemShopTv, R.drawable.temp_detail_jingd_pic, ViewUtil.DrawableDirection.LEFT);
                    break;
                case 3:
                    itemShopTv.setText(R.string.search_clip_platform_pindd);
                    ViewUtil.setDrawableOfTextView(itemShopTv, R.drawable.temp_detail_pingdd_big_pic, ViewUtil.DrawableDirection.LEFT);
                    break;
            }
        }
    }
}
