package com.by.lizhiyoupin.app.activity.constract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBean;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBuyBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/15 17:59
 * Summary:
 */
public interface ShakeCouponMainConstract {
    interface ShakeCouponMainView extends BaseView {

        void requestShakeCouponMainSuccess(List<ShakeCouponBean> beanList);

        void requestShakeCouponMainError(Throwable throwable);

        void requestShakeCouponBarrageListSuccess(List<ShakeCouponBuyBean> beanList);

        void requestShakeCouponBarrageListError(Throwable throwable);
    }


    interface ShakeCouponMainModel extends BaseModel {

        Observable<BaseBean<List<ShakeCouponBean>>> requestShakeCouponMainList2(int hourType,
                                                                                int start,
                                                                                int limit,
                                                                                long itemId);

        Observable<BaseBean<List<ShakeCouponBean>>> requestPreciseShakeCouponList(int hourType,
                                                                                  int start,
                                                                                  int limit,
                                                                                  long itemId,
                                                                                  int type
        );

        Observable<BaseBean<List<ShakeCouponBuyBean>>> requestShakeCouponBarrageList();
    }

    abstract class ShakeCouponMainPresenters extends BasePresenter<ShakeCouponMainView> {

        public abstract void requestShakeCouponMainList2(int hourType,
                                                         int start,
                                                         int limit,
                                                         long itemId);

        public abstract void requestPreciseShakeCouponList(int hourType,
                                                           int start,
                                                           int limit,
                                                           long itemId,
                                                           int type);

        public abstract void requestShakeCouponBarrageList();
    }
}
