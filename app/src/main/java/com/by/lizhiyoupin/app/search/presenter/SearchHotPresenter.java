package com.by.lizhiyoupin.app.search.presenter;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.search.contract.SearchContract;
import com.by.lizhiyoupin.app.search.model.SearchHotModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/19 11:00
 * Summary:
 */
public class SearchHotPresenter extends SearchContract.SearchPresenters {
    private  SearchContract.SearchModel mSearchModel;
    public SearchHotPresenter( SearchContract.SearchView searchView) {
        mSearchModel=new SearchHotModel();
    }

    @Override
    public void requestSearchHotList() {
        mSearchModel.requestSearchHotList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<String>>>() {
                    @Override
                    public void onNext(BaseBean<List<String>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (!isViewAttached()){
                            return;
                        }

                        if (listBaseBean.success()&&listBaseBean.data!=null){
                            getMVPView().requestSearchHotListSuccess(listBaseBean.data);
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
                        getMVPView().requestSearchHotListError(throwable);
                    }
                });
    }
}
