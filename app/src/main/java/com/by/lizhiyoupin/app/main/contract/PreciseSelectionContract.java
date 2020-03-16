package com.by.lizhiyoupin.app.main.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.GuideArticleBean;
import com.by.lizhiyoupin.app.io.bean.PreciseListBean;
import com.by.lizhiyoupin.app.io.bean.PreciseSelectionBean;
import com.by.lizhiyoupin.app.io.bean.ShakeCouponBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/16 10:41
 * Summary: 精选 Contract
 */
public interface PreciseSelectionContract {

    interface PreciseSelectionView extends BaseView {
        void requestGetSelectionChannelSuccess(PreciseSelectionBean bean);

        void requestGetSelectionChannelError(Throwable throwable);

        void requestGetGuideArticleListSuccess(BaseBean<List<GuideArticleBean>> bean);

        void requestGetGuideArticleListError(Throwable throwable);


        void requestGetShakeCouponListSuccess(BaseBean<List<ShakeCouponBean>> bean);

        void requestGetShakeCouponListError(Throwable throwable);


        void requestPreciseSelectionListSuccess(BaseBean<List<PreciseListBean>> bean);

        void requestPreciseSelectionListError(Throwable throwable);
    }


    interface PreciseSelectionModel extends BaseModel {

        Observable<BaseBean<PreciseSelectionBean>> requestGetSelectionChannel();

        Observable<BaseBean<List<GuideArticleBean>>> requestGetGuideArticleList();

        Observable<BaseBean<List<ShakeCouponBean>>> requestGetShakeCouponList(int hourType, int start, int limit, String itemId,int type);

        Observable<BaseBean<List<PreciseListBean>>> requestPreciseSelectionList(int iStart, int iLimit);
    }

    abstract class PreciseSelectionPresenter extends BasePresenter<PreciseSelectionContract.PreciseSelectionView> {

        /**
         * 查询 精选频道 banner icon入口 广告推荐 等数据接口
         */
        public abstract void requestGetSelectionChannel();

        public abstract void requestGetGuideArticleList();

        public abstract void requestGetShakeCouponList(int hourType, int start, int limit, String itemId,int type);

        public abstract void requestPreciseSelectionList(int iStart, int iLimit);
    }
}
