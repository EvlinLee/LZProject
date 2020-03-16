package com.by.lizhiyoupin.app.activity.presenter;

import com.by.lizhiyoupin.app.activity.constract.LimitedTimeConstract;
import com.by.lizhiyoupin.app.activity.model.LimitedTimeModel;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.LimitedTimeSkillTitleBean;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/14 10:10
 * Summary:
 */
public class LimitedTimeSpikePresenter extends LimitedTimeConstract.LimitedTimePresenters {
    private LimitedTimeConstract.LimitedTimeModel mTimeModel;

    public LimitedTimeSpikePresenter(LimitedTimeConstract.LimitedTimeView timeView) {
        mTimeModel = new LimitedTimeModel();
    }

    @Override
    public void requestJLimitedTimeTitle() {
        mTimeModel.requestLimitedTimeTitle().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<LimitedTimeSkillTitleBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<LimitedTimeSkillTitleBean>> bean) {
                        super.onNext(bean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (bean.success() && bean.data != null) {
                            getMVPView().requestLimitedTimeTitleSuccess(bean.getResult());
                        } else {
                            onError(new Throwable(bean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMVPView().requestLimitedTimeTitleError(throwable);
                    }
                });
    }
}
