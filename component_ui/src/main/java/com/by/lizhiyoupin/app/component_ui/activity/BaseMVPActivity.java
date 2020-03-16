package com.by.lizhiyoupin.app.component_ui.activity;

import android.os.Bundle;

import com.by.lizhiyoupin.app.common.base.BasePresenter;
import com.by.lizhiyoupin.app.common.base.BaseView;

import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/9 09:51
 * Summary: mvp
 */
public abstract class BaseMVPActivity<V extends BaseView, T extends BasePresenter<V>> extends BaseActivity {
    public T basePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        basePresenter = getBasePresenter();
        if (basePresenter != null) {
            basePresenter.attachView((V) this);
        }
    }

    /**
     *  创建 presenter
     * @return basePresenter
     */
    public abstract T getBasePresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (basePresenter != null) {
            basePresenter.detachView();
        }
    }
}
