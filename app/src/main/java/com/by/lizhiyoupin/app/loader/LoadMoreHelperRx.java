package com.by.lizhiyoupin.app.loader;


import android.util.Log;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.core.util.ObjectsCompat;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 创建时间：2016/9/10
 *
 * @author wzm
 */
public class LoadMoreHelperRx<T, Page> {
    /**
     * 分页展示的当前页
     */
    private Page curPage;
    private IPageStrategy<T, Page> pageStrategy;

    private List<T> data;
    private boolean hasMore;

    private ILoadProgressView<T> mView;

    private IEmptyView mEmptyView;

    private Loader<T, Page> loader;

    private Disposable loadDisposable;

    private boolean isLoading;

    public LoadMoreHelperRx() {
    }

    /**
     * load more 构造函数
     *
     * @param mView        load progress view
     * @param emptyView    empty view
     * @param pageStrategy 分页策略
     */
    public LoadMoreHelperRx(ILoadProgressView<T> mView, IEmptyView emptyView, IPageStrategy<T, Page> pageStrategy, Loader<T, Page> loader) {
        this.mView = mView;
        this.mEmptyView = emptyView;
        this.pageStrategy = pageStrategy;
        this.curPage = pageStrategy.begin();
        this.loader = loader;
    }

    public void setPageStrategy(IPageStrategy<T, Page> pageStrategy) {
        this.pageStrategy = pageStrategy;
    }

    public void setProgressView(ILoadProgressView<T> view) {
        this.mView = view;
    }

    public void setEmptyView(IEmptyView emptyView) {
        this.mEmptyView = emptyView;
    }

    public void setLoader(Loader<T, Page> loader) {
        this.loader = loader;
    }

    /**
     * 首次或重新加载
     */
    public void loadData() {
        curPage=null;//防止加载更多后，再次调用后当前页没有复原
        loadData(pageStrategy.begin());
    }

    public void loadDataMore() {
        if (isLoading){
            Log.i("load", "loadDataMore: 已经在加载中了,不要重复加载");
            return;
        }
        if (curPage == null) {
            curPage = pageStrategy.begin();
        }
        loadData(curPage);
    }

    private void loadData(final Page page) {
        isLoading = true;
        boolean isPageBegin = ObjectsCompat.equals(page, pageStrategy.begin());
        mView.showRefreshing(isPageBegin);
        if (isPageBegin) {
            tryShowEmpty(LoadType.LOADING);
        } else {
            tryShowLoader(LoadType.LOADING);
        }
        if (loadDisposable != null) {
            loadDisposable.dispose();

        }
        loader.load(page, pageStrategy.pageSize())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<Collection<T>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        loadDisposable = d;
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        isLoading = false;
                        curPage = pageStrategy.pageStrategy(data, curPage);
                        mView.showRefreshing(false);
                        tryShowEmpty(LoadType.NO_DATA);
                    }


                    @Override
                    public void onError(Throwable e) {
                        LZLog.e("load","DefaultRx2Subscribe onError=="+e.toString());
                        isLoading = false;
                        mView.showMsg(e.getMessage());
                        mView.showRefreshing(false);
                        mView.showLoadError(e, data == null || data.isEmpty());
                        if (e instanceof ConnectException || e instanceof SocketTimeoutException
                                || e instanceof UnknownHostException) {
                            tryShowEmpty(LoadType.NETWORK_ERROR);
                            tryShowLoader(LoadType.NETWORK_ERROR);
                        } else {
                            tryShowEmpty(LoadType.REQUEST_ERROR);
                            tryShowLoader(LoadType.REQUEST_ERROR);
                        }
                    }

                    @Override
                    public void onNext(Collection<T> data) {
                        LZLog.i("load","DefaultRx2Subscribe onNext");
                        isLoading = false;
                        int start;
                        int count = data == null ? 0 : data.size();
                        @ILoadProgressView.DataChangeType int changeType;
                        if (ObjectsCompat.equals(page, pageStrategy.begin())) {
                            LoadMoreHelperRx.this.data = (List<T>) data;
                            start = 0;
                            changeType = ILoadProgressView.DataChangeType.CHANGE;
                        } else {
                            start = LoadMoreHelperRx.this.data == null ? 0 : LoadMoreHelperRx.this.data.size();
                            if (data != null && data.size() > 0) {
                                if (LoadMoreHelperRx.this.data == null) {
                                    LoadMoreHelperRx.this.data = new ArrayList<>(data.size());
                                }
                                LoadMoreHelperRx.this.data.addAll(data);
                            }
                            changeType = ILoadProgressView.DataChangeType.INSERT_RANGE;
                        }
                        hasMore = loader.hasMore(data, pageStrategy.pageSize());
                        mView.showData(LoadMoreHelperRx.this.data, changeType, start, count, hasMore);
                        tryShowLoader(hasMore ? LoadType.LOADING : LoadType.NO_MORE_DATA);
                        tryShowEmpty(LoadType.NO_DATA);
                    }
                });
    }

    public void removeItem(T item) {
        int start = data == null ? 0 : data.indexOf(item);
        if (data != null && data.size() > 0 && data.remove(item)) {
            start = Math.max(0, start);
            mView.showData(data, ILoadProgressView.DataChangeType.REMOVE, start, 1, hasMore);
            tryShowEmpty(LoadType.NO_DATA);
        }
    }

    private void tryShowEmpty(int type) {
        if (mEmptyView != null) {
            if (data != null && data.size() > 0) {
                mEmptyView.hideEmpty();
            } else {
                mEmptyView.showEmpty(type);
            }
        }
    }

    public void showDeclaredEmpty(){
        if (mEmptyView!=null){
            clear();
            mView.showData(data, ILoadProgressView.DataChangeType.CHANGE, 0, 1, hasMore);
            mEmptyView.showEmpty(LoadType.NO_DATA);
        }
    }

    private void tryShowLoader(@LoadType int type) {
        if (mView != null) {
            if (data != null && data.size() > 0) {
                mView.showLoader(type);
            }
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasMore() {
        return hasMore;
    }

    public List<T> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    public T getItem(int pos) {
        if (data != null && data.size() > 0 && pos < data.size() && pos >= 0) {
            return data.get(pos);
        } else {
            return null;
        }
    }

    public void notifyDataSetChange() {
        mView.showData(data, ILoadProgressView.DataChangeType.CHANGE, 0, data == null ? 0 : data.size(), hasMore);
        tryShowEmpty(LoadType.NO_DATA);
    }

    public Page curPage() {
        return curPage;
    }

    public void clear() {
        if (data != null) {
            data.clear();
        }
    }

    public void cancel() {
        if (loadDisposable != null && !loadDisposable.isDisposed()) {
            loadDisposable.dispose();
        }
    }

    public interface Loader<T, Page> {
        Observable<? extends Collection<T>> load(Page page, int pageSize);

        /**
         * 是否还有更多数据
         *
         * @param data      数据
         * @param pageCount 尝试请求的数据 count
         * @return 是否有更多数据
         */
        default boolean hasMore(Collection<T> data, int pageCount) {
            int dataSize = data == null ? 0 : data.size();
            return data != null && dataSize > 0 && dataSize >= pageCount;
        }
    }

}
