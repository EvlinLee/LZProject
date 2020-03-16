package com.by.lizhiyoupin.app.main.holder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.weight.recyclerview.BaseOfDelegateAdapter;

import androidx.recyclerview.widget.RecyclerView;


/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/23 11:17
 * Summary: 精选底部列表 粘性头
 */
public class PreciseStickHeaderHolder implements View.OnClickListener, StickyLayoutHelper.StickyListener {
    private Context mContext;
    private StickyLayoutHelper stickyLayoutHelper;

    private TextView select_tv, people_recommend_tv, shake_tv, want_to_buy_tv;
    private View select_indicator, recommend_indicator, shake_indicator, want_to_buy_indicator;
    private TextView select_tip_tv, tip_recommend_tv, tip_shake_tv, tip_want_to_buy_tv;

    private View line_00, line_01, line_02,rootLL;
    private View line_top_00, line_top_01, line_top_02;
    private int currentIndex;
    boolean isStickTop;
    private BaseOfDelegateAdapter mBottomListAdapter;
    private BaseOfDelegateAdapter mBottomRecommendListAdapter;
    private BaseOfDelegateAdapter mShakeCouponListAdapter;
    private BaseOfDelegateAdapter mBottomWantToBuyListAdapter;
    private  DelegateAdapter delegateAdapter;
    private RecyclerView mRecyclerView;
    public int getCurrentIndex(){
        return currentIndex;
    }
    public PreciseStickHeaderHolder(BaseOfDelegateAdapter mBottomListAdapter,
                                    BaseOfDelegateAdapter mBottomRecommendListAdapter,
                                    BaseOfDelegateAdapter shakeCouponListAdapter,
                                    BaseOfDelegateAdapter mBottomWantToBuyListAdapter,
                                    DelegateAdapter delegateAdapter ) {
        super();
        this.mBottomListAdapter=mBottomListAdapter;
        this.mBottomRecommendListAdapter=mBottomRecommendListAdapter;
        this.mShakeCouponListAdapter=shakeCouponListAdapter;
        this.mBottomWantToBuyListAdapter=mBottomWantToBuyListAdapter;
        this.delegateAdapter=delegateAdapter;
    }

    public View getOnCreateView(Context context, RecyclerView recyclerView, StickyLayoutHelper stickyLayoutHelper) {
        mContext = context;
        mRecyclerView=recyclerView;
        this.stickyLayoutHelper = stickyLayoutHelper;
        View stickHeaderView = LayoutInflater.from(context).inflate(R.layout.item_precise_stick_layout, recyclerView, false);

        initStick(stickHeaderView);
        return stickHeaderView;
    }



    private void initStick(View root) {
        rootLL = root.findViewById(R.id.rootLL);
        line_00 = root.findViewById(R.id.line_00);
        line_01 = root.findViewById(R.id.line_01);
        line_02 = root.findViewById(R.id.line_02);
        line_top_00 = root.findViewById(R.id.line_top_00);
        line_top_01 = root.findViewById(R.id.line_top_01);
        line_top_02 = root.findViewById(R.id.line_top_02);
        select_tv = root.findViewById(R.id.select_tv);
        select_indicator = root.findViewById(R.id.select_indicator);
        select_tip_tv = root.findViewById(R.id.select_tip_tv);

        people_recommend_tv = root.findViewById(R.id.people_recommend_tv);
        recommend_indicator = root.findViewById(R.id.recommend_indicator);
        tip_recommend_tv = root.findViewById(R.id.tip_recommend_tv);

        shake_tv = root.findViewById(R.id.shake_tv);
        shake_indicator = root.findViewById(R.id.shake_indicator);
        tip_shake_tv = root.findViewById(R.id.tip_shake_tv);

        want_to_buy_tv = root.findViewById(R.id.want_to_buy_tv);
        want_to_buy_indicator = root.findViewById(R.id.want_to_buy_indicator);
        tip_want_to_buy_tv = root.findViewById(R.id.tip_want_to_buy_tv);
        root.findViewById(R.id.item_00).setOnClickListener(this);
        root.findViewById(R.id.item_01).setOnClickListener(this);
        root.findViewById(R.id.item_02).setOnClickListener(this);
        root.findViewById(R.id.item_03).setOnClickListener(this);
        stickyLayoutHelper.setStickyListener(this);
        select_tip_tv.setText("猜你喜欢");
        tip_recommend_tv.setText("好物值得买");
        tip_shake_tv.setText("直播秀");
        tip_want_to_buy_tv.setText("买家心得");
    }


