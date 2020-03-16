package com.by.lizhiyoupin.app.loader.facade;

import android.content.Context;

import com.by.lizhiyoupin.app.loader.ILoadProgressView;
import com.by.lizhiyoupin.app.loader.LoadType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;


abstract class DefaultLoadProgressView<T> implements ILoadProgressView<T> {
        private Context context;

        private SmartRefreshLayout refreshLayout;

        public DefaultLoadProgressView(Context context, SmartRefreshLayout refreshLayout) {
            this.context = context.getApplicationContext();
            this.refreshLayout = refreshLayout;
        }

        @Override
        public void showRefreshing(boolean active) {
            if (refreshLayout != null) {
                if (active) {
                    //
                } else {
                    refreshLayout.finishRefresh();
                }
            }
        }

        @Override
        public void showMsg(String msg) {
        }

        @Override
        public void showLoadError(Throwable e, boolean isEmpty) {
        }

        @Override
        public void showLoader(@LoadType int type) {
            //不分页加载，一般不需要实现这个方法。
        }
    }