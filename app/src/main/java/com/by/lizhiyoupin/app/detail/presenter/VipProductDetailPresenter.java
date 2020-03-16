package com.by.lizhiyoupin.app.detail.presenter;

import android.content.Context;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.VipGoodsBean;
import com.by.lizhiyoupin.app.detail.contract.VipProductDetailContract;
import com.by.lizhiyoupin.app.detail.model.VipProductDetailModel;
import com.by.lizhiyoupin.app.utils.ViewUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/23 14:08
 * Summary:
 */
public class VipProductDetailPresenter extends VipProductDetailContract.VipProductDetailPresenters {
    private VipProductDetailContract.VipProductDetailModel mDetailModel;
    private VipProductDetailContract.VipProductDetailView mDetailView;

    public VipProductDetailPresenter( VipProductDetailContract.VipProductDetailView view ) {
        this.mDetailView=view;
        this.mDetailModel=new VipProductDetailModel();
    }

    @Override
    public void requestVipGoodsDetail(long id,int activityType) {
        mDetailModel.requestVipGoodsDetail(id,activityType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<VipGoodsBean>>() {
                    @Override
                    public void onNext(BaseBean<VipGoodsBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()){
                            mDetailView.requestVipGoodsDetailSuccess(listBaseBean.data);
                        }else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mDetailView.requestVipGoodsDetailError(throwable);
                    }

                });
    }

    /**
     * 根据级别设置推广奖
     * @param context
     * @param textView
     * @param vipLevel
     */
    @Override
    public void setLevelData(Context context, TextView textView, int vipLevel) {
        switch (vipLevel) {
            case 1://1-普通
                break;
            case 2://2-超级
            case 3:// 3-Plus超级
                ViewUtil.setTextViewFormat(context, textView, R.string.product_detail_spread_text, 30);
                break;
            case 4:// 4-运营商
                ViewUtil.setTextViewFormat(context, textView, R.string.product_detail_spread_text, 32);
                break;
            case 5:// 5-plus运营商
                ViewUtil.setTextViewFormat(context, textView, R.string.product_detail_spread_text, 55);
                break;
        }
    }
}