    private void setSelectColor(int index, boolean isTop) {
        currentIndex = index;
        select_indicator.setVisibility(isTop && index == 0 ? View.VISIBLE : View.GONE);
        recommend_indicator.setVisibility(isTop && index == 1 ? View.VISIBLE : View.GONE);
        shake_indicator.setVisibility(isTop && index == 2 ? View.VISIBLE : View.GONE);
        want_to_buy_indicator.setVisibility(isTop && index == 3 ? View.VISIBLE : View.GONE);

        int colorSelect = mContext.getResources().getColor(R.color.color_FF005E);
        int colorUnSelect = mContext.getResources().getColor(R.color.color_333333);
        int colorUnTip = mContext.getResources().getColor(R.color.color_555555);
        select_tv.setTextColor(index == 0 ? colorSelect : colorUnSelect);
        people_recommend_tv.setTextColor(index == 1 ? colorSelect : colorUnSelect);
        shake_tv.setTextColor(index == 2 ? colorSelect : colorUnSelect);
        want_to_buy_tv.setTextColor(index == 3 ? colorSelect : colorUnSelect);

        select_tip_tv.setVisibility(isTop ? View.GONE : View.VISIBLE);
        tip_recommend_tv.setVisibility(isTop ? View.GONE : View.VISIBLE);
        tip_shake_tv.setVisibility(isTop ? View.GONE : View.VISIBLE);
        tip_want_to_buy_tv.setVisibility(isTop ? View.GONE : View.VISIBLE);

        if (isTop) {
            line_00.setVisibility(View.GONE);
            line_01.setVisibility(View.GONE);
            line_02.setVisibility(View.GONE);
            line_top_00.setVisibility(View.VISIBLE);
            line_top_01.setVisibility(View.VISIBLE);
            line_top_02.setVisibility(View.VISIBLE);
        } else {
            select_tip_tv.setBackgroundResource(index == 0 ? R.drawable.shape_bg_ff005f_10_full_corner : 0);
            tip_recommend_tv.setBackgroundResource(index == 1 ? R.drawable.shape_bg_ff005f_10_full_corner : 0);
            tip_shake_tv.setBackgroundResource(index == 2 ? R.drawable.shape_bg_ff005f_10_full_corner : 0);
            tip_want_to_buy_tv.setBackgroundResource(index == 3 ? R.drawable.shape_bg_ff005f_10_full_corner : 0);
            select_tip_tv.setTextColor(index == 0 ?Color.WHITE:colorUnTip);
            tip_recommend_tv.setTextColor(index == 1 ?Color.WHITE:colorUnTip);
            tip_shake_tv.setTextColor(index == 2 ?Color.WHITE:colorUnTip);
            tip_want_to_buy_tv.setTextColor(index == 3 ?Color.WHITE:colorUnTip);
            line_00.setVisibility(View.VISIBLE);
            line_01.setVisibility(View.VISIBLE);
            line_02.setVisibility(View.VISIBLE);
            line_top_00.setVisibility(View.GONE);
            line_top_01.setVisibility(View.GONE);
            line_top_02.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()){
            return;
        }
        int itemCount=25;
        switch (v.getId()) {
            case R.id.item_00://荔枝优选
                if (currentIndex==0){
                    return;
                }


                setSelectColor(0, isStickTop);
                delegateAdapter.removeAdapter(delegateAdapter.getAdaptersCount()-1);
                delegateAdapter.addAdapter(mBottomListAdapter);
                delegateAdapter.notifyDataSetChanged();
                itemCount = mBottomListAdapter.getItemCount();
                break;
            case R.id.item_01://达人推荐
                if (currentIndex==1){
                    return;
                }
                setSelectColor(1, isStickTop);
                delegateAdapter.removeAdapter(delegateAdapter.getAdaptersCount()-1);
                delegateAdapter.addAdapter(mBottomRecommendListAdapter);
                delegateAdapter.notifyDataSetChanged();
                itemCount = mBottomRecommendListAdapter.getItemCount();
                break;
            case R.id.item_02:// 抖券
                if (currentIndex==2){
                    return;
                }
                setSelectColor(2, isStickTop);
                delegateAdapter.removeAdapter(delegateAdapter.getAdaptersCount()-1);
                delegateAdapter.addAdapter(mShakeCouponListAdapter);
                delegateAdapter.notifyDataSetChanged();
                itemCount = mShakeCouponListAdapter.getItemCount();
                break;
            case R.id.item_03://种草
                if (currentIndex==3){
                    return;
                }
                setSelectColor(3, isStickTop);
                delegateAdapter.removeAdapter(delegateAdapter.getAdaptersCount()-1);
                delegateAdapter.addAdapter(mBottomWantToBuyListAdapter);
                delegateAdapter.notifyDataSetChanged();
                itemCount = mBottomWantToBuyListAdapter.getItemCount();
                break;
        }

        int itemCount1 = delegateAdapter.getItemCount();
        mRecyclerView.smoothScrollToPosition(itemCount1-itemCount);
    }

    @Override
    public void onSticky(int pos, View view) {
        isStickTop = true;
        rootLL.setBackgroundColor(Color.WHITE);
        setSelectColor(currentIndex, isStickTop);
    }

    @Override
    public void onUnSticky(int pos, View view) {
        isStickTop = false;
        rootLL.setBackgroundColor(mContext.getResources().getColor(R.color.color_F2F2F5));
        setSelectColor(currentIndex, isStickTop);
    }


}
