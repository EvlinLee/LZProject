package com.by.lizhiyoupin.app.main.findcircle.presenter;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.LzTransformationUtil;
import com.by.lizhiyoupin.app.io.bean.BusinessArticleBean;
import com.by.lizhiyoupin.app.io.bean.BusinessIconBean;
import com.by.lizhiyoupin.app.io.bean.HomeBannerBean;
import com.by.lizhiyoupin.app.io.bean.PreciseBannerIconBean;
import com.by.lizhiyoupin.app.main.findcircle.contract.BusinessContract;
import com.by.lizhiyoupin.app.main.findcircle.model.BusinessModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/20 17:29
 * Summary:
 */
public class BusinessPresenter  extends BusinessContract.BusinessPresenters {
    private BusinessContract.BusinessModel mBusinessModel;
    private BusinessContract.BusinessView mBusinessView;

    public BusinessPresenter(BusinessContract.BusinessView businessView) {
        mBusinessModel=new BusinessModel();
        mBusinessView=businessView;
    }

    @Override
    public void requestBusinessGuessList(int start, int limit) {
        mBusinessModel.requestBusinessGuessList(start,limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<BusinessArticleBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<BusinessArticleBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()){
                            //这个不要判空，留给内部判空 来区分是否有加载更多
                            mBusinessView.requestBusinessGuessListSuccess(listBaseBean.data,start);
                        }else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mBusinessView.requestBusinessGuessListError(throwable,start);
                    }
                });
    }

    @Override
    public void requestBusinessScrollArticleList(int start, int limit) {
        mBusinessModel.requestBusinessScrollArticleList(start,limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<BusinessArticleBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<BusinessArticleBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()&&listBaseBean.getResult()!=null){
                            mBusinessView.requestBusinessScrollArticleListSuccess(listBaseBean.data);
                        }else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mBusinessView.requestBusinessScrollArticleListError(throwable);
                    }
                });
    }

    @Override
    public void requestBusinessBannerList() {
        mBusinessModel.requestBusinessBannerList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<PreciseBannerIconBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<PreciseBannerIconBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()&&listBaseBean.getResult()!=null){
                            List<HomeBannerBean> homeIconBeans = LzTransformationUtil.transformationBanner(listBaseBean.data);
                            mBusinessView.requestBusinessBannerListSuccess(homeIconBeans);
                        }else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mBusinessView.requestBusinessBannerListError(throwable);
                    }
                });
    }

    @Override
    public void requestBusinessIconList() {
        mBusinessModel.requestBusinessIconList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<BusinessIconBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<BusinessIconBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()&&listBaseBean.getResult()!=null){
                              mBusinessView.requestBusinessIconListSuccess(listBaseBean.data);
                        }else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mBusinessView.requestBusinessIconListError(throwable);
                    }
                });
    }
}
