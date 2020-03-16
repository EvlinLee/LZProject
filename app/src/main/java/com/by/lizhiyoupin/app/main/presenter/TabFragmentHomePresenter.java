package com.by.lizhiyoupin.app.main.presenter;

import android.text.TextUtils;

import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.CommonCategoryBean;
import com.by.lizhiyoupin.app.main.contract.HomeContract;
import com.by.lizhiyoupin.app.main.model.TabFragmentHomeModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/16 11:51
 * Summary:
 */
public class TabFragmentHomePresenter extends HomeContract.TabFragmentHomePresenter {
    public static final String TAG=TabFragmentHomePresenter.class.getSimpleName();

    private HomeContract.TabFragmentHomeModel mHomeModel;
    private HomeContract.TabFragmentHomeView mHomeView;


    public TabFragmentHomePresenter(HomeContract.TabFragmentHomeView homeView) {
        super();
        this.mHomeView = homeView;
        mHomeModel = new TabFragmentHomeModel();
    }

    @Override
    public void requestCommodityKindList() {
        mHomeModel.requestCommodityKindList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<CommonCategoryBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<CommonCategoryBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (!listBaseBean.success()) {
                            onError(new Throwable(listBaseBean.msg));
                        }
                        LZLog.i(TAG,"requestTabFirstList success");
                        mHomeView.requestCommodityKindListSuccess(listBaseBean.data);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG,"requestTabFirstList error=="+throwable);
                        mHomeView.requestCommodityKindListError(throwable);
                    }
                });
    }


    public static boolean hasChanged(List<CommonCategoryBean> oldTabs, List<CommonCategoryBean> newTabs) {
        if (newTabs == null || newTabs.size() == 0) return false;//没有新数据，认为数据没有改变
        if (oldTabs == null || oldTabs.size() == 0) return true;
        if (oldTabs.size() != newTabs.size()) return true;
        for (int i = 0, size = oldTabs.size(); i < size; i++) {
            CommonCategoryBean oldTab = oldTabs.get(i);
            CommonCategoryBean newTab = newTabs.get(i);
            if (oldTab.getId() != newTab.getId()) return true;
            if (!TextUtils.equals(oldTab.getKindName(), newTab.getKindName())) return true;
        }
        return false;
    }

    public static List<String> names(List<CommonCategoryBean> tabs) {
        if (tabs == null) return null;
        List<String> titles = new ArrayList<>();
        for (CommonCategoryBean tab : tabs) {
            titles.add(tab.getKindName());
        }
        return titles;
    }
}
