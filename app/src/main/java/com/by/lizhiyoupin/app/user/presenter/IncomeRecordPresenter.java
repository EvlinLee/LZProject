package com.by.lizhiyoupin.app.user.presenter;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.MyIncomeVO;
import com.by.lizhiyoupin.app.user.contract.IncomeContract;
import com.by.lizhiyoupin.app.user.model.IncomeRecordModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/7 14:07
 * Summary: 收益记录presenter
 */
public class IncomeRecordPresenter extends IncomeContract.IncomeRecordPresenters {
    public static final String TAG = IncomeRecordPresenter.class.getSimpleName();

    private IncomeContract.IncomeRecordModel mRecordModel;

    public IncomeRecordPresenter(IncomeContract.IncomeRecordView recordView) {

        mRecordModel = new IncomeRecordModel();
    }

    @Override
    public void requestIncomeRecord() {
        mRecordModel.requestIncomeRecord().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<MyIncomeVO>>() {
                    @Override
                    public void onNext(BaseBean<MyIncomeVO> myIncomeVOBaseBean) {
                        super.onNext(myIncomeVOBaseBean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (myIncomeVOBaseBean.success() && myIncomeVOBaseBean.getResult() != null) {
                            getMVPView().requestIncomeRecordSuccess(myIncomeVOBaseBean.data);
                        } else {
                            onError(new Throwable(myIncomeVOBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMVPView().requestIncomeRecordError(throwable);
                    }

                });
    }
}
