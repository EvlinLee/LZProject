package com.by.lizhiyoupin.app.activity.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.PermissionsUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.web.LitchiProtocal;
import com.by.lizhiyoupin.app.component_ui.weight.CountTimeView;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.ProduclimitSkilltListBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.manager.AccountManager;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.hjq.toast.ToastUtils;

import androidx.fragment.app.FragmentManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/14 13:38
 * Summary: 限时秒杀
 */
public class LimitedTimeSpikeAdapter extends CommonRecyclerViewAdapter {
    public static final String TAG = LimitedTimeAdapter.class.getSimpleName();
    private FragmentManager fragmentManager;

    private CountTimeView countTimeView;
    private int getShowTimes; //抢购状态 -1 = 已抢购 0 = 正在抢购 1 = 即将开始抢购

    public LimitedTimeSpikeAdapter(Context context, FragmentManager fragmentManager, CountTimeView countTimeView) {
        super(context);
        this.fragmentManager = fragmentManager;
        this.countTimeView = countTimeView;
    }

    /**
     * 秒杀状态
     *
     * @param getShowTimes
     */
    public void setGetShowTimes(int  getShowTimes) {
        this.getShowTimes = getShowTimes;
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_limited_time_spike_list, parent, false);

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
        TextView mItemRobTv;//抢
        TextView mItemRemindTv;//提醒
        ProgressBar progress;//进度条
        TextView item_list_progress_tv;
        TextView item_list_after_ticket_tv;//原价
        TextView item_onlynum;//仅剩
        TextView limit_discount;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemListTopIv = itemView.findViewById(R.id.item_list_top_iv);
            mItemListTitleTv = itemView.findViewById(R.id.item_list_title_tv);
            mItemListBottomLeftTv = itemView.findViewById(R.id.item_list_bottom_left_tv);
            mItemListBottomRightTv = itemView.findViewById(R.id.item_list_bottom_right_tv);
            mItemListPriceTv = itemView.findViewById(R.id.item_list_price_tv);
            mItemListVolumeTv = itemView.findViewById(R.id.item_list_volume_tv);
            mItemListNameTv = itemView.findViewById(R.id.item_list_name_tv);
            mItemListNameIconIv = itemView.findViewById(R.id.item_list_name_icon_iv);
            mItemRobTv = itemView.findViewById(R.id.item_rob_tv);
            mItemRemindTv = itemView.findViewById(R.id.item_remind_tv);
            mItemRemindTv.setOnClickListener(this);
            progress=itemView.findViewById(R.id.progress1);
            item_list_progress_tv=itemView.findViewById(R.id.item_list_progress_tv);
            item_list_after_ticket_tv=itemView.findViewById(R.id.item_list_after_ticket_tv);
            item_onlynum=itemView.findViewById(R.id.item_onlynum);
            limit_discount=itemView.findViewById(R.id.limit_discount);

        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            ProduclimitSkilltListBean bean = (ProduclimitSkilltListBean) itemData;
            new GlideImageLoader(mContext, bean.getCommodityImg())
                    .error(R.drawable.empty_pic_list)
                    .into(mItemListTopIv);
            progress.setProgress(bean.getBuyPercent());
            mItemListTitleTv.setText(bean.getCommodityName());
            item_list_after_ticket_tv.setText("原价"+StringUtils.getFormattedDoubleOrInt(bean.getOriginalAmount()));
            item_onlynum.setText("仅剩"+bean.getResidueCount()+"件");
           // mItemListPriceTv.setText(StringUtils.getFormattedDoubleOrInt(bean.getRealPrice()));

            ViewUtil.setMoneyText(mContext, mItemListPriceTv, "¥" + StringUtils.getFormattedDoubleOrInt(bean.getRealPrice()),
                    R.style.product_money14_FF005F, R.style.product_money22_FF005F);

