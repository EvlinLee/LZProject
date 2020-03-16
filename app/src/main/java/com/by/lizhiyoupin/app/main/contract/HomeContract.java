package com.by.lizhiyoupin.app.main.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.CommonCategoryBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/16 11:55
 * Summary:
 */
public interface HomeContract {
    interface TabFragmentHomeView extends BaseView {
        void requestCommodityKindListSuccess(List<CommonCategoryBean> beanList);

        void requestCommodityKindListError(Throwable throwable);
    }

    interface TabFragmentHomeModel extends BaseModel {

        Observable<BaseBean<List<CommonCategoryBean>>> requestCommodityKindList();
    }

    abstract class TabFragmentHomePresenter extends BasePresenter<TabFragmentHomeView> {

        public abstract void requestCommodityKindList();
    }
}
