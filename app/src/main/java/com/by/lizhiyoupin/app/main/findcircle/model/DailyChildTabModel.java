package com.by.lizhiyoupin.app.main.findcircle.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.FindCircleChildListBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.main.findcircle.contract.DailyChildTabContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/19 17:58
 * Summary:
 */
public class DailyChildTabModel implements DailyChildTabContract.DailyChildTabModel {

    @Override
    public Observable<BaseBean<List<FindCircleChildListBean>>> requestTabChildList(long ringFirstKindId, long ringSecondKindId, int start, int limit) {
        return ApiService.getFindCircleApi().requestTabChildList(ringFirstKindId,ringSecondKindId,start,limit);

    }
}
