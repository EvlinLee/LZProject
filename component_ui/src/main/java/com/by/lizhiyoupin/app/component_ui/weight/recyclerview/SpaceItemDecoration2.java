package com.by.lizhiyoupin.app.component_ui.weight.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/28 15:13
 * Summary: 网格布局分割线 右 下添加分割线（只针对行或列固定不变的）
 */
public class SpaceItemDecoration2 extends RecyclerView.ItemDecoration {
    private int space;
    private int spanCount;
    private boolean hasHeader;
    private int orientation = RecyclerView.VERTICAL;

    public SpaceItemDecoration2(int space, int spanCount) {
        this.space = space;
        this.spanCount = spanCount;
    }
    public SpaceItemDecoration2(int space, int spanCount, @RecyclerView.Orientation int orientation) {
        this.space = space;
        this.spanCount = spanCount;
        this.orientation = orientation;
    }

    /**
     * 是否有header
     * @param hasHeader
     */
    public void setHasHeader(boolean hasHeader){
        this.hasHeader=hasHeader;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0,0,space,space);
        //竖向
        if (orientation == RecyclerView.VERTICAL ) {
            if (hasHeader){
                if ((parent.getChildLayoutPosition(view)-1)%spanCount==spanCount-1){
                    //最右边
                    outRect.set(0,0,0,space);
                }
            }else if (parent.getChildLayoutPosition(view)%spanCount==spanCount-1){
                //最右边
                outRect.set(0,0,0,space);
            }

        }else if (orientation==RecyclerView.HORIZONTAL){
            outRect.set(0,0,space,space);
        }
    }

}
