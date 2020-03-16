package com.by.lizhiyoupin.app.loader;


import java.util.List;

import androidx.annotation.IntDef;

/**
 * 创建时间：2016/9/10
 *
 * @author wzm
 */
public interface ILoadProgressView<T> {
    /**
     * 是否刷新中
     *
     * @param active true refreshing, otherwise false
     */
    void showRefreshing(boolean active);

    /**
     * 显示要提示消息，比如请求失败的信息
     *
     * @param msg 消息内容
     */
    void showMsg(String msg);

    /**
     * 显示数据
     *
     * @param allData 数据
     * @param hasMore 是否还有更多
     */
    void showData(List<T> allData, @DataChangeType int changeType, int start, int count, boolean hasMore);

    /**
     * 列表当前的加载情况，通常实现为 footer
     *
     * @param type {@link LoadType}
     */
    void showLoader(@LoadType int type);

    /**
     * 显示加载错误
     *
     * @param e       error
     * @param isEmpty 当前数据是否为空
     */
    void showLoadError(Throwable e, boolean isEmpty);

    @IntDef(value = {
            DataChangeType.CHANGE,
            DataChangeType.INSERT_RANGE,
            DataChangeType.INSERT,
            DataChangeType.REMOVE
    })
    @interface DataChangeType {
        int CHANGE = 1;
        int INSERT_RANGE = 2;
        int INSERT = 3;
        int REMOVE = 4;
    }
}
