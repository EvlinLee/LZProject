package com.by.lizhiyoupin.app.loader;

/**
 * 空白页面
 */
public interface IEmptyView {
    /**
     * 显示 empty view
     *
     * @param type 空白页的类型
     */
    void showEmpty(@LoadType int type);

    /**
     * 隐藏空白页
     */
    void hideEmpty();
}
