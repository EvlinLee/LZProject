package com.by.lizhiyoupin.app.main.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.VipGoodListBean;
import com.by.lizhiyoupin.app.io.bean.VipPowerBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.main.contract.TabFragmentVipContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/22 17:40
 * Summary:
 */
public class TabFragmentVipModel implements TabFragmentVipContract.TabFragmentVipModel {
    @Override
    public Observable<BaseBean<List<VipPowerBean>>> requestVipPowerList(final String phone) {
        return ApiService.getVipApi().requestVipPowerList(phone);
    }

    @Override
    public Observable<BaseBean<VipGoodListBean>> requestVipGoodsList(int start,int limit) {
        return ApiService.getVipApi().requestVipGoodsList(start,limit);
    }
}
