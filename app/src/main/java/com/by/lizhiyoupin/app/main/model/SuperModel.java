package com.by.lizhiyoupin.app.main.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.ShopMdBean;
import com.by.lizhiyoupin.app.io.bean.ShopMindBean;
import com.by.lizhiyoupin.app.io.bean.ShopBannerBean;
import com.by.lizhiyoupin.app.io.bean.SuperAttionBean;
import com.by.lizhiyoupin.app.io.bean.SuperKindBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.main.contract.SuperContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * data:2019/11/23
 * author:jyx
 * function:
 */
public class SuperModel implements SuperContract.SuperModel{
    @Override
    public Observable<BaseBean<List<SuperKindBean>>> requestSuperKind() {
        return ApiService.getSuperApi().requestSuperKind();
    }

    @Override
    public Observable<BaseBean<ShopMindBean>> requestShopKind(int kindId, int minId/*, int pageNo, int pageSize*/) {
        return ApiService.getSuperApi().requestShopKind(kindId,minId/*,pageNo,pageSize*/);
    }

    @Override
    public Observable<BaseBean<SuperAttionBean>> requestFollowShop(int shopId, int followType) {
        return ApiService.getSuperApi().requestFollowShop(shopId,followType);
    }

    @Override
    public Observable<BaseBean<ShopMdBean>> requestShopGoods(int shopId, int minId, /*int pageNo, int pageSize,*/ int sortType, int sortDesc) {
        return ApiService.getSuperApi().requestShopGoods(shopId,minId/*,pageNo,pageSize*/,sortType,sortDesc);
    }

    @Override
    public Observable<BaseBean<List<ShopBannerBean>>> requestSuperBanner() {
        return ApiService.getSuperApi().requestShopBanner();
    }
}
