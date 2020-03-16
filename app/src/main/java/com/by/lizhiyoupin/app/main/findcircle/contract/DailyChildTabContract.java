package com.by.lizhiyoupin.app.main.findcircle.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.FindCircleChildListBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/19 17:32
 * Summary:
 */
public interface DailyChildTabContract {
    interface DailyChildTabView extends BaseView {
        void requestTabChildListSuccess(List<FindCircleChildListBean> beanList);

        void requestTabChildListError(Throwable throwable);
    }

    interface DailyChildTabModel extends BaseModel {

        Observable<BaseBean<List<FindCircleChildListBean>>> requestTabChildList(long ringFirstKindId,
                                                                                long ringSecondKindId,
                                                                                int start,
                                                                                int limit);
    }

    abstract class DailyChildTabPresenters extends BasePresenter<DailyChildTabView> {

        public abstract void requestTabChildList(long ringFirstKindId,
                                                 long ringSecondKindId,
                                                 int start,
                                                 int limit);

    }
}
