package com.by.lizhiyoupin.app.main.holder;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.ArraysUtils;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.component_ui.weight.CountTimeView;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.LimitedTimeSkillBean;
import com.by.lizhiyoupin.app.main.adapter.LimitskillAdapter;
import com.by.lizhiyoupin.app.user.SettingRequestManager;
import com.by.lizhiyoupin.app.weight.AutoInterceptViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager2.widget.ViewPager2;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * jyx
 * 限时秒杀
 */
public class LimitSkillNewHeaderHolder implements View.OnClickListener {
    public static final String TAG=LimitSkillNewHeaderHolder.class.getSimpleName();
    private Context mContext;
    private LimitskillAdapter sadapter;
    private ViewPager2 viewpager2;
    private LinearLayout linear1, linear2, linear3, time_bg1, time_bg2, time_bg3, limitskill;
    private TextView tv_time1, tv_time2, tv_time3, tv_title1, tv_title2, tv_title3, tv1, tv2;
    private List<LimitedTimeSkillBean> result;

    private CountTimeView count_time_view;

    private AutoInterceptViewGroup autoView;
    private DataSetObserver mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            //倒计时结束
            Log.i("倒计时结束", "onChanged:重新请求 ");
            initsData();
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    if (viewpager2 != null) {
                        int index = viewpager2.getCurrentItem();
                        viewpager2.setCurrentItem(index + 1);
                    }
                    handler.sendEmptyMessageDelayed(1001, 3000);
                    break;
            }
        }

    };

    public View getOnCreateView(Context context, View root) {
        this.mContext = context;
        View stickHeaderView = root.findViewById(R.id.limited_kill);

        initView(stickHeaderView);
        initsData();


        return stickHeaderView;
    }

    public void autoPlay() {
        autoView.setAutoPlay(true);
        handler.removeMessages(1001);
        handler.sendEmptyMessageDelayed(1001, 3000);
    }

    private void initView(View stickHeaderView) {
        result=new ArrayList<>();
        viewpager2 = stickHeaderView.findViewById(R.id.viewpager2);
        count_time_view = stickHeaderView.findViewById(R.id.count_time_view);
        count_time_view.registerDataSetObserver(mObserver);
        tv_time1 = stickHeaderView.findViewById(R.id.tv_time1);
        tv_time2 = stickHeaderView.findViewById(R.id.tv_time2);
        tv_time3 = stickHeaderView.findViewById(R.id.tv_time3);
        tv_title1 = stickHeaderView.findViewById(R.id.tv_title1);
        tv_title2 = stickHeaderView.findViewById(R.id.tv_title2);
        tv_title3 = stickHeaderView.findViewById(R.id.tv_title3);
        linear1 = stickHeaderView.findViewById(R.id.linear1);
        linear2 = stickHeaderView.findViewById(R.id.linear2);
        linear3 = stickHeaderView.findViewById(R.id.linear3);
        limitskill = stickHeaderView.findViewById(R.id.limitskill);
        linear1.setOnClickListener(this);
        linear2.setOnClickListener(this);
        linear3.setOnClickListener(this);
        autoView = stickHeaderView.findViewById(R.id.autoView);
        time_bg1 = stickHeaderView.findViewById(R.id.time_bg1);
        time_bg2 = stickHeaderView.findViewById(R.id.time_bg2);
        time_bg3 = stickHeaderView.findViewById(R.id.time_bg3);
        time_bg1.setBackgroundResource(R.drawable.shape_bg_count_timeskill_corner);
        time_bg2.setBackgroundResource(R.drawable.shape_bg_count_timeskill_corner);
        time_bg3.setBackgroundResource(R.drawable.shape_bg_count_timeskill_corner);
        tv1 = stickHeaderView.findViewById(R.id.tv1);
        tv2 = stickHeaderView.findViewById(R.id.tv2);
        tv1.setTextColor(Color.parseColor("#FF333333"));
        tv2.setTextColor(Color.parseColor("#FF333333"));
        sadapter = new LimitskillAdapter(mContext, result);
        viewpager2.setAdapter(sadapter);

        limitskill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonSchemeJump.showActivity(mContext, "/app/LimitedTimeSpikeActivity");
            }
        });
        autoView.setOnsClickListener(new AutoInterceptViewGroup.OnClickListener() {
            @Override
            public void paly() {
                if (result.size()<=0){
                    return;
                }
                autoView.setAutoPlay(true);
                handler.removeMessages(1001);
                handler.sendEmptyMessageDelayed(1001, 3000);
            }

            @Override
            public void stop() {
                autoView.setAutoPlay(false);
                handler.removeMessages(1001);
            }
        });
        viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position % 3 == 0) {
                    tv_time1.setTextColor(Color.parseColor("#FFFF005E"));
                    tv_title1.setBackgroundResource(R.drawable.shape_bg_limitedskill_time_title);
                    tv_title1.setTextColor(Color.parseColor("#FFFFFF"));

                    tv_time2.setTextColor(Color.parseColor("#FF999999"));
                    tv_title2.setTextColor(Color.parseColor("#FF999999"));
                    tv_title2.setBackgroundResource(R.drawable.shape_bg_limited_time_title);
                    tv_time3.setTextColor(Color.parseColor("#FF999999"));
                    tv_title3.setTextColor(Color.parseColor("#FF999999"));
                    tv_title3.setBackgroundResource(R.drawable.shape_bg_limited_time_title);
                } else if (position % 3 == 1) {
                    tv_time1.setTextColor(Color.parseColor("#FF999999"));
                    tv_title1.setBackgroundResource(R.drawable.shape_bg_limited_time_title);
                    tv_title1.setTextColor(Color.parseColor("#FF999999"));
                    tv_time2.setTextColor(Color.parseColor("#FFFF005E"));
                    tv_title2.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_title2.setBackgroundResource(R.drawable.shape_bg_limitedskill_time_title);
                    tv_time3.setTextColor(Color.parseColor("#FF999999"));
                    tv_title3.setTextColor(Color.parseColor("#FF999999"));
                    tv_title3.setBackgroundResource(R.drawable.shape_bg_limited_time_title);
                } else {
                    tv_time1.setTextColor(Color.parseColor("#FF999999"));
                    tv_title1.setBackgroundResource(R.drawable.shape_bg_limited_time_title);
                    tv_title1.setTextColor(Color.parseColor("#FF999999"));
                    tv_time2.setTextColor(Color.parseColor("#FF999999"));
                    tv_title2.setTextColor(Color.parseColor("#FF999999"));
                    tv_title2.setBackgroundResource(R.drawable.shape_bg_limited_time_title);
                    tv_time3.setTextColor(Color.parseColor("#FFFF005E"));
                    tv_title3.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_title3.setBackgroundResource(R.drawable.shape_bg_limitedskill_time_title);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);


            }
        });

    }


    public void initsData() {
        SettingRequestManager.requestLitmitSkillTitle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<List<LimitedTimeSkillBean>>>() {
                    @Override
                    public void onNext(BaseBean<List<LimitedTimeSkillBean>> userInfoBeanBaseBean) {
                        super.onNext(userInfoBeanBaseBean);
                        LZLog.i(TAG,"requestLitmitSkillTitle");
                        if (!userInfoBeanBaseBean.success()) {
                            onError(new Throwable(userInfoBeanBaseBean.msg));
                            return;
                        }
                        List<LimitedTimeSkillBean> data= userInfoBeanBaseBean.getResult();
                        if (ArraysUtils.isListEmpty(data)||data.size()<3){
                            return;
                        }
                        result.clear();
                        result.addAll(data);

                        tv_time1.setText(result.get(0).getSessionTime());
                        tv_time2.setText(result.get(1).getSessionTime());
                        tv_time3.setText(result.get(2).getSessionTime());
                        if (result.get(0).getBuyStatus() == 0) {
                            tv_title1.setText("即将开始");
                        } else {
                            tv_title1.setText("已开抢");
                        }
                        if (result.get(1).getBuyStatus() == 0) {
                            tv_title2.setText("即将开始");
                        } else {
                            tv_title2.setText("已开抢");
                        }
                        if (result.get(2).getBuyStatus() == 0) {
                            tv_title3.setText("即将开始");
                        } else {
                            tv_title3.setText("已开抢");
                        }
                        count_time_view.startLimitedTime(result.get(0).getSecond());
                        sadapter.notifyDataSetChanged();
                        viewpager2.setCurrentItem(1000000, false);
                        autoPlay();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LZLog.i(TAG,"requestLitmitSkillTitle error=="+throwable);
                    }
                });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear1:
                tv_time1.setTextColor(Color.parseColor("#FFFF005E"));
                tv_title1.setBackgroundResource(R.drawable.shape_bg_limitedskill_time_title);
                tv_title1.setTextColor(Color.parseColor("#FFFFFF"));

                tv_time2.setTextColor(Color.parseColor("#FF999999"));
                tv_title2.setTextColor(Color.parseColor("#FF999999"));
                tv_title2.setBackgroundResource(R.drawable.shape_bg_limited_time_title);
                tv_time3.setTextColor(Color.parseColor("#FF999999"));
                tv_title3.setTextColor(Color.parseColor("#FF999999"));
                tv_title3.setBackgroundResource(R.drawable.shape_bg_limited_time_title);
                viewpager2.setCurrentItem(auplay(0));
                break;
            case R.id.linear2:
                tv_time1.setTextColor(Color.parseColor("#FF999999"));
                tv_title1.setBackgroundResource(R.drawable.shape_bg_limited_time_title);
                tv_title1.setTextColor(Color.parseColor("#FF999999"));
                tv_time2.setTextColor(Color.parseColor("#FFFF005E"));
                tv_title2.setTextColor(Color.parseColor("#FFFFFF"));
                tv_title2.setBackgroundResource(R.drawable.shape_bg_limitedskill_time_title);
                tv_time3.setTextColor(Color.parseColor("#FF999999"));
                tv_title3.setTextColor(Color.parseColor("#FF999999"));
                tv_title3.setBackgroundResource(R.drawable.shape_bg_limited_time_title);
                viewpager2.setCurrentItem(auplay(1));
                break;
            case R.id.linear3:
                tv_time1.setTextColor(Color.parseColor("#FF999999"));
                tv_title1.setBackgroundResource(R.drawable.shape_bg_limited_time_title);
                tv_title1.setTextColor(Color.parseColor("#FF999999"));
                tv_time2.setTextColor(Color.parseColor("#FF999999"));
                tv_title2.setTextColor(Color.parseColor("#FF999999"));
                tv_title2.setBackgroundResource(R.drawable.shape_bg_limited_time_title);
                tv_time3.setTextColor(Color.parseColor("#FFFF005E"));
                tv_title3.setTextColor(Color.parseColor("#FFFFFF"));
                tv_title3.setBackgroundResource(R.drawable.shape_bg_limitedskill_time_title);
                viewpager2.setCurrentItem(auplay(2));
                break;
        }
    }


    public int auplay(int currt) {
        int index = viewpager2.getCurrentItem() % 3;

        if (currt == 1) {
            if (index == 1) {
                return viewpager2.getCurrentItem();
            } else if (index == 2) {
                return viewpager2.getCurrentItem() - 1;
            } else {
                return viewpager2.getCurrentItem() + 1;
            }

        } else if (currt == 2) {
            if (index == 1) {
                return viewpager2.getCurrentItem() + 1;
            } else if (index == 2) {
                return viewpager2.getCurrentItem();
            } else {
                return viewpager2.getCurrentItem() + 2;
            }
        } else {
            if (index == 1) {
                return viewpager2.getCurrentItem() - 1;
            } else if (index == 2) {
                return viewpager2.getCurrentItem() - 2;
            } else {
                return viewpager2.getCurrentItem();
            }
        }

    }

    public void onDestroyView() {
        if (count_time_view != null && mObserver != null) {
            count_time_view.unregisterDataSetObserver(mObserver);
        }
    }
}
