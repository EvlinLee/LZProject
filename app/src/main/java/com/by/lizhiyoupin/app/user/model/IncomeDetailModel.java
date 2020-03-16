package com.by.lizhiyoupin.app.user.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.IncomeDetailsVO;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.user.contract.IncomeContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/7 15:45
 * Summary:
 */
public class IncomeDetailModel implements IncomeContract.IncomeDetailModel {
    @Override
    public Observable<BaseBean<List<IncomeDetailsVO>>> requestIncomeDetail(int desc, int start, int limit) {
        return ApiService.getIncomeApi().requestIncomeDetail(desc,start,limit,0,0,0);
    }
}
