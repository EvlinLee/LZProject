package com.by.lizhiyoupin.app.main.weight;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.CannotScrollRecyclerView;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.SpeedLinearLayoutManager;
import com.by.lizhiyoupin.app.io.bean.BusinessArticleBean;
import com.by.lizhiyoupin.app.main.findcircle.adapter.BusinessArticleScrollAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/27 17:42
 * Summary:
 */
public class BusinessSchoolScrollLayout extends FrameLayout implements Handler.Callback {
    private int currentScrollPosition=1;
    private Handler mHandler;
    public static final int SCROLL_ARTICLE_CODE = 602;
    private CannotScrollRecyclerView mArticleRecyclerView;
    private BusinessArticleScrollAdapter mArticleScrollAdapter;


    public BusinessSchoolScrollLayout(@NonNull Context context) {
        this(context, null);
    }

    public BusinessSchoolScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BusinessSchoolScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        mHandler = new Handler(Looper.getMainLooper(), this);
        mArticleRecyclerView = findViewById(R.id.article_recyclerView);
        mArticleRecyclerView.setLayoutManager(new SpeedLinearLayoutManager(getContext()));
        mArticleScrollAdapter = new BusinessArticleScrollAdapter(getContext());
        mArticleRecyclerView.setNestedScrollingEnabled(false);
        mArticleRecyclerView.setClickable(false);
        mArticleRecyclerView.setAdapter(mArticleScrollAdapter);
    }

    public void updateView(List<BusinessArticleBean> articleBeans) {
        if (mArticleScrollAdapter != null) {
            /*不加这句会有很快的滚动动画*/
            mArticleRecyclerView.scrollToPosition(0);
            currentScrollPosition=1;
            mArticleScrollAdapter.setListData(articleBeans);
            mArticleScrollAdapter.notifyDataSetChanged();
            if (articleBeans.size() > 1) {
                mHandler.removeMessages(SCROLL_ARTICLE_CODE);
                mHandler.sendEmptyMessageDelayed(SCROLL_ARTICLE_CODE, 3000);
            }
        }
    }
    private boolean canScroll=true;
    public void stopScroll(){
        canScroll=false;
    }
    public void startScroll(){
        canScroll=true;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (SCROLL_ARTICLE_CODE == msg.what) {
            if (canScroll){
                mArticleRecyclerView.smoothScrollToPosition(currentScrollPosition);
                currentScrollPosition++;
            }
            mHandler.sendEmptyMessageDelayed(SCROLL_ARTICLE_CODE, 3000);
            return true;
        }
        return false;
    }
}
