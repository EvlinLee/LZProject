package com.by.lizhiyoupin.app.main.presenter;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.GuideArticleBean;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;
import com.by.lizhiyoupin.app.io.bean.PreciseSelectionBean;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBean;
import com.by.lizhiyoupin.app.main.contract.PreciseSelectionContract;
import com.by.lizhiyoupin.app.main.model.PreciseSelectionModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/16 10:38
 * Summary: 精选 presenter
 */
public class PreciseSelectionPresenter extends PreciseSelectionContract.PreciseSelectionPresenter {

    private PreciseSelectionContract.PreciseSelectionModel mSelectionModel;
    private PreciseSelectionContract.PreciseSelectionView mSelectionView;

    public PreciseSelectionPresenter(PreciseSelectionContract.PreciseSelectionView view) {
        super();
        mSelectionView = view;
        mSelectionModel = new PreciseSelectionModel();
    }


    @Override
    public void requestGetSelectionChannel() {
        //查询 精选频道 banner icon入口 广告推荐 等数据接口
        mSelectionModel.requestGetSelectionChannel()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<PreciseSelectionBean>>() {
                    @Override
                    public void onNext(BaseBean<PreciseSelectionBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success() && listBaseBean.data != null) {
                            mSelectionView.requestGetSelectionChannelSuccess(listBaseBean.getResult());
                        }else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mSelectionView.requestGetSelectionChannelError(throwable);
                    }
                });
    }

    @Override
    public void requestGetGuideArticleList() {
        mSelectionModel.requestGetGuideArticleList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<GuideArticleBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<GuideArticleBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    @Override
    public void requestGetShakeCouponList(int hourType, int start, int limit, String itemId,int type) {
        mSelectionModel.requestGetShakeCouponList(hourType,start,limit,itemId,type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<ShakeCouponBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<ShakeCouponBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    @Override
    public void requestPreciseSelectionList(int iStart, int iLimit) {
        mSelectionModel.requestPreciseSelectionList(iStart,iLimit).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<PreciseListBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<PreciseListBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }


}
