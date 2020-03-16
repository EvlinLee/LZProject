package com.by.lizhiyoupin.app.detail.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;
import com.by.lizhiyoupin.app.io.entity.DetailGuessYouLikeEntity;
import com.by.lizhiyoupin.app.io.entity.DetailRecommendationEntity;
import com.by.lizhiyoupin.app.io.entity.ProductDetailEntity;
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
public interface ProductDetailContract {
    interface ProductDetailView extends BaseView {
        void requestLocalProductDetailSuccess(ProductDetailEntity entity);
        void requestLocalProductDetailError(Throwable throwable);

        void requestThreePartyProductDecPicSuccess(List<String> entity);
        void requestThreePartyProductDecPicError(Throwable throwable);

        void requestDetailRecommendationSuccess(List<PreciseListBean> entity);
        void requestDetailRecommendationError(Throwable throwable);

        void requestDetailGuessYouLikeSuccess(List<PreciseListBean> entity);
        void requestDetailGuessYouLikeError(Throwable throwable);

        void requestDeleteShoppingCartInfoSuccess(Boolean b);
        void requestDeleteShoppingCartInfoError(Throwable throwable);

        void requestAdd2ShoppingCartInfoSuccess(RequestShoppingCartEntity b);
        void requestAdd2ShoppingCartInfoError(Throwable throwable);

    }

    interface ProductDetailModel extends BaseModel {
        Observable<BaseBean<ProductDetailEntity>> requestLocalProductDetail(Long commodityId);
        Observable<BaseBean<DetailRecommendationEntity>> requestDetailRecommendation(String keyWord, int platformType);
        Observable<BaseBean<DetailGuessYouLikeEntity>> requestDetailGuessYouLike(String goodsId);
        Observable<BaseBean<Object>> requestDeleteShoppingCartInfo(Long shoppingId);
        Observable<BaseBean<RequestShoppingCartEntity>> requestAdd2ShoppingCartInfo(RequestShoppingCartEntity entity);
    }

    abstract class ProductDetailPresenters extends BasePresenter<ProductDetailView> {
        public abstract void requestLocalProductDetail(Long commodityId,int platformType);
        public abstract void requestDetailRecommendation(String keyWord,int platformType);
        public abstract void requestDetailGuessYouLike(String goodsId);
        public abstract void requestThreePartyProductDecPic(long detailId);

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
