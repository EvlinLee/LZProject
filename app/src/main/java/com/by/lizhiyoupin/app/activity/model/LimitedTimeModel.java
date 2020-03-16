package com.by.lizhiyoupin.app.activity.model;

import com.by.lizhiyoupin.app.activity.constract.LimitedTimeConstract;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.LimitedTimeSkillTitleBean;
import com.by.lizhiyoupin.app.io.service.ApiService;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/14 10:13
 * Summary:
 */
public class LimitedTimeModel implements LimitedTimeConstract.LimitedTimeModel {


    @Override
    public Observable<BaseBean<List<LimitedTimeSkillTitleBean>>> requestLimitedTimeTitle() {
        return ApiService.getActivityApi().requestLimitedTimeSkillTitleList();
    }
}
