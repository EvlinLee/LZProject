package com.by.lizhiyoupin.app.search.contract;

import com.by.lizhiyoupin.app.common.base.BaseModel;
import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;
import com.by.lizhiyoupin.app.io.BaseBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/19 10:53
 * Summary:
 */
public interface SearchContract {
    interface SearchView extends BaseView{
        void requestSearchHotListSuccess(List<String> hotList);

        void requestSearchHotListError(Throwable throwable);
    }

    interface SearchModel extends BaseModel{
        Observable<BaseBean<List<String>>> requestSearchHotList( );
    }
    abstract class SearchPresenters extends BasePresenter<SearchView>{
        public abstract  void requestSearchHotList();
    }
}
