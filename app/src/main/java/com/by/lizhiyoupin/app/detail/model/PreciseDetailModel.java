package com.by.lizhiyoupin.app.detail.model;

import com.by.lizhiyoupin.app.common.ContextHolder;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.detail.contract.PreciseDetailContract;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.entity.PreciseDetailEntity;
import com.by.lizhiyoupin.app.io.entity.RequestButtonRecommend;
import com.by.lizhiyoupin.app.io.entity.RequestShoppingCartEntity;
import com.by.lizhiyoupin.app.io.service.ApiService;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/21 17:04
 * Summary:
 */
public class PreciseDetailModel implements PreciseDetailContract.PreciseDetailModel {
    @Override
    public Observable<BaseBean<PreciseDetailEntity>> requestThreePartyProductDetail(Long id,int platformType,Integer batch,
                                                                                    Integer fastBuyCommodityType,int activityType) {
        String uniqueId = DeviceUtil.getImei(ContextHolder.getInstance().getContext());
        return ApiService.getProductDetailApi().requestThreePartyProductDetail(id,platformType,uniqueId,batch,fastBuyCommodityType,activityType);
    }

    @Override
    public Observable<BaseBean<List<String>>> requestThreePartyProductPics(Long goodsId, int platformType) {
        return ApiService.getProductDetailApi().requestThreePartyProductPics(goodsId,platformType);
    }




    @Override
    public Observable<BaseBean<RequestShoppingCartEntity>> requestAdd2ShoppingCartInfo(RequestShoppingCartEntity entity) {
        return ApiService.getShoppingApi().requestAdd2ShoppingCartInfo(entity);
    }

    @Override
    public Observable<BaseBean<Object>> requestDeleteShoppingCartInfo(Long shoppingId) {
        return ApiService.getShoppingApi().requestDeleteShoppingCartInfo(shoppingId);
    }


    @Override
    public Observable<BaseBean<Long>> requestAddButtonRecommendInfo(RequestButtonRecommend recommend) {
        return ApiService.getProductDetailApi().requestAddButtonRecommendInfo(recommend);
    }

    @Override
    public Observable<BaseBean<Object>> requestDeleteButtonRecommendInfo(long recommendId) {
        return  ApiService.getProductDetailApi().requestDeleteButtonRecommendInfo(recommendId);
    }
}
