package com.by.lizhiyoupin.app.main.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.InformationBean;
import com.by.lizhiyoupin.app.io.bean.UserMessageVO;

import java.util.List;

import io.reactivex.Observable;

/**
 * data:2019/11/9
 * author:jyx
 * function:
 */
public interface MessageContract {

    interface  MessageView extends BaseView {
        void requestMessageSuccess(List<UserMessageVO> list);
        void requestMessageError(Throwable throwable);

        void requestMainMessageSuccess(List<InformationBean> list);
        void requestMainMessageError(Throwable throwable);



    }

    interface MessageModel extends BaseModel {
        Observable<BaseBean<List<UserMessageVO>>> requestMessage(int type, int start, int limit);
        Observable<BaseBean<List<InformationBean>>> requestMainMessage();


    }
    abstract class MessagePresenters extends BasePresenter<MessageView> {

        public abstract void requestMessageupdate(int type,int start,int limit);
        public abstract void requestMainMessage();


    }



}
