package com.by.lizhiyoupin.app.user;

import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.bean.AlipayBean;
import com.by.lizhiyoupin.app.io.bean.LimitedTimeListBean;
import com.by.lizhiyoupin.app.io.bean.LimitedTimeSkillBean;
import com.by.lizhiyoupin.app.io.bean.OrderLogisticsBean;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.service.ApiService;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

/**
 * data:2019/11/5
 * author:jyx
 * function:
 */
public class SettingRequestManager {

    /**
     * 设置
     * @param apiToken  用户token
     * @param gender   性别: 0-未知 1-男 2-女
     * @param name    昵称
     * @param avatar  头像
     * @return
     */
    public static Observable<BaseBean<UserInfoBean>> requestSetting(
                                                                    String apiToken,
                                                                    String gender,
                                                                    String wechat,
                                                                    String name,
                                                                    String avatar) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("apiToken", apiToken);
        hashMap.put("gender", gender);
        hashMap.put("name", name);
        hashMap.put("avatar", avatar);
        hashMap.put("wechat", wechat);


        return ApiService.getSettingApi().requestSetting(hashMap);
    }

    /**
     * 设置
     * @param apiToken  用户token
     * @param gender   性别: 0-未知 1-男 2-女
     * @param name    昵称
     * @param avatar  头像
     * @return
     */
    public static Observable<BaseBean<UserInfoBean>> requestSet(
                                                                    String apiToken,
                                                                    String gender,
                                                                    String name,
                                                                    String avatar,
                                                                    String wechat) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("apiToken", apiToken);
        hashMap.put("gender", gender);
        hashMap.put("name", name);
        hashMap.put("avatar", avatar);
        hashMap.put("wechat", wechat);



        return ApiService.getSettingApi().requestSetting(hashMap);
    }
    /**
     * 设置
     *
     * @param apiToken  用户token
     * @param gender   性别: 0-未知 1-男 2-女
     * @param name    昵称
     * @param avatar  头像
     * @return
     */
    public static Observable<BaseBean<UserInfoBean>> requestBirth(
                                                                    String apiToken,
                                                                    String gender,
                                                                     String userBirthday,
                                                                    String name,
                                                                    String avatar,
                                                                   String wechat) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("apiToken", apiToken);
        hashMap.put("gender", gender);
        hashMap.put("name", name);
        hashMap.put("avatar", avatar);
         hashMap.put("userBirthday", userBirthday);
        hashMap.put("wechat", wechat);



        return ApiService.getSettingApi().requestSetting(hashMap);
    }

    /**
     * 更改手机号
     * @param phone  手机号
     * @param apiToken  用户token
     * @param smdCode   验证码
     * @return
     */
    public static Observable<BaseBean<UserInfoBean>> requestPhone(String phone,
                                                                  String apiToken,
                                                                  String smdCode) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("apiToken", apiToken);
        hashMap.put("smdCode", smdCode);
        hashMap.put("phone", phone);



        return ApiService.getSettingApi().requestPhone(hashMap);
    }

    /**
     * 支付宝绑定
     * @param idCard         身份证号
     * @param alipayAccount  支付宝号
     * @param fullName       真实姓名
     * @param smdCode   验证码
     * @return
     */
    public static Observable<BaseBean<AlipayBean>> requestAlipay(
                                                                  String idCard,
                                                                   String alipayAccount,
                                                                   String fullName,
                                                                  String smdCode) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("idCard", idCard);
        hashMap.put("smdCode", smdCode);
        hashMap.put("alipayAccount", alipayAccount);
        hashMap.put("fullName", fullName);




        return ApiService.getSettingApi().requestAlipay(hashMap);
    }


    /**
     * 银行卡绑定
     * @param bankAccount  银行账号
     * @param bankName     银行名称
     * @param bankNickName  开户名
     * @param smsCode      验证码
     * @return
     */
    public static Observable<BaseBean<AlipayBean>> requestBank(
            String bankAccount,
            String bankName,
            String bankNickName,
            String smsCode) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("smsCode", smsCode);
        hashMap.put("bankAccount", bankAccount);
        hashMap.put("bankName", bankName);
        hashMap.put("bankNickName", bankNickName);



        return ApiService.getSettingApi().requestBank(hashMap);
    }


    /**
     *
     * 修改消息已读
     * @param type 消息类型
     * @param mids 修改id
     * @return
     */
    public static Observable<BaseBean<String>> requestMessageStatus(
            String type,
            String mids
    ) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("type", type);
        hashMap.put("mids", mids);


        return ApiService.getMessageApi().requestUserMessageStatus(hashMap);
    }


    /**
     *
     * 修改消息已读
     * @return
     */
    public static Observable<BaseBean<String>> requestInteractMessageStatus(String ids
    ) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ids", ids);


        return ApiService.getMessageApi().requestInteractMessageStatus(hashMap);
    }

    /**
     * 根据身份证号查询签约结果接口
     * @param idCard 身份证号
     * @return
     */
    public static Observable<BaseBean<AlipayBean>> requestAlipayBindFind(
            String idCard,
            String fullName
          ) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("idCard", idCard);
        hashMap.put("fullName", fullName);



        return ApiService.getSettingApi().requestAlipayBindFind(hashMap);
    }


    /**
     * 通过会员token获取会员账号信息
     *
     * @return
     */
    public static Observable<BaseBean<AlipayBean>> requestFindInfo() {

        return ApiService.getSettingApi().requestInfo();
    }


    /**
     *首页限时秒杀
     *
     * @return
     */
    public static Observable<BaseBean<List<LimitedTimeSkillBean>>> requestLitmitSkillTitle() {

        return ApiService.getActivityApi().requestLimitTimeSkillList();
    }

    /**
     *限时列表
     *
     * @return
     */
    public static Observable<BaseBean<LimitedTimeListBean>> requestLimitedTimeList(int hourType, int start) {

        return ApiService.getActivityApi().requestLimitedTimeList(hourType,start);
    }


    /**
     *99礼包订单物流信息
     *
     * @return
     */
    public static Observable<BaseBean<OrderLogisticsBean>> requestOrderLogistics(String company,String number) {

        return ApiService.getOrderApi().requestOrderLogistics(company,number);
    }
}
