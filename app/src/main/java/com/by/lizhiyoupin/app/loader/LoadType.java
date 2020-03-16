package com.by.lizhiyoupin.app.loader;


import androidx.annotation.IntDef;

@IntDef(value = {LoadType.NO_DATA, LoadType.NO_MORE_DATA, LoadType.LOADING, LoadType.NETWORK_ERROR, LoadType.REQUEST_ERROR})
public @interface LoadType {
    int NO_DATA = 1;
    int NO_MORE_DATA = 2;
    int LOADING = 3;
    int NETWORK_ERROR = 4;
    int REQUEST_ERROR = 5;
}
