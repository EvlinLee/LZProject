package com.by.lizhiyoupin.app.weight.week;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

import androidx.annotation.Nullable;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/8 20:29
 * Summary:
 */
public class LimitedTimeSkillitleView extends LinearLayout implements IPagerTitleView {

    private TextView mTimeTv;
    private TextView mTipTv;
    private int selectTipsColor=-1;
    private int selectTimeColor=-1;
    private int unSelectTipsColor=-1;
    private int unSelectTimeColor=-1;
    private Context mContext;
    public LimitedTimeSkillitleView(Context context) {
        this(context, null);
    }

    public LimitedTimeSkillitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LimitedTimeSkillitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        setOrientation(VERTICAL);
        init(context);
    }

    private void init(Context context) {

        LayoutInflater.from(context).inflate(R.layout.item_limited_time_title_layout, this, true);
        mTimeTv = findViewById(R.id.time_tv);
        mTipTv = findViewById(R.id.tips_tv);
        selectTipsColor =Color.WHITE;
        selectTimeColor = getResources().getColor(R.color.color_FF005E);
        unSelectTipsColor = getResources().getColor(R.color.color_999999);
        unSelectTimeColor = getResources().getColor(R.color.color_999999);
        setGravity(Gravity.CENTER);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        mTimeTv.setTextColor(selectTimeColor);
        mTipTv.setTextColor(selectTipsColor);
        mTipTv.setBackgroundResource(R.drawable.shape_bg_limited_timeskill_title);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        mTimeTv.setTextColor(unSelectTimeColor);
        mTipTv.setTextColor(unSelectTipsColor);
        mTipTv.setBackground(null);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
    }

    public void setTimeText(String timeText){
        if (mTimeTv!=null){
            mTimeTv.setText(timeText);
        }
    }

    public void setTipsText(String tips){
        if (mTipTv!=null){
            mTipTv.setText(tips);
        }
    }
}
