package com.by.lizhiyoupin.app.detail.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.entity.CreateShareEntity;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/23 19:52
 * Summary:
 */
public interface CreateShareContract {
    interface CreateShareView extends BaseView {
        void requestShareDetailSuccess(CreateShareEntity shareEntity);

        void requestShareDetailError(Throwable throwable);

    }

    interface CreateShareModel extends BaseModel {
        Observable<BaseBean<CreateShareEntity>> requestShareDetail(Long commodityId,
                                                                   int type,
                                                                   int platformType,
                                                                   String title,
                                                                   String zkFinalPrice,
                                                                   String discountsPriceAfter,
                                                                   String volume,
                                                                   String couponAmount,
                                                                   String pictUrl);
    }

    abstract class CreateSharePresenters extends BasePresenter<CreateShareView> {
        public abstract void requestShareDetail(Long commodityId, int type, int platformType,
                                                String title,
                                                String zkFinalPrice,
                                                String discountsPriceAfter,
                                                String volume,
                                                String couponAmount,
                                                String pictUrl);
    }
}
