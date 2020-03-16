package com.by.lizhiyoupin.app.detail.presenter;

import com.by.lizhiyoupin.app.detail.contract.PreciseDetailContract;
import com.by.lizhiyoupin.app.detail.model.PreciseDetailModel;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.entity.PreciseDetailEntity;
import com.by.lizhiyoupin.app.io.entity.RequestButtonRecommend;
import com.by.lizhiyoupin.app.io.entity.RequestShoppingCartEntity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/21 16:57
 * Summary: 商详 presenter
 */
public class PreciseDetailPresenter extends PreciseDetailContract.PreciseDetailPresenters {
    private PreciseDetailContract.PreciseDetailModel mDetailModel;
    private PreciseDetailContract.PreciseDetailView mDetailView;

    public PreciseDetailPresenter(PreciseDetailContract.PreciseDetailView view) {
        super();
        mDetailModel = new PreciseDetailModel();
        this.mDetailView = view;
    }

    @Override
    public void requestThreePartyProductDetail(Long goodsId, int platformType,Integer batch,Integer fastBuyCommodityType,int activityType) {
        mDetailModel.requestThreePartyProductDetail(goodsId, platformType,batch,fastBuyCommodityType,activityType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<PreciseDetailEntity>>() {
                    @Override
                    public void onNext(BaseBean<PreciseDetailEntity> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.getResult() != null) {
                            mDetailView.requestThreePartyProductDetailSuccess(listBaseBean.data);
                        } else if (BaseBean.CODE_ERROR.equals(listBaseBean.code)) {
                            mDetailView.requestThreePartyProductDetailNoData(listBaseBean.msg);
                        } else if (BaseBean.CODE_PRODUCT_NO_ERROR.equals(listBaseBean.code)){
                            mDetailView.requestThreePartyProductDetailError(listBaseBean.code,new Throwable(listBaseBean.msg));
                        }else if (BaseBean.CODE_PRODUCT_NO_BUT_JUMP_ERROR.equals(listBaseBean.code)&&listBaseBean.getResult()!=null){
                            mDetailView.requestThreePartyProductDetailNoButJump(listBaseBean.data);
                        }else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mDetailView.requestThreePartyProductDetailError(BaseBean.CODE_FAIL,throwable);
                    }
                });
    }

    @Override
    public void requestThreePartyProductDecPic(Long goodsId, int platformType) {
        mDetailModel.requestThreePartyProductPics(goodsId, platformType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<String>>>() {
                    @Override
                    public void onNext(BaseBean<List<String>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.getResult() != null) {
                            mDetailView.requestThreePartyProductDecPicSuccess(listBaseBean.data);
                        } else {
                            mDetailView.requestThreePartyProductDecPicError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mDetailView.requestThreePartyProductDecPicError(throwable);
                    }
                });

    }


    @Override
    public void requestDeleteShoppingCartInfo(Long shoppingId) {
        mDetailModel.requestDeleteShoppingCartInfo(shoppingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        mDetailView.requestDeleteShoppingCartInfoSuccess(baseBean.success());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mDetailView.requestDeleteShoppingCartInfoError(throwable);

                    }
                });
    }

    @Override
    public void requestAdd2ShoppingCartInfo(RequestShoppingCartEntity entity) {
        mDetailModel.requestAdd2ShoppingCartInfo(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<RequestShoppingCartEntity>>() {
                    @Override
                    public void onNext(BaseBean<RequestShoppingCartEntity> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.success() && baseBean.getResult() != null) {
                            mDetailView.requestAdd2ShoppingCartInfoSuccess(baseBean.getResult());
                        } else {
                            onError(new Throwable(baseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mDetailView.requestAdd2ShoppingCartInfoError(throwable);

                    }
                });
    }

    @Override
    public void requestAddButtonRecommendInfo(RequestButtonRecommend recommend) {
        mDetailModel.requestAddButtonRecommendInfo(recommend)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<Long>>() {
                    @Override
                    public void onNext(BaseBean<Long> longBaseBean) {
                        super.onNext(longBaseBean);
                        if (longBaseBean.success()){
                            mDetailView.requestAddButtonRecommendInfoSuccess(longBaseBean.data);
                        }else {
                            onError(new Throwable(longBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mDetailView.requestAddButtonRecommendInfoError(throwable);
                    }
                });
    }

    @Override
    public void requestDeleteButtonRecommendInfo(long recommendId) {
        mDetailModel.requestDeleteButtonRecommendInfo(recommendId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> bean) {
                        super.onNext(bean);
                        if (bean.success()){
                            mDetailView.requestDeleteButtonRecommendInfoSuccess(true);
                        }else {
                            onError(new Throwable(bean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mDetailView.requestDeleteButtonRecommendInfoError(throwable);
                    }
                });
    }
}
