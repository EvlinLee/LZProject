package com.by.lizhiyoupin.app.loader;


import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 创建时间：2016/9/10
 *
 * @author wzm
 */
public class LoadListHelperRx<T> {

    List<T> data;
    ILoadProgressView<T> mView;
    IEmptyView mEmptyView;
    Loader<T> loader;
    SingleLoader<T> singleLoader;
    Disposable loadDisposable;

    public LoadListHelperRx() {
    }

    public LoadListHelperRx(ILoadProgressView<T> mView, IEmptyView emptyView, Loader<T> loader) {
        this.mView = mView;
        this.mEmptyView = emptyView;
        this.loader = loader;
    }

    public void loadData() {
        mView.showRefreshing(true);
        if (loader != null) {
            loader.load().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Collection<T>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            loadDisposable = d;
                        }

                        @Override
                        public void onComplete() {
                            doOnComplete();
                        }

                        @Override
                        public void onError(Throwable e) {
                            onOnError(e);
                        }

                        @Override
                        public void onNext(Collection<T> data) {
                            doOnNext(data);
                        }
                    });
        } else if (singleLoader != null) {
            singleLoader.load().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Collection<T>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            loadDisposable = d;
                        }

                        @Override
                        public void onSuccess(Collection<T> data) {
                            doOnNext(data);
                            doOnComplete();
                        }

                        @Override
                        public void onError(Throwable e) {
                            onOnError(e);
                        }
                    });
        } else {
            onOnError(new NullPointerException("LoadListHelperRx loader is null"));
        }
    }

    private void doOnNext(Collection<T> data) {
        LoadListHelperRx.this.data = (List<T>) data;
        mView.showData(LoadListHelperRx.this.data, ILoadProgressView.DataChangeType.CHANGE, 0, data == null ? 0 : data.size(), false);
        tryShowEmpty(LoadType.NO_DATA);
    }

    private void doOnComplete() {
        mView.showRefreshing(false);
    }

    private void onOnError(Throwable e) {
        mView.showMsg(e.getMessage());
        mView.showRefreshing(false);
        mView.showLoadError(e, data == null || data.size() <= 0);

        if (e instanceof ConnectException || e instanceof SocketTimeoutException
                || e instanceof UnknownHostException) {
            tryShowEmpty(LoadType.NETWORK_ERROR);
        } else {
            tryShowEmpty(LoadType.REQUEST_ERROR);
        }
    }

    public void setProgressView(ILoadProgressView<T> mView) {
        this.mView = mView;
    }

    public void setEmptyView(IEmptyView emptyView) {
        this.mEmptyView = emptyView;
    }

    public void setLoader(Loader<T> loader) {
        this.loader = loader;
    }

    public void setSingleLoader(SingleLoader<T> loader) {
        this.singleLoader = loader;
    }

    private void tryShowEmpty(int type) {
        if (data != null && data.size() > 0) {
            mEmptyView.hideEmpty();
        } else {
            mEmptyView.showEmpty(type);
        }
    }

    public List<T> getData() {
        return data;
    }

    public T getItem(int pos) {
        if (data != null && data.size() > 0 && pos < data.size() && pos >= 0) {
            return data.get(pos);
        } else {
            return null;
        }
    }

    public void cancel() {
        if (loadDisposable != null) {
            loadDisposable.dispose();
        }
    }

    public interface Loader<T> {
        Observable<? extends Collection<T>> load();
    }

    public interface SingleLoader<T> {
        Single<? extends Collection<T>> load();
    }
}
