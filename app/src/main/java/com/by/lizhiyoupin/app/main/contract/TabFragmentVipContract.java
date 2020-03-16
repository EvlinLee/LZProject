package com.by.lizhiyoupin.app.main.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.VipGoodListBean;
import com.by.lizhiyoupin.app.io.bean.VipPowerBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/22 17:40
 * Summary:
 */
public interface TabFragmentVipContract {
    interface TabFragmentVipView extends BaseView {
        void requestVipPowerListSuccess(List<VipPowerBean> list);
        void requestVipPowerListError(Throwable throwable);
    }

    interface TabFragmentVipModel extends BaseModel {

        Observable<BaseBean<List<VipPowerBean>>> requestVipPowerList(final String phone);

        Observable<BaseBean<VipGoodListBean>> requestVipGoodsList(final int start,final int limit);
    }

    abstract class TabFragmentVipPresenters extends BasePresenter<TabFragmentVipView> {

        public abstract void requestVipPowerList(final String phone);
        public abstract void requestVipGoodsList(final int start,final int limit);
    }
}
