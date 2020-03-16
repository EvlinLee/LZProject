package com.by.lizhiyoupin.app.main.findcircle.presenter;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.BusinessIconBean;
import com.by.lizhiyoupin.app.main.findcircle.contract.BusinessContract;
import com.by.lizhiyoupin.app.main.findcircle.model.BusinessSecondListModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/21 10:14
 * Summary:
 */
public class BusinessSecondListPresenter extends BusinessContract.BusinessSecondListPresenters {
    private BusinessContract.BusinessSecondListModel mListModel;

    public BusinessSecondListPresenter(BusinessContract.BusinessSecondListView secondListView) {
        mListModel=new BusinessSecondListModel();
    }

    @Override
    public void requestBusinessSecondTabList(long superiorId) {
        mListModel.requestBusinessSecondTabList(superiorId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<BusinessIconBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<BusinessIconBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (!isViewAttached()){
                            return;
                        }
                        if (listBaseBean.success()&&listBaseBean.getResult()!=null){
                            getMVPView().requestBusinessSecondTabListSuccess(listBaseBean.data);
                        }else {
                            onError(new Throwable(listBaseBean.msg));
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (!isViewAttached()){
                            return;
                        }
                        getMVPView().requestBusinessSecondTabListError(throwable);
                    }
                });
    }
}
