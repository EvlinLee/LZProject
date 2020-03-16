package com.by.lizhiyoupin.app.user.presenter;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.IncomeDetailsVO;
import com.by.lizhiyoupin.app.user.contract.IncomeContract;
import com.by.lizhiyoupin.app.user.model.IncomeDetailModel;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/7 15:45
 * Summary:
 */
public class IncomeDetailPresenter extends IncomeContract.IncomeDetailPresenters {
    public static final String TAG = IncomeDetailPresenter.class.getSimpleName();

    private IncomeContract.IncomeDetailModel mDetailModel;
    private WeakReference<IncomeContract.IncomeDetailView> mReference;

    public IncomeDetailPresenter(IncomeContract.IncomeDetailView recordView) {
        mReference = new WeakReference<>(recordView);
        mDetailModel = new IncomeDetailModel();
    }
    @Override
    public void requestIncomeDetail(int desc, int start, int limit) {
        mDetailModel.requestIncomeDetail(desc,start,limit).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<IncomeDetailsVO>>>() {
                    @Override
                    public void onNext(BaseBean<List<IncomeDetailsVO>> baseBean) {
                        super.onNext(baseBean);
                        if (mReference.get() == null) {
                            return;
                        }
                        if (baseBean.success() && baseBean.getResult() != null) {
                            mReference.get().requestIncomeDetailSuccess(baseBean.data);
                        } else {
                            onError(new Throwable(baseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (mReference.get() == null) {
                            return;
                        }
                        mReference.get().requestIncomeDetailError(throwable);
                    }
                });
    }
}
