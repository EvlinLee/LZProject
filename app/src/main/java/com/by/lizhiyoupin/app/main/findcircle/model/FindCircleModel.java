package com.by.lizhiyoupin.app.main.findcircle.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.FindCircleTabListBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.main.findcircle.contract.FindCircleContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/19 15:02
 * Summary:
 */
public class FindCircleModel implements FindCircleContract.FindCircleModel {
    @Override
    public Observable<BaseBean<List<FindCircleTabListBean>>> requestTabFirstList() {
        return ApiService.getFindCircleApi().requestFindCircleTabList();
    }

    @Override
    public Observable<BaseBean<List<FindCircleTabListBean>>> requestTabSecondList(long superiorId) {
        return ApiService.getFindCircleApi().requestTabSecondList(superiorId);
    }
}
