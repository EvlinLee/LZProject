package com.by.lizhiyoupin.app.main.findcircle.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.FindCircleTabListBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/19 14:59
 * Summary:
 */
public interface FindCircleContract {
    interface FindCircleView  extends BaseView{
        void requestFirstLevelListSuccess(List<FindCircleTabListBean> beanList);

        void requestFirstLevelListError(Throwable throwable);
        void requestSecondLevelListSuccess(List<FindCircleTabListBean> beanList);

        void requestSecondLevelListError(Throwable throwable);
    }
    interface FindCircleModel  extends BaseModel {
        Observable<BaseBean<List<FindCircleTabListBean>>> requestTabFirstList();
        Observable<BaseBean<List<FindCircleTabListBean>>> requestTabSecondList(long superiorId);
    }
    abstract class FindCirclelPresenters extends BasePresenter<FindCircleView> {
        /**
         *  查询发圈一级类目数据
         */
        public abstract void requestFirstLevelList( );

        /**
         * 查询发圈二级类目数据
         */
        public abstract void requestSecondLevelList(long superiorId );
    }
}
