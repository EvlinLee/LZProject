package com.by.lizhiyoupin.app.io.config;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.by.lizhiyoupin.app.common.utils.NativeBase64Utils;
import com.by.lizhiyoupin.app.common.utils.storage.SPUtils;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/22 13:45
 * Summary:
 */
public class AccountConfig {
    // 用户信息
    private static final String ACCLOUNT_INFO = "account_info";
    private static volatile UserInfoBean configs;
    private static Gson gson;

    public static synchronized UserInfoBean getAccountInfo() {
            if(configs==null){
                configs=new UserInfoBean();
            }
        return configs;
    }

    public static synchronized void setAccountInfo(UserInfoBean infoBean) {
        configs = infoBean;
    }

    public static boolean isLogin() {
        return configs != null && configs.getId() > 0;
    }

    private static void initGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(byte[].class, new ByteTypeAdapter())
                    .create();
        }
    }

    public static void saveAccountInfoPref(final UserInfoBean userInfoBean) {
        try {
            initGson();
            SharedPreferences.Editor editor = SPUtils.getDefault().edit();
            editor.putString(ACCLOUNT_INFO, userInfoBean != null ? gson.toJson(userInfoBean) : "");
            editor.commit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static UserInfoBean getAccountInfoPref() {
        try {
            initGson();
            String jsonInfo = SPUtils.getDefault().getString(ACCLOUNT_INFO);
            if (TextUtils.isEmpty(jsonInfo)) {
                return null;
            }
            return gson.fromJson(jsonInfo, UserInfoBean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static void removeAccountInfo() {
        // 退出登陆，清除Token状态
        SPUtils.getDefault().putString(ACCLOUNT_INFO, "");
    }

    public static class ByteTypeAdapter extends TypeAdapter<byte[]> {
        @Override
        public void write(JsonWriter out, byte[] value) throws IOException {
            out.value(NativeBase64Utils.encode(value));
        }

        @Override
        public byte[] read(JsonReader in) throws IOException {
            String string = in.nextString();
            return NativeBase64Utils.decode(string);
        }
    }
}
