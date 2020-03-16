package com.by.lizhiyoupin.app.component_ui.weight.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.LayoutHelper;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/2 12:23
 * Summary: 头
 */
public class BaseHeaderDelegateAdapter extends BaseOfDelegateAdapter {
    public static final String TAG = BaseHeaderDelegateAdapter.class.getSimpleName();

    public BaseHeaderDelegateAdapter(Context context, LayoutHelper layoutHelper) {
        super(context, layoutHelper,TYPE_HEADER);
    }

    @Override
    protected BaseOfViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        return null;
    }
}
