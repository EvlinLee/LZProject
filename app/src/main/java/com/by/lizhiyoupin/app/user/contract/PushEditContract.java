package com.by.lizhiyoupin.app.user.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.PushEditBean;
import com.by.lizhiyoupin.app.io.bean.PushMessageDescBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/3 17:15
 * Summary:
 */
public interface PushEditContract {
    interface IPushEditView extends BaseView {


        void requestPushEditTipsSuccess(int integer);
        void requestPushEditTipsError(Throwable throwable);

        void requestGetPushDetailInfoSuccess(PushEditBean editBean);
        void requestGetPushDetailInfoError(Throwable throwable);

        void requestAddPushDetailInfoSuccess();
        void requestAddPushDetailInfoError(Throwable throwable);

        void requestUpdatePushDetailInfoSuccess();
        void requestUpdatePushDetailInfoError(Throwable throwable);

        void requestPushMessageDescListSuccess(List<PushMessageDescBean> descBeanList);
        void requestPushMessageDescListError(Throwable throwable);
    }

    interface IPushEditModel extends BaseModel {

        Observable<BaseBean<Integer>> requestPushEditTips(String time);

        Observable<BaseBean<PushEditBean>> requestGetPushDetailInfo(long messageId);

        Observable<BaseBean<Object>> requestAddPushDetailInfo(PushEditBean editBean);
        Observable<BaseBean<Object>> requestUpdatePushDetailInfo(PushEditBean editBean);

        Observable<BaseBean<List<PushMessageDescBean>>> requestPushMessageDescList();
    }

    abstract class IPushEditPresenter extends BasePresenter<IPushEditView> {
        /**
         * 获取每天可推送消息数
         * @param time
         */
        public abstract void requestPushEdit(String time);

        /**
         * 获取消息详情
         * @param messageId
         */
        public abstract void requestGetPushDetailInfo(long messageId);

        /**
         * 添加 消息
         * @param editBean
         */
        public abstract void requestAddPushDetailInfo(PushEditBean editBean);

        /**
         * 修改 消息
         * @param editBean
         */
        public abstract void requestUpdatePushDetailInfo(PushEditBean editBean);

        /**
         * 获取 消息文案列表
         */
        public abstract void requestPushMessageDescList();
    }
}
