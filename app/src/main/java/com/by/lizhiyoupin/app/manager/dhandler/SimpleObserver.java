package com.by.lizhiyoupin.app.manager.dhandler;


import com.by.lizhiyoupin.app.common.log.LZLog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SimpleObserver<T> implements Observer<T> {
    private final String tag;

    public SimpleObserver() {
        this(SimpleObserver.class.getSimpleName());
    }

    public SimpleObserver(String tag) {
        this.tag = tag;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        LZLog.e(tag, e.getMessage(), e);
    }

    @Override
    public void onComplete() {

    }
}
