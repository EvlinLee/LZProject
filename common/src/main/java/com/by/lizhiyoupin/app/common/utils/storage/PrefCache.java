package com.by.lizhiyoupin.app.common.utils.storage;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

/**
 * 业务类重复比较高的存储计数工具，方便业务层直接调用
 * {@link #expireInDays(int)} 当天有效记录
 * {@link #expireInDays(int)} n天内记录有效
 * {@link #forDayTimes(int)} 当天可记录次数，每天更新
 * {@link #forMaxTimes(int)} 最大可计数次数
 */
@SuppressWarnings("ALL")
public class PrefCache {

    private static final String PREFRENCE_NAME = "pref_cache";

    /**
     *
     * @param key
     * @param value
     * @param expireTime 过期日期时间  秒
     */
    public static void putString(String key, String value, long expireTime) {
       SaveModel saveModel = new SaveModel<>(value, expireTime);
       SharedPreferences.Editor editor = getPreferences().edit();
       editor.putString(key, JSON.toJSONString(saveModel));
       editor.commit();
    }

    public static String getString(String key) {
        SaveModel model = getSaveModel(key);
        if(model != null){
            return model.isExpired() ? null : model.stringValue();
        }
        return "";
    }

    public static void updateTimes(String key, long expireTime) {
        updateTimes(key, expireTime, true);
    }

    public static void updateTimes(String key, long expireTime, boolean resetExpire) {
        SaveModel model = getSaveModel(key);
        if(model != null){
            if (resetExpire || model.expireTime <= 0 || model.isExpired()) {
                model.expireTime=expireTime;
                model.value = 0;
            }
            model.value = (model.intValue() + 1);
        } else {
            model = new SaveModel<>(0, expireTime);
        }
        putSaveModel(key, model);
    }

    public static Integer getTimes(String key) {
        SaveModel model = getSaveModel(key);
        if(model != null){
            return model.isExpired() ? 0 : model.intValue();
        }
        return 0;
    }

    public static void putBoolean(String key, Boolean value, int expireDays) {
        SaveModel<Boolean> model = getSaveModel(key);
        if (model == null) {
            model = new SaveModel<>();
        }
        model.value = value;
        model.expireInDays(expireDays);
        putSaveModel(key, model);
    }

    public static boolean getBoolean(String key, boolean def) {
        SaveModel<Boolean> model = getSaveModel(key);
        if(model != null){
            return model.isExpired() ? def : (model.value != null ? model.value : def);
        }
        return def;
    }

