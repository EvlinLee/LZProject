package com.by.lizhiyoupin.app.main.presenter;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.ShopMdBean;
import com.by.lizhiyoupin.app.io.bean.ShopMindBean;
import com.by.lizhiyoupin.app.io.bean.ShopBannerBean;
import com.by.lizhiyoupin.app.io.bean.SuperAttionBean;
import com.by.lizhiyoupin.app.io.bean.SuperKindBean;
import com.by.lizhiyoupin.app.main.contract.SuperContract;
import com.by.lizhiyoupin.app.main.model.SuperModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * data:2019/11/23
 * author:jyx
 * function:
 */
public class SuperPresenter extends SuperContract.SuperPresenters {

    private SuperContract.SuperModel mSuperModel;
    private SuperContract.SuperView mSuperView;
    public SuperPresenter(SuperContract.SuperView view) {
        this.mSuperView = view;
        mSuperModel = new SuperModel();
    }

    @Override
    public void requestSuperKind() {
        mSuperModel.requestSuperKind()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<SuperKindBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<SuperKindBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()) {
                            mSuperView.requestSuperKindSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mSuperView.requestSuperKindError(throwable);
                    }
                });
    }

    @Override
    public void requestShopKind(int kindId,int minid/*,int pageNo,int pageSize*/) {
        mSuperModel.requestShopKind(kindId,minid/*,pageNo,pageSize*/)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<ShopMindBean>>() {
                    @Override
                    public void onNext(BaseBean<ShopMindBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()) {
                            mSuperView.requestShopKindSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mSuperView.requestShopKindError(throwable);
                    }
                });
    }

    @Override
    public void requestFollowShop(int shopId,int followType) {
        mSuperModel.requestFollowShop(shopId,followType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<SuperAttionBean>>() {
                    @Override
                    public void onNext(BaseBean<SuperAttionBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()) {
                            mSuperView.requestFollowShopSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mSuperView.requestFollowShopError(throwable);
                    }
                });
    }

    @Override
    public void requestShopGoods(int shopId,int minId/*, int pageNo, int pageSize*/, int sortType, int sortDesc) {
        mSuperModel.requestShopGoods(shopId,minId,/*pageNo,pageSize,*/sortType,sortDesc)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<ShopMdBean>>() {
                    @Override
                    public void onNext(BaseBean<ShopMdBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()) {
                            mSuperView.requestShopGoodsSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mSuperView.requestShopGoodsError(throwable);
                    }
                });
    }

    @Override
    public void requestSuperBanner() {
        mSuperModel.requestSuperBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<ShopBannerBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<ShopBannerBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()) {
                            mSuperView.requestShopBannerSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mSuperView.requestShopBannerError(throwable);
                    }
                });
    }
}
