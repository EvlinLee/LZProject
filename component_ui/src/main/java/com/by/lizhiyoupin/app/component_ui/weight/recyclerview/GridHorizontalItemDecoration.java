package com.by.lizhiyoupin.app.component_ui.weight.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/20 11:38
 * Summary:
 */
public class GridHorizontalItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int spanCount;

    public GridHorizontalItemDecoration(int space, int spanCount) {
        super();
        this.space = space;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        //super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildLayoutPosition(view) % spanCount != 0) {
            outRect.set(space, 0, space, 0);
        } else {
            outRect.set(0, 0, space, 0);
        }
    }
}
