package com.by.lizhiyoupin.app.main.model;

import com.by.lizhiyoupin.app.common.ContextHolder;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.GuideArticleBean;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;
import com.by.lizhiyoupin.app.io.bean.PreciseSelectionBean;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.main.contract.PreciseSelectionContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/16 10:51
 * Summary:  精选 model
 */
public class PreciseSelectionModel implements PreciseSelectionContract.PreciseSelectionModel {
    @Override
    public Observable<BaseBean<PreciseSelectionBean>> requestGetSelectionChannel() {
        return ApiService.getHomeApi().requestSelectionChannel();
    }

    @Override
    public Observable<BaseBean<List<GuideArticleBean>>> requestGetGuideArticleList() {
        return ApiService.getHomeApi().requestGetGuideArticleList();
    }

    @Override
    public Observable<BaseBean<List<ShakeCouponBean>>> requestGetShakeCouponList(int hourType, int start, int limit, String itemId,int type) {
        return ApiService.getActivityApi().requestPreciseShakeCouponList(hourType,start,limit,itemId,type);
    }

    @Override
    public Observable<BaseBean<List<PreciseListBean>>> requestPreciseSelectionList( int iStart, int iLimit) {
        String uniqueId = DeviceUtil.getImei(ContextHolder.getInstance().getContext());
       return ApiService.getHomeApi().requestPreciseSelectionList(iStart, iLimit, uniqueId);
    }


}
