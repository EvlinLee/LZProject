package com.by.lizhiyoupin.app.main.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.CommonSecondBean;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/16 17:23
 * Summary:
 */
public interface HomeCommonContract {
    interface HomeCommonView extends BaseView {
        void requestSecondBannerIconSuccess(CommonSecondBean  secondBean);

        void requestSecondBannerIconError(Throwable throwable);

    }

    interface HomeCommonModel extends BaseModel {
        /**
         * 查询其他频道二级类目 banner+icon数据
         * @return
         */
        Observable<BaseBean<CommonSecondBean>> requestSecondBannerIcon(Long id);

    }

    abstract class HomeCommonPresenter extends BasePresenter<HomeCommonView> {
        /**
         *  查询首页其他类目 的banner+icon
         * @param id 当前tab类目的id
         */
        public abstract void requestSecondBannerIcon(Long id);

    }
}