            mItemListNameIconIv.setImageResource(ViewUtil.getIconImg(bean.getPlatformType()));
            limit_discount.setText(StringUtils.getFormattedDoubleOrInt(bean.getPriceDiscount())+"折");
            if (bean.getCommodityStatus() == 3 ){ //商品购买状态 1 即将开始 2 商品已抢完 3 商品正在快抢中
                mItemRobTv.setVisibility(View.VISIBLE);
                mItemRobTv.setText("马上抢");
                mItemRemindTv.setVisibility(View.GONE);
                mItemRobTv.setBackgroundResource(R.drawable.shape_bg_limited_time_red_circular);
                mItemRobTv.setText("马上抢");
                item_list_progress_tv.setText("已抢"+bean.getBuyPercent()+"%");

            } else if (bean.getCommodityStatus() == 2){
                mItemRobTv.setBackgroundResource(R.drawable.shape_bg_limited_time_gray_circular);
                mItemRobTv.setText("已抢完");
                mItemRobTv.setVisibility(View.VISIBLE);
                mItemRemindTv.setVisibility(View.GONE);
                item_list_progress_tv.setText("已抢"+bean.getBuyPercent()+"%");


            }else {
                mItemRobTv.setVisibility(View.GONE);
                mItemRemindTv.setVisibility(View.VISIBLE);
                item_list_progress_tv.setText("等待开抢");
                mItemRemindTv.setText(bean.getRemindStatus() == 0 ? R.string.limit_time_spike_remind_text : R.string.limit_time_spike_has_cancle_text);
                mItemRemindTv.setBackgroundResource(bean.getRemindStatus() == 0 ?R.drawable.shape_bg_limited_time_green_four:R.drawable.shape_bg_limited_timeskill_gray_four);

            }

        }


        @Override
        public void onItemClicked() {
            super.onItemClicked();
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            ProduclimitSkilltListBean bean = (ProduclimitSkilltListBean) getListData().get(mRealPosition);
            if (bean == null) {
                return;
            }
            if ( bean.getCommodityStatus()== 1) {
                //还未到抢购秒杀时间
                MessageToast.showToast(mContext, "还未到秒杀时间");
                return;
            }else if (bean.getCommodityStatus()== 2) {
                ToastUtils.show("商品已抢完，换个商品吧");
                return;
            }

            Bundle bundle=new Bundle();
            bundle.putLong(CommonConst.KEY_THREE_DETAIL_ID,Long.parseLong(bean.getCommodityId()));
            bundle.putInt(CommonConst.KEY_PRODUCT_PLATFORM_TYPE,bean.getPlatformType());
            bundle.putInt(CommonConst.KEY_LIMIT_TIME_TYPE,getShowTimes);
            bundle.putInt(CommonConst.KEY_LIMIT_PRODUCT_TYPE,bean.getFastBuyCommodityType());
            CommonSchemeJump.showActivity(mContext,"/app/PreciseDetailActivity",bundle);
        }

        @Override
        public void onClick(View v) {
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            switch (v.getId()) {
                case R.id.item_remind_tv:
                    if (shouldLoginFirst()) {
                        //还未到抢购秒杀时间,需要设置提醒
                        boolean notificationEnabled = PermissionsUtil.isNotificationEnabled(mContext);
                        if (!notificationEnabled) {
                            //没有开启推送权限则去设置开启
                            DiaLogManager.showSettingPushTipsDialog(mContext, fragmentManager);
                        } else {
                            //开启了则发送接口推送
                            ProduclimitSkilltListBean bean = (ProduclimitSkilltListBean) getListData().get(mRealPosition);
                            if (bean.getRemindStatus() == 0) {
                                //未提提醒则 改为已设置提醒
                                requestAddPush(bean.getCommodityName(), LitchiProtocal.LIMITED_TIME_SPIKE_URL, bean.getSecond(), bean.getCommodityId(),getShowTimes,bean.getFastBuyCommodityType());
                                mItemRemindTv.setText(R.string.limit_time_spike_has_cancle_text);
                                mItemRemindTv.setBackgroundResource(R.drawable.shape_bg_limited_timeskill_gray_four);
                                bean.setRemindStatus(1);
                                DiaLogManager.showToastDialog(mContext, fragmentManager,"设置提醒成功\n将在开抢前1分钟\n通过消息推送告知",R.drawable.toast_detail_success, Gravity.TOP,false);
                            }else {
                                requestDeletePush(bean.getCommodityId(),getShowTimes,bean.getFastBuyCommodityType());
                                mItemRemindTv.setText(R.string.limit_time_spike_remind_text);
                                mItemRemindTv.setBackgroundResource(R.drawable.shape_bg_limited_time_green_four);
                                bean.setRemindStatus(0);
                                DiaLogManager.showToastDialog(mContext, fragmentManager,"取消成功\n系统将不会进行推送告知",R.drawable.toast_detail_success, Gravity.TOP,false);
                            }
                        }
                        return;
                    }
                    break;
            }
        }
        private boolean shouldLoginFirst() {
            AccountManager accountManager = LiZhiApplication.getApplication().getAccountManager();
            if (!accountManager.isLogined()) {
                CommonSchemeJump.showLoginActivity(mContext);
                return false;
            } else {
                return true;
            }
        }
    }

    private void requestAddPush(String title, String link, int second, String  itemId,int batch,int commdyitype) {
        AccountManager accountManager = LiZhiApplication.getApplication().getAccountManager();
        //标记已设置提醒
        ApiService.getActivityApi().requestAddPushRemindTag(batch,itemId,commdyitype)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        LZLog.i(TAG,"requestAddPushRemindTag success=="+baseBean.success());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG,"requestAddPushRemindTag error=="+throwable);
                    }
                });
        long accountId = accountManager.getAccountId();
        //标记推送
        ApiService.getNewsApi().requestPushOfLimitTime(accountId, 1, itemId,title, link, second).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        LZLog.i(TAG,"requestPushOfLimitTime success=="+baseBean.success());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG,"requestPushOfLimitTime error=="+throwable);
                    }
                });
    }
    private void requestDeletePush( String  itemId,int batch,int commdyitype){
       // AccountManager accountManager = LiZhiApplication.getApplication().getAccountManager();
        //已设置标记  刪除
        ApiService.getActivityApi().requestDeletePushRemindTag(batch,itemId,commdyitype)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        LZLog.i(TAG,"requestDeletePushRemindTag success=="+baseBean.success());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG,"requestDeletePushRemindTag error=="+throwable);
                    }
                });

        ApiService.getNewsApi().requestDeletePushOfLimitTime(itemId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        LZLog.i(TAG,"requestDeletePushOfLimitTime success=="+baseBean.success());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG,"requestDeletePushOfLimitTime error=="+throwable);
                    }
                });
    }
}
