package com.by.lizhiyoupin.app.main.holder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;

import androidx.viewpager.widget.ViewPager;


/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/12/23 11:17
 * Summary: 精选底部列表 粘性头
 */
public class PreciseStickTabHolder implements View.OnClickListener {
    private Context mContext;


    private TextView select_tv, people_recommend_tv, shake_tv, want_to_buy_tv;
    private TextView select_tip_tv, tip_recommend_tv, tip_shake_tv, tip_want_to_buy_tv;
    private View rootLL;
    private int currentIndex;
    private  ViewPager mViewPager;

    public PreciseStickTabHolder() {
        super();

    }

    public void getOnCreateView(Context context, View root, ViewPager viewPager) {
        mContext = context;
        mViewPager=viewPager;
        initStick(root);
    }


    private void initStick(View root) {
        rootLL = root.findViewById(R.id.rootLL);
        select_tv = root.findViewById(R.id.select_tv);
        people_recommend_tv = root.findViewById(R.id.people_recommend_tv);
        shake_tv = root.findViewById(R.id.shake_tv);
        want_to_buy_tv = root.findViewById(R.id.want_to_buy_tv);

        select_tip_tv = root.findViewById(R.id.select_tip_tv);
        tip_recommend_tv = root.findViewById(R.id.tip_recommend_tv);
        tip_shake_tv = root.findViewById(R.id.tip_shake_tv);
        tip_want_to_buy_tv = root.findViewById(R.id.tip_want_to_buy_tv);
        root.findViewById(R.id.item_00).setOnClickListener(this);
        root.findViewById(R.id.item_01).setOnClickListener(this);
        root.findViewById(R.id.item_02).setOnClickListener(this);
        root.findViewById(R.id.item_03).setOnClickListener(this);

        select_tip_tv.setText("猜你喜欢");
        tip_recommend_tv.setText("好物值得买");
        tip_shake_tv.setText("直播秀");
        tip_want_to_buy_tv.setText("买家心得");
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setSelectColor(int index) {
        currentIndex = index;
        mViewPager.setCurrentItem(index);
        int colorSelect = mContext.getResources().getColor(R.color.color_FF005E);
        int colorUnSelect = mContext.getResources().getColor(R.color.color_333333);
        int colorUnTip = mContext.getResources().getColor(R.color.color_555555);
        select_tv.setTextColor(index == 0 ? colorSelect : colorUnSelect);
        people_recommend_tv.setTextColor(index == 1 ? colorSelect : colorUnSelect);
        shake_tv.setTextColor(index == 2 ? colorSelect : colorUnSelect);
        want_to_buy_tv.setTextColor(index == 3 ? colorSelect : colorUnSelect);

        select_tip_tv.setBackgroundResource(index == 0 ? R.drawable.shape_bg_ff005f_10_full_corner : 0);
        tip_recommend_tv.setBackgroundResource(index == 1 ? R.drawable.shape_bg_ff005f_10_full_corner : 0);
        tip_shake_tv.setBackgroundResource(index == 2 ? R.drawable.shape_bg_ff005f_10_full_corner : 0);
        tip_want_to_buy_tv.setBackgroundResource(index == 3 ? R.drawable.shape_bg_ff005f_10_full_corner : 0);
        select_tip_tv.setTextColor(index == 0 ? Color.WHITE : colorUnTip);
        tip_recommend_tv.setTextColor(index == 1 ? Color.WHITE : colorUnTip);
        tip_shake_tv.setTextColor(index == 2 ? Color.WHITE : colorUnTip);
        tip_want_to_buy_tv.setTextColor(index == 3 ? Color.WHITE : colorUnTip);
    }


    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.item_00://荔枝优选
                if (currentIndex == 0) {
                    return;
                }
                setSelectColor(0);
                break;
            case R.id.item_01://达人推荐
                if (currentIndex == 1) {
                    return;
                }
                setSelectColor(1);
                break;
            case R.id.item_02:// 抖券
                if (currentIndex == 2) {
                    return;
                }
                setSelectColor(2);
                break;
            case R.id.item_03://种草
                if (currentIndex == 3) {
                    return;
                }
                setSelectColor(3);
                break;
        }

    }


}
