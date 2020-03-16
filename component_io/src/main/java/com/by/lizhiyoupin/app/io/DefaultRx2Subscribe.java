package com.by.lizhiyoupin.app.io;

import android.text.TextUtils;

import com.by.lizhiyoupin.app.common.log.LZLog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/5/28 20:17
 * Summary: Rx java2
 */
public abstract class DefaultRx2Subscribe<T> implements Observer<T> {
    private String TAG ;

    public DefaultRx2Subscribe() {
       this("");
    }

    public DefaultRx2Subscribe(String TAG) {
        if (TextUtils.isEmpty(TAG)) {
            TAG = DefaultRx2Subscribe.class.getSimpleName();
        }
        this.TAG = TAG;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable throwable) {
        LZLog.e(TAG, throwable.getMessage(), throwable);
    }

    @Override
    public void onComplete() {

    }
}
