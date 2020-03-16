package com.by.lizhiyoupin.app.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.user.SettingRequestManager;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * data:2019/11/4
 * author:jyx
 * function:性别选择器
 */
public class SexUtil {
    private volatile static SexUtil sSexUtil;
    private Context context;
    public SexUtil(Context context) {
        this.context=context;
    }
    public static SexUtil getInstance(Context context){
        if (sSexUtil==null){
            sSexUtil=new SexUtil(context);
        }
        return sSexUtil;
    }

    public void showSex(Context context, final TextView textView){
        final ArrayList<String> list = new ArrayList<>();
        list.add("男");
        list.add("女");
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                String sex = list.get(options1);

                textView.setText(sex);
                requestSex(textView);

            }
        })
                .setCancelText("关闭")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setDividerColor(Color.parseColor("#999999"))
                .setTextColorCenter(Color.parseColor("#FF111111")) //设置选中项文字颜色
                .setSubmitColor(Color.parseColor("#FF528CDB"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#999999"))//取消按钮文字颜色
                .setTypeface(Typeface.DEFAULT)
                .setContentTextSize(20)
                .build();
         pvOptions.setPicker(list);

        pvOptions.show();


    }

    private void requestSex(TextView textView) {
        int gender=0;
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        String apiToken = accountManager.getAccountInfo().getApiToken();
        String name = accountManager.getAccountInfo().getName();
        String avatar = accountManager.getAccountInfo().getAvatar();
        String wechat = accountManager.getAccountInfo().getWechat();
        String userBirthday = accountManager.getAccountInfo().getUserBirthday();
        if (wechat==null){
            wechat="";

        }
        if (userBirthday==null){
            userBirthday="";
        }
        if (textView.getText().toString().equals("男")){
            gender=1;
        }else{
            gender=2;
        }
        SettingRequestManager.requestBirth(apiToken, String.valueOf(gender),userBirthday,name,avatar,wechat)
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
//                        LZLog.i(TAG, "修改成功");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        CommonToast.showToast("修改失败");
                    }
                });
    }
}
