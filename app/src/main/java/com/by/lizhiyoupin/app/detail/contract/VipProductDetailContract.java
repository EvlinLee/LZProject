package com.by.lizhiyoupin.app.detail.contract;

import android.content.Context;
import android.widget.TextView;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.VipGoodsBean;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/23 14:09
 * Summary:
 */
public interface VipProductDetailContract {
    interface VipProductDetailView extends BaseView {
        void requestVipGoodsDetailSuccess(VipGoodsBean bean);
        void requestVipGoodsDetailError(Throwable throwable);
    }

    interface VipProductDetailModel extends BaseModel {

        Observable<BaseBean<VipGoodsBean>> requestVipGoodsDetail(final  long id,int activityType);

    }

    abstract class VipProductDetailPresenters extends BasePresenter<VipProductDetailView> {

        public abstract void requestVipGoodsDetail(final  long id,int activityType);
        public abstract void setLevelData(Context context, TextView textView, int vipLevel);
    }
}
