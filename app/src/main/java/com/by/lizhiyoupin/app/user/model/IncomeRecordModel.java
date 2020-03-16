package com.by.lizhiyoupin.app.user.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.MyIncomeVO;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.user.contract.IncomeContract;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/7 14:13
 * Summary:
 */
public class IncomeRecordModel implements IncomeContract.IncomeRecordModel {
    @Override
    public Observable<BaseBean<MyIncomeVO>> requestIncomeRecord() {
        return ApiService.getIncomeApi().requestIncomeProfitRecord();
    }
}
