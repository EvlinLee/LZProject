package com.by.lizhiyoupin.app.main.findcircle.presenter;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.FindCircleTabListBean;
import com.by.lizhiyoupin.app.main.findcircle.contract.FindCircleContract;
import com.by.lizhiyoupin.app.main.findcircle.model.FindCircleModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/19 15:12
 * Summary: 发圈presenter
 */
public class FindCirclelPresenter extends FindCircleContract.FindCirclelPresenters {
    private FindCircleContract.FindCircleModel mCircleModel;
    private FindCircleContract.FindCircleView mCircleView;
    public FindCirclelPresenter(FindCircleContract.FindCircleView circleView) {
        mCircleModel = new FindCircleModel();
        mCircleView=circleView;
    }

    @Override
    public void requestFirstLevelList() {
        mCircleModel.requestTabFirstList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<FindCircleTabListBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<FindCircleTabListBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()&&listBaseBean.data!=null){
                            mCircleView.requestFirstLevelListSuccess(listBaseBean.data);
                        }else {
                            onError(new Throwable(listBaseBean.msg));
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mCircleView.requestFirstLevelListError(throwable);
                    }
                });
    }

    @Override
    public void requestSecondLevelList(long superiorId) {
        mCircleModel.requestTabSecondList(superiorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<FindCircleTabListBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<FindCircleTabListBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()&&listBaseBean.data!=null){
                            mCircleView.requestSecondLevelListSuccess(listBaseBean.data);
                        }else {
                            onError(new Throwable(listBaseBean.msg));
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mCircleView.requestSecondLevelListError(throwable);
                    }
                });
    }
}
