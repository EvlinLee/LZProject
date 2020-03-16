package com.by.lizhiyoupin.app.detail.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.entity.PreciseDetailEntity;
import com.by.lizhiyoupin.app.io.entity.RequestButtonRecommend;
import com.by.lizhiyoupin.app.io.entity.RequestShoppingCartEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/21 17:00
 * Summary:
 */
public interface PreciseDetailContract {
    interface PreciseDetailView extends BaseView {
        void requestThreePartyProductDetailSuccess(PreciseDetailEntity entity);
        void requestThreePartyProductDetailNoData(String msg);
        void requestThreePartyProductDetailNoButJump(PreciseDetailEntity entity);

        void requestThreePartyProductDetailError(String code,Throwable throwable);

        void requestThreePartyProductDecPicSuccess(List<String> entity);

        void requestThreePartyProductDecPicError(Throwable throwable);

        void requestAddButtonRecommendInfoSuccess(Long recommendId);
        void requestAddButtonRecommendInfoError(Throwable throwable);

        void requestDeleteButtonRecommendInfoSuccess(Boolean b);
        void requestDeleteButtonRecommendInfoError(Throwable throwable);

        void requestDeleteShoppingCartInfoSuccess(Boolean b);
        void requestDeleteShoppingCartInfoError(Throwable throwable);

        void requestAdd2ShoppingCartInfoSuccess(RequestShoppingCartEntity b);
        void requestAdd2ShoppingCartInfoError(Throwable throwable);
    }

    interface PreciseDetailModel extends BaseModel {
        Observable<BaseBean<PreciseDetailEntity>> requestThreePartyProductDetail(Long goodsId,int platformType, Integer batch,
                                                                                 Integer fastBuyCommodityType,int activityType);
        Observable<BaseBean<List<String>>> requestThreePartyProductPics(Long goodsId,int platformType);


        Observable<BaseBean<Object>> requestDeleteShoppingCartInfo(Long shoppingId);
        Observable<BaseBean<RequestShoppingCartEntity>> requestAdd2ShoppingCartInfo(RequestShoppingCartEntity entity);

        Observable<BaseBean<Long>> requestAddButtonRecommendInfo(RequestButtonRecommend recommend);
        Observable<BaseBean<Object>> requestDeleteButtonRecommendInfo(long recommendId);
    }

    abstract class PreciseDetailPresenters extends BasePresenter<PreciseDetailView> {
        public abstract void requestThreePartyProductDetail(Long id,int platformType,Integer batch,
                                                            Integer fastBuyCommodityType,int activityType);
        public abstract void requestThreePartyProductDecPic(Long goodsId,int platformType);

        /**
         * 加入推荐列表
         * @param recommend
         */
        public abstract void requestAddButtonRecommendInfo(RequestButtonRecommend recommend);

        /**
         * 从推荐列表删除
         * @param recommendId
         */
        public abstract void requestDeleteButtonRecommendInfo(long recommendId);



        /**
         * 从购物车删除
         * @param shoppingId
         */
        public abstract void requestDeleteShoppingCartInfo(Long shoppingId);

        /**
         * 加入购物车
         * @param entity
         */
        public abstract void requestAdd2ShoppingCartInfo(RequestShoppingCartEntity entity);
    }
}
