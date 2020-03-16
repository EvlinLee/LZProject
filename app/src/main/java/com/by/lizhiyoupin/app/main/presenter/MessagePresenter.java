package com.by.lizhiyoupin.app.main.presenter;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.InformationBean;
import com.by.lizhiyoupin.app.io.bean.UserMessageVO;
import com.by.lizhiyoupin.app.main.contract.MessageContract;
import com.by.lizhiyoupin.app.main.model.MessageModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * data:2019/11/9
 * author:jyx
 * function:
 */
public class MessagePresenter extends MessageContract.MessagePresenters {

    private MessageContract.MessageModel mMessageModel;
    private MessageContract.MessageView mMessageView;
    public MessagePresenter(MessageContract.MessageView view) {
        this.mMessageView = view;
        mMessageModel = new MessageModel();
    }



    @Override
    public void requestMessageupdate(int type, int start, int limit) {
        mMessageModel.requestMessage(type,start,limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<UserMessageVO>>>() {
                    @Override
                    public void onNext(BaseBean<List<UserMessageVO>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()) {
                            mMessageView.requestMessageSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mMessageView.requestMessageError(throwable);
                    }
                });
    }

    @Override
    public void requestMainMessage() {
        mMessageModel.requestMainMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<InformationBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<InformationBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.success()) {
                            mMessageView.requestMainMessageSuccess(listBaseBean.data);
                        } else {
                            onError(new Throwable(listBaseBean.msg));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mMessageView.requestMainMessageError(throwable);
                    }
                });
    }


}
