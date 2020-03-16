package com.by.lizhiyoupin.app.main.findcircle.presenter;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.FindCircleChildListBean;
import com.by.lizhiyoupin.app.main.findcircle.contract.DailyChildTabContract;
import com.by.lizhiyoupin.app.main.findcircle.model.DailyChildTabModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/19 17:32
 * Summary:
 */
public class DailyChildTabPresenter extends DailyChildTabContract.DailyChildTabPresenters {

    private DailyChildTabContract.DailyChildTabModel mTabModel;
    private DailyChildTabContract.DailyChildTabView mTabView;

    public DailyChildTabPresenter(DailyChildTabContract.DailyChildTabView tabView) {
        mTabView = tabView;
        mTabModel = new DailyChildTabModel();
    }

    @Override
    public void requestTabChildList(long ringFirstKindId, long ringSecondKindId, int start, int limit) {
        mTabModel.requestTabChildList(ringFirstKindId, ringSecondKindId, start, limit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<FindCircleChildListBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<FindCircleChildListBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()&&listBaseBean.data!=null){
                            mTabView.requestTabChildListSuccess(listBaseBean.data);
                        }else {
                            mTabView.requestTabChildListError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mTabView.requestTabChildListError(throwable);
                    }
                });
    }
}
