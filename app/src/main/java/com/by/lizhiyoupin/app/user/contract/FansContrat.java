package com.by.lizhiyoupin.app.user.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.FansCountBean;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/9 15:23
 * Summary:
 */
public interface FansContrat {
    interface  FansMainView extends BaseView {
        void requestFansMainSuccess(FansCountBean fansCountBean);

        void requestFansMainError(Throwable throwable);

    }

    interface FansMainModel extends BaseModel {

        Observable<BaseBean<FansCountBean>> requestFansMain( );

    }

    abstract class FansMainPresenters extends BasePresenter<FansMainView> {

        public abstract void requestFansMain( );

    }
}
