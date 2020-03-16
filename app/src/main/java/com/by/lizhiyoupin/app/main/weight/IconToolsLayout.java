package com.by.lizhiyoupin.app.main.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.io.bean.HomeIconBean;
import com.by.lizhiyoupin.app.main.adapter.HomeIconToolAdapter;
import com.by.lizhiyoupin.app.weight.IconIndicatorView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/30 15:27
 * Summary: 首页金刚区 icon
 */
public class IconToolsLayout extends LinearLayout {
    public static final String TAG = IconToolsLayout.class.getSimpleName();
    private RecyclerView iconRecyclerView;
    private IconIndicatorView iconIndicator;
    private Context mContext;
    private HomeIconToolAdapter mAdapter;

    public IconToolsLayout(Context context) {
        this(context, null);
    }

    public IconToolsLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconToolsLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iconRecyclerView = findViewById(R.id.icon_recyclerView);
        iconIndicator = findViewById(R.id.iconIndicator);
        mAdapter = new HomeIconToolAdapter(mContext);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.HORIZONTAL, false);
        //setUsingSpansToEstimateScrollbarDimensions 设置true 来防止应为spanCount 不满2时 计算不准确问题
        //跨距估算滚动条距离
        gridLayoutManager.setUsingSpansToEstimateScrollbarDimensions(true);
        iconRecyclerView.setLayoutManager(gridLayoutManager);
        iconRecyclerView.setAdapter(mAdapter);
        iconRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //当前RcyclerView显示区域的高度。水平列表屏幕从左侧到右侧显示范围
                int extent = recyclerView.computeHorizontalScrollExtent();
                //整体的高度，注意是整体，包括在显示区域之外的。
                int range = recyclerView.computeHorizontalScrollRange();

                //已经滚动的距离，为0时表示已处于顶部。
                int offset = recyclerView.computeHorizontalScrollOffset();
                if (extent==range){
                    iconIndicator.setVisibility(GONE);
                }else {
                    iconIndicator.setVisibility(VISIBLE);
                    //计算出溢出部分的宽度，即屏幕外剩下的宽度
                    float maxEndX = range - extent;
                    //计算比例
                    float proportion = offset / maxEndX;
                    int layoutWidth = iconIndicator.getWidth();
                    int indicatorViewWidth = iconIndicator.getRegularWidth();
                    //可滑动的距离
                    int scrollableDistance = layoutWidth - indicatorViewWidth;
                    //设置滚动条移动
                    iconIndicator.setDragWidthX(scrollableDistance * proportion);
                }


            }
        });

    }

    public void initNetWorkIcon(List<HomeIconBean> iconEntities) {
        iconIndicator.setVisibility(VISIBLE);
        mAdapter.setListData(iconEntities);
        mAdapter.notifyDataSetChanged();
    }
}
