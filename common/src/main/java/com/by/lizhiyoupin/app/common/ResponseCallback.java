package com.by.lizhiyoupin.app.common;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/28 19:39
 * Summary:
 */
public interface ResponseCallback<T> {
    void success(T t);
    void error(Throwable throwable);
}
