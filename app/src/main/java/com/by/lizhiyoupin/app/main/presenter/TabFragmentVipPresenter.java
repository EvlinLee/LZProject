package com.by.lizhiyoupin.app.main.presenter;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.VipGoodListBean;
import com.by.lizhiyoupin.app.io.bean.VipPowerBean;
import com.by.lizhiyoupin.app.main.contract.TabFragmentVipContract;
import com.by.lizhiyoupin.app.main.model.TabFragmentVipModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/22 17:39
 * Summary:
 */
public class TabFragmentVipPresenter extends TabFragmentVipContract.TabFragmentVipPresenters {
    public static final String TAG=TabFragmentVipPresenter.class.getSimpleName();
    private TabFragmentVipContract.TabFragmentVipModel mVipModel;
    private TabFragmentVipContract.TabFragmentVipView mVipView;

    public TabFragmentVipPresenter(TabFragmentVipContract.TabFragmentVipView vipView) {
        super();
        this.mVipModel=new TabFragmentVipModel();
        this.mVipView=vipView;
    }

    @Override
    public void requestVipPowerList(String phone) {
        mVipModel.requestVipPowerList(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<VipPowerBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<VipPowerBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()){
                            LZLog.i(TAG,"requestVipPowerList success");
                            mVipView.requestVipPowerListSuccess(listBaseBean.data);
                        }else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG,"requestVipPowerList error"+throwable);
                        mVipView.requestVipPowerListError(throwable);
                    }
                });
    }

    @Override
    public void requestVipGoodsList(int start, int limit) {
        mVipModel.requestVipGoodsList(start,limit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<VipGoodListBean>>() {
                    @Override
                    public void onNext(BaseBean<VipGoodListBean> vipGoodListBeanBaseBean) {
                        super.onNext(vipGoodListBeanBaseBean);
                        if (vipGoodListBeanBaseBean.success()){
                            LZLog.i(TAG,"requestVipGoodsList success");
                          //  mVipView.requestVipGoodsListSuccess(vipGoodListBeanBaseBean.data);
                        }else {
                            onError(new Throwable(vipGoodListBeanBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.w(TAG,"requestVipGoodsList onError"+throwable);
                       // mVipView.requestVipGoodsListError(throwable);
                    }
                });
    }
}
