package com.by.lizhiyoupin.app.detail.model;

import com.by.lizhiyoupin.app.common.ContextHolder;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.detail.contract.ProductDetailContract;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.entity.DetailGuessYouLikeEntity;
import com.by.lizhiyoupin.app.io.entity.DetailRecommendationEntity;
import com.by.lizhiyoupin.app.io.entity.ProductDetailEntity;
import com.by.lizhiyoupin.app.io.entity.RequestShoppingCartEntity;
import com.by.lizhiyoupin.app.io.service.ApiService;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/21 17:04
 * Summary:
 */
public class ProductDetailModel implements ProductDetailContract.ProductDetailModel {

    @Override
    public Observable<BaseBean<ProductDetailEntity>> requestLocalProductDetail(Long commodityId) {
        String uniqueId = DeviceUtil.getImei(ContextHolder.getInstance().getContext());
        return ApiService.getProductDetailApi().requestLocalProductDetail(commodityId,uniqueId);
    }

    @Override
    public Observable<BaseBean<DetailRecommendationEntity>> requestDetailRecommendation(String keyWord, int platformType) {
        String uniqueId = DeviceUtil.getImei(ContextHolder.getInstance().getContext());

        return ApiService.getProductDetailApi().requestDetailRecommendation(keyWord,platformType,uniqueId);
    }

    @Override
    public Observable<BaseBean<DetailGuessYouLikeEntity>> requestDetailGuessYouLike(String goodsId) {
        String uniqueId = DeviceUtil.getImei(ContextHolder.getInstance().getContext());
        return ApiService.getProductDetailApi().requestDetailGuessYouLike(goodsId,uniqueId);
    }
    @Override
    public Observable<BaseBean<RequestShoppingCartEntity>> requestAdd2ShoppingCartInfo(RequestShoppingCartEntity entity) {
        return ApiService.getShoppingApi().requestAdd2ShoppingCartInfo(entity);
    }

    @Override
    public Observable<BaseBean<Object>> requestDeleteShoppingCartInfo(Long shoppingId) {
        return ApiService.getShoppingApi().requestDeleteShoppingCartInfo(shoppingId);
    }
}
