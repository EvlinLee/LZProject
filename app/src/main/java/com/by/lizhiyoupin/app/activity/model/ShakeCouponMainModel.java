package com.by.lizhiyoupin.app.activity.model;

import com.by.lizhiyoupin.app.activity.constract.ShakeCouponMainConstract;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBean;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBuyBean;
import com.by.lizhiyoupin.app.io.service.ApiService;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/15 18:06
 * Summary:
 */
public class ShakeCouponMainModel implements ShakeCouponMainConstract.ShakeCouponMainModel {
    @Override
    public Observable<BaseBean<List<ShakeCouponBean>>> requestShakeCouponMainList2(int hourType, int start, int limit, long itemId) {
        return ApiService.getActivityApi().requestShakeCouponList2(hourType, start, limit, itemId);
    }

    @Override
    public Observable<BaseBean<List<ShakeCouponBean>>> requestPreciseShakeCouponList(int hourType, int start, int limit, long itemId, int type) {

        return ApiService.getActivityApi().requestPreciseShakeCouponList(hourType, start, limit, itemId == 0 ? "" : String.valueOf(itemId), type);
    }

    @Override
    public Observable<BaseBean<List<ShakeCouponBuyBean>>> requestShakeCouponBarrageList() {
        return ApiService.getActivityApi().requestShakeCouponBarrageList();
    }
}
