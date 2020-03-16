package com.by.lizhiyoupin.app.main.findcircle.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.BusinessArticleBean;
import com.by.lizhiyoupin.app.io.bean.BusinessIconBean;
import com.by.lizhiyoupin.app.io.bean.HomeBannerBean;
import com.by.lizhiyoupin.app.io.bean.PreciseBannerIconBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/20 17:30
 * Summary: 商学院
 */
public interface BusinessContract {
    interface BusinessView extends BaseView {
        void requestBusinessBannerListSuccess(List<HomeBannerBean> beanList);

        void requestBusinessBannerListError(Throwable throwable);

        void requestBusinessIconListSuccess(List<BusinessIconBean> beanList);

        void requestBusinessIconListError(Throwable throwable);

        void requestBusinessScrollArticleListSuccess(List<BusinessArticleBean> beanList);

        void requestBusinessScrollArticleListError(Throwable throwable);

        void requestBusinessGuessListSuccess(List<BusinessArticleBean> beanList,int page);

        void requestBusinessGuessListError(Throwable throwable,int page);
    }

    interface BusinessModel extends BaseModel {

        Observable<BaseBean<List<PreciseBannerIconBean>>> requestBusinessBannerList();

        Observable<BaseBean<List<BusinessIconBean>>> requestBusinessIconList();

        Observable<BaseBean<List<BusinessArticleBean>>> requestBusinessScrollArticleList(int start, int limit);

        Observable<BaseBean<List<BusinessArticleBean>>> requestBusinessGuessList(int start, int limit);

    }

    abstract class BusinessPresenters extends BasePresenter<BusinessView> {
        public abstract void requestBusinessBannerList();

        public abstract void requestBusinessIconList();
        /**
         * 查询 为你推荐商学院文章  滚动资讯
         */
        public abstract void requestBusinessScrollArticleList(int start, int limit);

        public abstract void requestBusinessGuessList(int start, int limit);
    }

    //商学院二级列表相关

    interface BusinessSecondListView extends BaseView {
        void requestBusinessSecondTabListSuccess(List<BusinessIconBean> beanList);

        void requestBusinessSecondTabListError(Throwable throwable);
    }

    interface BusinessSecondListModel extends BaseModel {

        Observable<BaseBean<List<BusinessIconBean>>> requestBusinessSecondTabList(long superiorId);

    }

    abstract class BusinessSecondListPresenters extends BasePresenter<BusinessSecondListView> {

        public abstract void requestBusinessSecondTabList(long superiorId);
    }

}