    /**
     * 存储 SaveModel 对象
     * @param key String
     * @param model SaveModel
     */
    public static void putSaveModel(String key, SaveModel model) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, JSON.toJSONString(model));
        editor.commit();
    }

    private static SaveModel getSaveModel(String key) {
        try {
            String json=getSaveModelString(key);
            if (!TextUtils.isEmpty(json)) {
                SaveModel<?> spSaveModel= new Gson().fromJson(json, new TypeToken<SaveModel<?>>(){}.getType());
//                SaveModel<T> spSaveModel=JSON.parseObject(json, new TypeReference<SaveModel<T>>(){}.getType());
                return spSaveModel;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private static String getSaveModelString(String key) {
        return getPreferences().getString(key,"");
    }

    /**
     * 计算current时间和存储时间的间隔毫秒数
     * @param key String
     * @param current long
     * @return int 间隔毫秒数
     */
    public static long timeInterval(String key, long current) {
        String json = getPreferences().getString(key,"");
        JSONObject jsonObject = JSON.parseObject(json);
        long saveTime = jsonObject.getLong("saveTime");
        if (saveTime > 0) {
            return current - saveTime;
        } else {
            return current;
        }
    }

    /**
     * 计算current时间和存储时间的间隔天数
     * @param key String
     * @param current long
     * @return int 间隔毫秒数
     */
    public static long dayInterval(String key, long current) {
        long diff = timeInterval(key, current);
        return diff > 0 ? diff / (1000 * 3600 * 24) : 0;
    }

    private static PrefsProxy getPreferences() {
        return SPUtils.getPreferences(PREFRENCE_NAME);
    }

    /**
     * 设置过期时间为今天凌晨
     * @return SaveModel
     */
    public static long expireToday() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime() / 1000;

    }

    /**
     * day天后过期
     * @param day
     * @return SaveModel<T>
     */
    public static long expireInDays(int day) {
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + day);
        return date.getTime().getTime() / 1000;
    }

    /**
     * day天后过期
     * @param day
     * @return SaveModel<T>
     */
    public static long expireInMinute(int minute) {
        Date date = new Date();
        return (date.getTime() / 1000) + (minute * 60);
    }

    /**
     * 设置最大记录次数
     * @return SaveModel<T>
     */
    public static SaveModel<Integer> forMaxTimes(int maxTimes) {
        SaveModel<Integer> model = new SaveModel<>(0);
        model.maxTimes = maxTimes;
        return model;
    }

    /**
     * 每天可记录次数
     * @param times int
     * @return SaveModel<Integer>
     */
    public static SaveModel<Integer> forDayTimes(int times) {
        SaveModel<Integer> model = new SaveModel<>(0);
        model.maxTimes = times;
        model.everyDay = true;
        return model;
    }

    @Keep
    public static class SaveModel<T> {
        public @NonNull
        T value;

        // 记录数据时间
        public long saveTime;

        // 过期时间
        public long expireTime;

        // 最大记录次数
        public int maxTimes;

        // 是否每天更新记录
        public boolean everyDay;

        public SaveModel() {}

        public SaveModel(@NonNull T value) {
            this(value, 0);
        }

        public SaveModel(@NonNull T value, long expireTime) {
            this.value = value;
            this.saveTime = TimeUtils.systemCurrentTimeSeconds();
            this.expireTime = expireTime;
        }

        public SaveModel<T> saveTime(long saveTime) {
            this.saveTime = saveTime;
            return this;
        }

        public SaveModel<T> expireTime(long expireTime) {
            this.expireTime = expireTime;
            return this;
        }

        public SaveModel<T> expireInDays(int days) {
            Calendar date = Calendar.getInstance();
            date.setTime(new Date());
            date.set(Calendar.DATE, date.get(Calendar.DATE) + days);
            expireTime = date.getTime().getTime() / 1000;
            return this;
        }

        public SaveModel<T> expireToday() {
            SaveModel<T> model = new SaveModel<>(value);
            long now = TimeUtils.systemCurrentTimeSeconds();
            long daySecond = 60 * 60 * 24;
            model.expireTime = (now - (now + 8 * 3600) % daySecond);
            return this;
        }

        public SaveModel<T> maxTimes(int maxTimes) {
            this.maxTimes = maxTimes;
            return this;
        }

        public SaveModel<T> everyDay(boolean everyDay) {
            this.everyDay = everyDay;
            return this;
        }

        /**
         * 是否已过期
         * @return TRUE / FALSE
         */
        public boolean isExpired() {
            return expireTime <= 0 || expireTime < TimeUtils.systemCurrentTimeSeconds();
        }

        /**
         * 是否未超过次数
         * @return TRUE / FALSE
         */
        public boolean isInMaxTimes() {
            try {
                if (value instanceof Integer) {
                    return (Integer) value < maxTimes;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return false;
        }

        /**
         * 当前记录存储时间是否在当天
         * @return TRUE / FALSE
         */
        public boolean isInSameday() {
            return TimeUtils.isTheSameDay(saveTime, TimeUtils.systemCurrentTimeSeconds());
        }

        public String stringValue() {
            return value != null ? value.toString() : "";
        }

        public boolean booleanValue() {
            try {
                return Boolean.valueOf(stringValue());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return false;
        }

        public int intValue() {
            return (int) doubleValue();
        }

        public double doubleValue() {
            try {
                return Double.parseDouble(stringValue());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return 0;
        }
    }
}
