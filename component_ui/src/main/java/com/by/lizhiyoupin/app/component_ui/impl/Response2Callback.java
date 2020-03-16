package com.by.lizhiyoupin.app.component_ui.impl;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/25 10:55
 * Summary:
 */
public interface Response2Callback<T> {
    void callbackSuccess(T t);

    void callbackError(String error);
}
