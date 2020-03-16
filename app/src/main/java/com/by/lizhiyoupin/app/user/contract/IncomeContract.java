package com.by.lizhiyoupin.app.user.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.IncomeDetailsVO;
import com.by.lizhiyoupin.app.io.bean.MyIncomeVO;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/7 14:08
 * Summary:
 */
public interface IncomeContract {
    interface  IncomeRecordView extends BaseView {
        void requestIncomeRecordSuccess(MyIncomeVO myIncomeVO);

        void requestIncomeRecordError(Throwable throwable);

    }

    interface IncomeRecordModel extends BaseModel {

        Observable<BaseBean<MyIncomeVO>> requestIncomeRecord( );

    }

    abstract class IncomeRecordPresenters extends BasePresenter<IncomeRecordView> {

        public abstract void requestIncomeRecord( );

    }


    interface  IncomeDetailView extends BaseView {
        void requestIncomeDetailSuccess(List<IncomeDetailsVO>  voList);

        void requestIncomeDetailError(Throwable throwable);

    }
    interface IncomeDetailModel extends BaseModel {

        Observable<BaseBean<List<IncomeDetailsVO>>> requestIncomeDetail(int desc, int start, int limit);

    }

    abstract class IncomeDetailPresenters extends BasePresenter<IncomeDetailView> {

        public abstract void requestIncomeDetail( int desc, int start, int limit);

    }
}
