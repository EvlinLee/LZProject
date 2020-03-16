package com.by.lizhiyoupin.app.user.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.ExpandTextView;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;
import com.by.lizhiyoupin.app.io.bean.PushGoodsBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.loader.LoadMoreHelperRx;
import com.by.lizhiyoupin.app.stack.ActivityStack;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.hjq.toast.ToastUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/5 13:56
 * Summary: 推送列表adapter
 */
public class PushHistoryAdapter extends CommonRecyclerViewAdapter {
    private LoadMoreHelperRx<PushGoodsBean, Integer> mHelperRx;

    public PushHistoryAdapter(Context context) {
        super(context);
    }

    public void setHelperRx(LoadMoreHelperRx<PushGoodsBean, Integer> mHelperRx) {
        this.mHelperRx = mHelperRx;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_push_history_layout, parent, false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends CommonViewHolder implements View.OnClickListener {


        private int corner;
        private TextView explosions_content;//title
        private ImageView explosions_img;//主图
        private TextView explosions_time, explosions_push;//推送时间
        private TextView interactive_price;//价格
        private TextView interactive_vonum;//月销
        private TextView interactive_coupon;//优惠券金额
        private TextView interactive_red;//佣金比例
        private ExpandTextView interactive_descibe;//描述
        private TextView item_shopping_delete_tv;//删除
        private RelativeLayout item_push_history;


        public ViewHolder(View itemView) {
            super(itemView);
            corner = DeviceUtil.dip2px(mContext, 8);
            explosions_content = itemView.findViewById(R.id.explosions_content);
            explosions_img = itemView.findViewById(R.id.explosions_img);
            explosions_time = itemView.findViewById(R.id.explosions_time);
            interactive_price = itemView.findViewById(R.id.interactive_price);
            interactive_vonum = itemView.findViewById(R.id.interactive_vonum);
            interactive_coupon = itemView.findViewById(R.id.interactive_coupon);
            interactive_red = itemView.findViewById(R.id.interactive_red);
            interactive_descibe = itemView.findViewById(R.id.interactive_descibe);
            item_shopping_delete_tv = itemView.findViewById(R.id.item_shopping_delete_tv);
            item_push_history = itemView.findViewById(R.id.item_push_history);
            item_push_history.setOnClickListener(this);
            item_shopping_delete_tv.setOnClickListener(this);
            explosions_push = itemView.findViewById(R.id.explosions_push);


        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            PushGoodsBean bean = (PushGoodsBean) itemData;
            PreciseListBean commondity = bean.getCommondity();

            new GlideImageLoader(mContext, commondity.getPictUrl())
                    .error(R.drawable.empty_pic_list)
                    .transform(new RoundedCornersTransformation(corner,
                            0, RoundedCornersTransformation.CornerType.ALL))
                    .into(explosions_img);

            interactive_coupon.setVisibility(commondity.getCouponAmount() == 0 ? View.GONE :
                    View.VISIBLE);

            explosions_content.setText(commondity.getTitle());
            explosions_time.setText(TimeUtils.transForDate(bean.getSendTime(), "yyyy-MM-dd " +
                    "HH:mm:ss"));
            ViewUtil.setTextViewFormat(mContext, interactive_vonum, R.string.list_volume_text,
                    ViewUtil.getIVolume(commondity.getVolume()));


            interactive_red.setVisibility(commondity.getCommissionRate() == 0 ? View.GONE :
                    View.VISIBLE);
            ViewUtil.setTextViewFormat(mContext, interactive_red,
                    R.string.product_commission_rate_price,
                    StringUtils.getFormattedDoubleOrInt(commondity.getCommissionRate()), "%");


            interactive_price.setText(StringUtils.getFormattedDoubleOrInt(commondity.getDiscountsPriceAfter()));
            interactive_coupon.setVisibility(commondity.getCouponAmount() == 0 ? View.GONE :
                    View.VISIBLE);
            ViewUtil.setTextViewFormat(mContext, interactive_coupon,
                    R.string.product_coupon_price,
                    StringUtils.getFormattedDoubleOrInt(commondity.getCouponAmount()));
            interactive_descibe.setText(bean.getDescribe());

            //判断发送状态
            item_shopping_delete_tv.setVisibility(bean.getSendStatus() == 1 ? View.GONE :
                    View.VISIBLE);
            explosions_push.setText(bean.getSendStatus() == 1 ? "已发送" : "未发送");


        }


        @Override
        public void onClick(View v) {
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            PushGoodsBean cartListBean =
                    (PushGoodsBean) mListData.get(mRealPosition);
            switch (v.getId()) {
                case R.id.item_push_history:
                    if (cartListBean.getSendStatus() == 0) {
                        CommonSchemeJump.showPushEditActivity(mContext, cartListBean.getId());
                    } else {
                        ToastUtils.show("已发送的消息不可编辑");
                    }

                    break;
                case R.id.item_shopping_delete_tv:
                    //删除
                    ApiService.getPushEditApi().requestDeletePushGoodsInfo(cartListBean.getId())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new DefaultRx2Subscribe<BaseBean<Object>>() {
                                @Override
                                public void onNext(BaseBean<Object> baseBean) {
                                    super.onNext(baseBean);
                                    if (!ActivityStack.isActivityDestoryed(mContext)) {
                                        ToastUtils.show("删除成功");
                                        mListData.remove(cartListBean);
                                        if (mHelperRx != null) {
                                            //两边数据都删除，然後刷新
                                            mHelperRx.getData().remove(cartListBean);
                                            mHelperRx.notifyDataSetChange();
                                        } else {
                                            notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    super.onError(throwable);
                                    if (!ActivityStack.isActivityDestoryed(mContext)) {
                                        ToastUtils.show("删除失败" + throwable.getMessage());
                                    }
                                }
                            });

                    break;
            }
        }
    }
}
