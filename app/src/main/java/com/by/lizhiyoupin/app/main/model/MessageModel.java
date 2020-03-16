package com.by.lizhiyoupin.app.main.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.InformationBean;
import com.by.lizhiyoupin.app.io.bean.UserMessageVO;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.main.contract.MessageContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * data:2019/11/9
 * author:jyx
 * function:
 */
public class MessageModel implements MessageContract.MessageModel {

    @Override
    public Observable<BaseBean<List<UserMessageVO>>> requestMessage(int type, int start, int limit) {
        return ApiService.getMessageApi().requestUserMessage(type,start,limit);
    }

    @Override
    public Observable<BaseBean<List<InformationBean>>> requestMainMessage() {
        return ApiService.getMessageApi().requestMainMessage();
    }

}
