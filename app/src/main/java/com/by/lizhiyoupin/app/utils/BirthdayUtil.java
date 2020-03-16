package com.by.lizhiyoupin.app.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.user.SettingRequestManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * data:2019/11/4
 * author:jyx
 * function:生日选择器
 */
public class BirthdayUtil {
    private volatile static BirthdayUtil sBirthdayUtil;
    private Context context;
    public BirthdayUtil(Context context) {
        this.context=context;
    }
    public static BirthdayUtil getInstance(Context context){
        if (sBirthdayUtil==null){
            sBirthdayUtil=new BirthdayUtil(context);
        }
        return sBirthdayUtil;
    }

    /**
     * 时间选择器
     * @param textView 显示选择时间的控件
     * @param title 选择器弹窗的标题
     * @param flag 返回时间的格式
     */
    public void showBirthdayDate(Context context, final TextView textView, String title, final int flag){


        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH,+100);// 选择时间的间隔
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1800,1,1);//设置起始年份
        Calendar endDate = Calendar.getInstance();
       new  TimePickerBuilder(context, new OnTimeSelectListener() {
           @Override
           public void onTimeSelect(Date date, View v) {
               String time = getTime(date,flag);// 返回的时间
               textView.setText(time);
               requestBirthday(textView);
           }
       })

                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setContentTextSize(20)//内容位子大小
                .setTitleText(title)//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#FF3388FF"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#999999"))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build()
                .show();

    }

    private void requestBirthday(TextView textView) {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        String apiToken = accountManager.getAccountInfo().getApiToken();
        String name = accountManager.getAccountInfo().getName();
        int gender = accountManager.getAccountInfo().getGender();
        String avatar = accountManager.getAccountInfo().getAvatar();
        String wechat = accountManager.getAccountInfo().getWechat();
        if (wechat==null){
            wechat="";
        }
        SettingRequestManager.requestBirth(apiToken, String.valueOf(gender),textView.getText().toString(),name,avatar,wechat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<UserInfoBean>>() {
                    @Override
                    public void onNext(BaseBean<UserInfoBean> userInfoBeanBaseBean) {
                        super.onNext(userInfoBeanBaseBean);
                        if (!userInfoBeanBaseBean.success()) {
                            onError(new Throwable(userInfoBeanBaseBean.msg));
                            return;
                        }

                        if (accountManager!=null&&userInfoBeanBaseBean.data!=null){

                            accountManager.saveAccountInfo(userInfoBeanBaseBean.data);
                        }
                        CommonToast.showToast("修改成功");


                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        CommonToast.showToast("修改失败");
                    }
                });
    }

    /**
     * 格式化时间
     * @param date 时间数据源
     * @param type 时间返回格式
     * @return
     */
    public String getTime(Date date ,int type) {//可根据需要自行截取数据显示
        SimpleDateFormat format = null;
        if (type==1){
            format = new SimpleDateFormat("yyyy-MM-dd");
        }else if (type==0){
            format = new SimpleDateFormat("HH:mm");
        }else {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }

        return format.format(date);
    }

}
