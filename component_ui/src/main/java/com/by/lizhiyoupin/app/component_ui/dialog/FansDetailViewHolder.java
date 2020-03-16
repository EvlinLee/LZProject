package com.by.lizhiyoupin.app.component_ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.StringUtils;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.R;
import com.by.lizhiyoupin.app.component_ui.utils.GlideImageLoader;
import com.by.lizhiyoupin.app.component_ui.weight.MoneyTextView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.FansDetailBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.message_box.MessageBox;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.message_box.holder.IContentHolder;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/11 20:46
 * Summary: 显示粉丝详情
 */
public class FansDetailViewHolder implements IContentHolder, View.OnClickListener {

    private ImageView mFansDetailPhotoIv;
    private ImageView mFansDetailCloseIv;
    private TextView mFansDetailNameTv;
    private TextView mFansDetailCodeTv;
    private MoneyTextView mFansDetailAllMoneyTv;
    private MoneyTextView mFansLastMonthForMeTv;
    private MoneyTextView mFansThisMonthForMeTv;
    private TextView mFansDetailTimeTv;
    private Context context;
    private MessageBox mMessageBox;
    private Long mUid;
    private Disposable mDisposable;
    private String mWechat;

    public FansDetailViewHolder(Context context, Long uid) {
        this.context = context;
        mUid = uid;
    }

    @Override
    public View contentView(MessageBox messageBox, ViewGroup parent) {
        mMessageBox = messageBox;
        View content = LayoutInflater.from(context).inflate(R.layout.messagebox_fans_detail_layout, parent, false);
        initView(content);
        return content;
    }

    private void initView(View content) {
        mFansDetailPhotoIv = content.findViewById(R.id.fans_detail_photo_iv);
        mFansDetailCloseIv = content.findViewById(R.id.fans_detail_close_iv);
        mFansDetailNameTv = content.findViewById(R.id.fans_detail_name_tv);
        mFansDetailCodeTv = content.findViewById(R.id.fans_detail_code_tv);
        mFansDetailAllMoneyTv = content.findViewById(R.id.fans_detail_all_money_tv);
        mFansLastMonthForMeTv = content.findViewById(R.id.fans_last_month_for_me_tv);
        mFansThisMonthForMeTv = content.findViewById(R.id.fans_this_month_for_me_tv);
        mFansDetailTimeTv = content.findViewById(R.id.fans_detail_time_tv);
        mFansDetailCloseIv.setOnClickListener(this);
        mFansDetailCodeTv.setOnClickListener(this);
        requestData();
    }

    private void requestData() {
        ApiService.getFansApi().requestGetFansDetail(mUid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<FansDetailBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(BaseBean<FansDetailBean> fansDetailBeanBaseBean) {
                        super.onNext(fansDetailBeanBaseBean);
                        if (fansDetailBeanBaseBean.success() && fansDetailBeanBaseBean.getResult() != null) {
                            refreshData(fansDetailBeanBaseBean.getResult());
                        } else {
                            onError(new Throwable(fansDetailBeanBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        MessageToast.showToast(context,"数据加载异常,请稍后再试");
                    }
                });
    }

    private void refreshData(FansDetailBean bean) {
        mWechat = bean.getWeiXin();
        mFansDetailNameTv.setText(bean.getName());
        mFansDetailCodeTv.setText(String.format(context.getResources().getString(R.string.fans_detail_code_text), bean.getWeiXin()));
        mFansDetailAllMoneyTv.setText(StringUtils.getFormattedDouble(bean.getAllContribution()));

        mFansLastMonthForMeTv.setText(StringUtils.getFormattedDouble(bean.getLastMonthContribution()));
        mFansThisMonthForMeTv.setText(StringUtils.getFormattedDouble(bean.getNowMonthContribution()));
        String time = String.format(context.getResources().getString(R.string.fans_detail_registration_time_text),
                TimeUtils.transDateToDateString(bean.getCreateTime(), 3));
        mFansDetailTimeTv.setText(time);
        new GlideImageLoader(context, bean.getAvatar())
                .placeholder(R.drawable.default_face)
                .error(R.drawable.default_face)
                .into(mFansDetailPhotoIv);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fans_detail_close_iv) {
            if (mDisposable != null && !mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
            mMessageBox.dismiss();
        } else if (v.getId() == R.id.fans_detail_code_tv) {
            if (TextUtils.isEmpty(mWechat)){
                MessageToast.showToast(context,"该用户暂无绑定微信号");
            }else{
                //复制 微信号
                DeviceUtil.putClipboardText(context, mWechat);
                MessageToast.showToast(context,"复制成功");
            }

        }
    }
}
