package com.by.lizhiyoupin.app.io;
import android.text.TextUtils;

import io.reactivex.functions.Function;

public class BaseBeanRxTransfer2<T> implements Function<BaseBean<T>, T> {

    @Override
    public T apply(BaseBean<T> bean) throws Exception {
        if (bean != null) {
            if (!TextUtils.isEmpty(bean.code)) {
                //响应成功
                if (bean.code.equals("1")) {
                    return bean.data;
                } else if (bean.code.equals("0")) { //失败

                } else if (bean.code.equals("403")) {  //token失效
                    OkHttpClientUtils.cancelAllRequest();
                } else if (bean.code.equals("-1")) {  //强制更新
                    OkHttpClientUtils.cancelAllRequest();
                }
            }
        }
        return null;
    }
}
