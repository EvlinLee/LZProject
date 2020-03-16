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
import com.by.lizhiyoupin.app.component_ui.weight.ExpandTextView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.PushGoodsBean;
import com.by.lizhiyoupin.app.user.SettingRequestManager;
import com.by.lizhiyoupin.app.user.adapter.CommissionRecyclerViewAdapter;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * data:2019/11/9
 * author:jyx
 * function:
 */
public class InteractiveNewsAdapter extends CommissionRecyclerViewAdapter {
      public InteractiveNewsAdapter(Context context) {
        super(context);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View root = mInflater.inflate(R.layout.item_interactivenews_details, parent, false);
        return new InteractiveNewsAdapter.ViewHolder(root);
    }

    class ViewHolder extends CommonViewHolder {

        TextView explosions_content;//标题
        ImageView explosions_img, img_status;//图片
        TextView explosions_time;//创建时间
        TextView interactive_price;//价格
        TextView interactive_vonum;//月销
        TextView interactive_coupon;//优惠券
        TextView interactive_red;//佣金比例
        ExpandTextView interactive_buy;//立即购买
        private int corner;

        public ViewHolder(View itemView) {
            super(itemView);
            corner = DeviceUtil.dip2px(mContext, 8);
            explosions_content = itemView.findViewById(R.id.explosions_content);
            explosions_img = itemView.findViewById(R.id.explosions_img);
            explosions_time = itemView.findViewById(R.id.explosions_time);
            img_status = itemView.findViewById(R.id.img_status);
            interactive_price = itemView.findViewById(R.id.interactive_price);
            interactive_vonum = itemView.findViewById(R.id.interactive_vonum);
            interactive_coupon = itemView.findViewById(R.id.interactive_coupon);
            interactive_red = itemView.findViewById(R.id.interactive_red);
            interactive_buy = itemView.findViewById(R.id.interactive_buy);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            PushGoodsBean bean = (PushGoodsBean) itemData;
            explosions_time.setText(TimeUtils.transForDate(bean.getCreateTime(), "yyyy-MM-dd " +
                    "HH:mm:ss"));
            new GlideImageLoader(mContext, bean.getCommondity().getPictUrl())
                    .error(R.drawable.empty_pic_list)
                    .placeholder(R.drawable.empty_pic_list)
                    .into(explosions_img);
            explosions_content.setText(bean.getCommondity().getTitle());
            ViewUtil.setTextViewFormat(mContext, interactive_vonum, R.string.list_volume_text,
                    ViewUtil.getIVolume(bean.getCommondity().getVolume()));
            ViewUtil.setTextViewFormat(mContext, interactive_red,
                    R.string.product_commission_price,
                    StringUtils.getFormattedDoubleOrInt(bean.getCommondity().getCommissionMoney()), "元");
            interactive_price.setText(StringUtils.getFormattedDoubleOrInt(bean.getCommondity().getDiscountsPriceAfter()));

            interactive_coupon.setVisibility(bean.getCommondity().getCouponAmount() == 0 ?
                    View.GONE : View.VISIBLE);

            ViewUtil.setTextViewFormat(mContext, interactive_coupon,
                    R.string.product_coupon_price,
                    StringUtils.getFormattedDoubleOrInt(bean.getCommondity().getCouponAmount()));
            interactive_buy.setText(bean.getDescribe());
            img_status.setVisibility(bean.getReadStatus() == 1 ? View.GONE : View.VISIBLE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TimeUtils.isFrequentOperation()) {
                        return;
                    }

                    CommonSchemeJump.showPreciseDetailActivity(mContext,
                            bean.getCommondity().getItemId(), bean.getCommondity().getIcon());
                    SettingRequestManager.requestInteractMessageStatus(String.valueOf(bean.getId()))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DefaultRx2Subscribe<BaseBean<String>>() {
                                @Override
                                public void onNext(BaseBean<String> userInfoBeanBaseBean) {
                                    super.onNext(userInfoBeanBaseBean);
                                    if (!userInfoBeanBaseBean.success()) {
                                        onError(new Throwable(userInfoBeanBaseBean.msg));
                                        return;
                                    }
                                    img_status.setVisibility(View.GONE);

                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    super.onError(throwable);

                                }
                            });

                }
            });


        }
    }


}
