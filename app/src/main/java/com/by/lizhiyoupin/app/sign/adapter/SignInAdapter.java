package com.by.lizhiyoupin.app.sign.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.weight.SignCountTimeView;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CommonRecyclerViewAdapter;
import com.by.lizhiyoupin.app.component_umeng.share.presenter.LZShare;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.io.bean.SignInBean;
import com.by.lizhiyoupin.app.io.entity.ChannelListBean;
import com.by.lizhiyoupin.app.sign.contract.SignInContract;
import com.by.lizhiyoupin.app.utils.ViewUtil;
import com.hjq.toast.ToastUtils;

import java.util.List;

import androidx.constraintlayout.widget.Group;
import cn.jpush.android.api.JPushInterface;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/9 15:53
 * Summary: 签到 Adapter
 */
public class SignInAdapter extends CommonRecyclerViewAdapter {
    private SignInContract.ISignInPresenter mISignInPresenter;
    private double allEarnings;
    public SignInAdapter(Context context, SignInContract.ISignInPresenter iSignInPresenter) {
        super(context);
        mISignInPresenter = iSignInPresenter;
    }

    /**
     * 累计收益
     * @param allEarnings
     */
    public void setAllEarnings(double allEarnings){
        this.allEarnings=allEarnings;
    }
    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_sign_in_layout, parent, false);
        return new ViewHolder(inflate);
    }



   public class ViewHolder extends CommonViewHolder implements View.OnClickListener, SignCountTimeView.CountTime0Interface {
        TextView dayLeftTv;
        TextView dayRightTv;
        ImageView redBgIv;
        TextView priceTv;//12元
        public SignCountTimeView redTimeTv;//23:1:45
        TextView redTimeStopTv;//后停止翻倍
        TextView redEnevlopesTv;//荔枝红包
        TextView doubleTv;//翻倍
        Group showGroup;
        View redItem;

        public ViewHolder(View itemView) {
            super(itemView);
            redItem = itemView.findViewById(R.id.red_item);
            dayLeftTv = itemView.findViewById(R.id.day_left_tv);
            dayRightTv = itemView.findViewById(R.id.day_right_tv);
            redBgIv = itemView.findViewById(R.id.red_bg_iv);
            priceTv = itemView.findViewById(R.id.sign_in_get_price_tv);
            redTimeTv = itemView.findViewById(R.id.red_time_tv);
            redTimeStopTv = itemView.findViewById(R.id.red_time_stop_tv);
            redEnevlopesTv = itemView.findViewById(R.id.red_envelopes_tv);
            doubleTv = itemView.findViewById(R.id.double_tv);
            showGroup = itemView.findViewById(R.id.showGroup);
            doubleTv.setOnClickListener(this);
            LZLog.i("SSS","onChanged==registerDataSetObserver");
            redTimeTv.setCountTime0Listener(this);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            SignInBean.UserSignBonusVOListBean bean = (SignInBean.UserSignBonusVOListBean) itemData;
            dayLeftTv.setText(bean.getDateName());
            if (bean.getDateNameType() == 0) {
                //昨天
                dayRightTv.setText("");
                ViewUtil.setDrawableOfTextView(dayRightTv, bean.getSignStatus() == 1 ?
                        R.drawable.sign_in_receive : R.drawable.sign_in_unreceive, ViewUtil.DrawableDirection.RIGHT);
                if (bean.getSignStatus() == 1) {
                    //已经领取
                    showGroup.setVisibility(View.GONE);
                    doubleTv.setVisibility(View.GONE);
                    redEnevlopesTv.setVisibility(View.VISIBLE);
                    redBgIv.setImageResource(R.drawable.red_open_pic);
                    redEnevlopesTv.setText("荔枝红包");
                    if (bean.getTodayAllBonusAmount() != 0) {
                        ViewUtil.setLastText(mContext, false, priceTv, bean.getTodayAllBonusAmount() + "元"
                                , R.style.product_money13_BF4335, R.style.product_money11_BF4335);
                    } else {
                        priceTv.setText("");
                    }

                } else {
                    //未领取
                    redBgIv.setImageResource(R.drawable.red_un_get_pic);
                    showGroup.setVisibility(View.GONE);
                    doubleTv.setVisibility(View.GONE);
                    redEnevlopesTv.setVisibility(View.VISIBLE);
                    redEnevlopesTv.setText("红包已错过");
                    priceTv.setText("");
                }
            } else if (bean.getDateNameType() == 1) {
                //今天
                dayRightTv.setText("");
                ViewUtil.setDrawableOfTextView(dayRightTv, bean.getSignStatus() == 1 ?
                        R.drawable.sign_in_receive : R.drawable.sign_in_unreceive, ViewUtil.DrawableDirection.RIGHT);
                if (bean.getSignStatus() == 1) {
                    //已经领取
                    showGroup.setVisibility(bean.getDoubleStatus() == 1 ? View.INVISIBLE:View.VISIBLE);
                    doubleTv.setVisibility(View.VISIBLE);
                    redEnevlopesTv.setVisibility(View.GONE);
                    redBgIv.setImageResource(R.drawable.red_open_pic);
                    if (bean.getTodayAllBonusAmount() != 0) {
                        ViewUtil.setLastText(mContext, false, priceTv, bean.getTodayAllBonusAmount() + "元"
                                , R.style.product_money13_BF4335, R.style.product_money11_BF4335);
                    } else {
                        priceTv.setText("");
                    }
                    redTimeTv.startLimitedTime(bean.getSeconds());
                    doubleTv.setText(bean.getDoubleStatus() == 1 ? "已翻倍" : "立即翻倍");

                } else {
                    //未领取
                    redBgIv.setImageResource(R.drawable.red_sign_get_pic);
                    showGroup.setVisibility(View.GONE);
                    doubleTv.setVisibility(View.GONE);
                    redEnevlopesTv.setVisibility(View.VISIBLE);
                    redEnevlopesTv.setText("荔枝红包");
                    priceTv.setText("");
                }
            } else {
                //明天
                dayRightTv.setText("明天领");
                ViewUtil.setDrawableOfTextView(dayRightTv, null, ViewUtil.DrawableDirection.RIGHT);
                redBgIv.setImageResource(R.drawable.red_sign_get_pic);
                showGroup.setVisibility(View.GONE);
                doubleTv.setVisibility(View.GONE);
                redEnevlopesTv.setVisibility(View.VISIBLE);
                priceTv.setText("");
                redEnevlopesTv.setText("荔枝红包");
            }
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            if (TimeUtils.isFrequentOperation()){
                return;
            }
            if (mRealPosition == 1) {
                SignInBean.UserSignBonusVOListBean bean = (SignInBean.UserSignBonusVOListBean) getListData().get(mRealPosition);
                if (bean.getSignStatus() == 0) {
                    //未领取，点击请求弹出红包框
                    if (mISignInPresenter != null) {
                        mISignInPresenter.requestSignInRedPaper(1);
                    }
                }
            }else if (mRealPosition==2){
                ToastUtils.show("明天来签到哦");
            }
        }

        @Override
        public void onClick(View v) {
            if (TimeUtils.isFrequentOperation()) {
                return;
            }
            if (v.getId() == R.id.double_tv) {
                //点击翻倍，即弹出分享框
                String code = LiZhiApplication.getApplication().getAccountManager().getAccountInfo().getCode();
                List<ChannelListBean> channelListH5 = LZShare.getChannelListH5(WebUrlManager.getSignInShareUrl(code),null,
                        String.format(mContext.getResources().getString(R.string.share_sign_in_make_money_text), StringUtils.getFormattedDouble0(allEarnings)));
                LZShare.share(channelListH5).show();
            }
        }

        @Override
        public void notifyTimeChanged() {
            LZLog.i("SSS","onChanged==签到刷新");
            if (mISignInPresenter!=null){
                mISignInPresenter.requestSignInInfo(JPushInterface.isNotificationEnabled(mContext) == 1 ? 1 : 0);
            }
        }
    }
}
