package com.by.lizhiyoupin.app.activity.constract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.LimitedTimeSkillTitleBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/14 10:11
 * Summary:
 */
public interface LimitedTimeConstract {
    interface LimitedTimeView extends BaseView {

        void requestLimitedTimeTitleSuccess(List<LimitedTimeSkillTitleBean> timeTitleBean);
        void requestLimitedTimeTitleError(Throwable throwable);
    }


    interface  LimitedTimeModel extends BaseModel {

        Observable<BaseBean<List<LimitedTimeSkillTitleBean>>> requestLimitedTimeTitle();
    }

    abstract class LimitedTimePresenters extends BasePresenter<LimitedTimeView> {

        public abstract void requestJLimitedTimeTitle();
    }
}
