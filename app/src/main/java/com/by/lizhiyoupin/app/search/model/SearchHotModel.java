package com.by.lizhiyoupin.app.search.model;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.service.ApiService;
import com.by.lizhiyoupin.app.search.contract.SearchContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/19 10:58
 * Summary:
 */
public class SearchHotModel implements SearchContract.SearchModel {
    @Override
    public Observable<BaseBean<List<String>>> requestSearchHotList() {
        return ApiService.getSearchApi().requestSearchHotList();
    }
}
