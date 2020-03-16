package com.by.lizhiyoupin.app.main.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.ShopMdBean;
import com.by.lizhiyoupin.app.io.bean.ShopMindBean;
import com.by.lizhiyoupin.app.io.bean.ShopBannerBean;
import com.by.lizhiyoupin.app.io.bean.SuperAttionBean;
import com.by.lizhiyoupin.app.io.bean.SuperKindBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * data:2019/11/23
 * author:jyx
 * function:
 */
public interface SuperContract {
    interface  SuperView extends BaseView {
        void requestSuperKindSuccess(List<SuperKindBean> list);
        void requestSuperKindError(Throwable throwable);

        void requestShopKindSuccess(ShopMindBean bean);
        void requestShopKindError(Throwable throwable);

        void requestFollowShopSuccess(SuperAttionBean bean);
        void requestFollowShopError(Throwable throwable);

        void requestShopGoodsSuccess(ShopMdBean bean);
        void requestShopGoodsError(Throwable throwable);


        void requestShopBannerSuccess(List<ShopBannerBean> list);
        void requestShopBannerError(Throwable throwable);


    }

    interface SuperModel extends BaseModel {
        Observable<BaseBean<List<SuperKindBean>>> requestSuperKind();
        Observable<BaseBean<ShopMindBean>> requestShopKind(int kindId,int minId/*,int pageNo,int pageSize*/);
        Observable<BaseBean<SuperAttionBean>> requestFollowShop(int shopId,int followType);
        Observable<BaseBean<ShopMdBean>> requestShopGoods(int shopId,int minId/*, int pageNo, int pageSize*/,int sortType, int sortDesc);
        Observable<BaseBean<List<ShopBannerBean>>> requestSuperBanner();


    }
    abstract class SuperPresenters extends BasePresenter<SuperContract.SuperView> {

        public abstract void requestSuperKind();
        public abstract void requestShopKind(int kindId,int minId/*,int pageNo,int pageSize*/);
        public abstract void requestFollowShop(int shopId,int followType);
        public abstract void requestShopGoods(int shopId,int minId,/* int pageNo, int pageSize,*/int sortType, int sortDesc);
        public abstract void requestSuperBanner();


    }
}
