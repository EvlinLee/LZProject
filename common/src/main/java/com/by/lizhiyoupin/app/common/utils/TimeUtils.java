package com.by.lizhiyoupin.app.common.utils;

import android.os.SystemClock;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/20 10:04
 * Summary:
 */
public class TimeUtils {
    private static long lastOperationTime;
    private static long YI = 100000000;
    private static long WAN = 10000;
    private static SimpleDateFormat mDateFormat = new SimpleDateFormat();
    private static Calendar mNowCalendar = Calendar.getInstance();

    /**
     * 是否频繁触发
     *
     * @return true 是
     */
    public static boolean isFrequentOperation() {
        final long now = SystemClock.elapsedRealtime();
        final long gap = now - lastOperationTime;
        if (gap < 500) {
            return true;
        } else {
            lastOperationTime = now;
            return false;
        }
    }

    private static void updateDate() {
        mNowCalendar.setTimeInMillis(System.currentTimeMillis());
    }


    /**
     * 得到当前时间 秒
     *
     * @return
     */
    public static int systemCurrentTimeSeconds() {
        return (int) (System.currentTimeMillis() / 1000);
    }


    /**
     * 2个日期格式转换 yyyy-MM-dd HH:mm:ss转换成yyyy.MM.dd
     * @see #transDateToDateString
     * @param timeFormat 需要转换的日期 如 2019-10-14 14:21:05
     * @return
     */
    public static String getFormatEndTime(String timeFormat) {
        SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfh = new SimpleDateFormat("yyyy.MM.dd");
        String nowTime2 = "";
        try {
            nowTime2 = sdfh.format(sdfx.parse(timeFormat));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowTime2;
    }

    /**
     *
     * @param date  如 2019-10-14 14:21:05
     * @param formType 想要赚的date的格式 如 yyyy-MM-dd HH:mm:ss
     * @param type 想要的格式
     * @return
     */
    public static String transDateToDateString(String date,String formType,int type){
        if (TextUtils.isEmpty(date)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formType);
        try {
            Date millionDate = sdf.parse(date);
            String format = "yyyy-MM-dd";
            if (type == 0) {
                format = "yyyy-MM-dd";
            } else if (type == 1) {
                format = "yyyy/MM/dd";
            } else if (type == 2) {
                format = "yyyyMMdd";
            } else if (type == 3) {
                format = "yyyy.MM.dd HH:mm:ss";
            } else if (type == 4) {
                format = "yyyy-MM-dd HH:mm:ss";
            } else if (type == 5) {
                format = "yyyy/MM/dd HH:mm:ss";
            } else if (type == 6) {
                format = "yyyyMMddHHmmss";
            } else if (type == 7) {
                format = "yyyy.MM.dd";
            }
            mDateFormat.applyPattern(format);
            return mDateFormat.format(millionDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 如2012-12-13 13:26:54 转2012.12.13 13:26:54
     * String 类型的date转另一个格式的String Data
     *
     * @param date
     * @param type 想要转换成的结果类型
     * @return
     */
    public static String transDateToDateString(String date, int type) {
        if (TextUtils.isEmpty(date)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date millionDate = sdf.parse(date);
            String format = "yyyy-MM-dd";
            if (type == 0) {
                format = "yyyy-MM-dd";
            } else if (type == 1) {
                format = "yyyy/MM/dd";
            } else if (type == 2) {
                format = "yyyyMMdd";
            } else if (type == 3) {
                format = "yyyy.MM.dd HH:mm:ss";
            } else if (type == 4) {
                format = "yyyy-MM-dd HH:mm:ss";
            } else if (type == 5) {
                format = "yyyy/MM/dd HH:mm:ss";
            } else if (type == 6) {
                format = "yyyyMMddHHmmss";
            } else if (type == 7) {
                format = "yyyy.MM.dd";
            }
            mDateFormat.applyPattern(format);
            return mDateFormat.format(millionDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 转换时间格式 时间错转 时分
     * 11:32
     *
     * @param time
     * @return
     */
    public static String timeHoursAndMinute(long time) {
        mDateFormat.applyPattern("HH:mm");
        return mDateFormat.format(time);
    }

    /**
     * 时间戳转日期格式
     *
     * @param format    如“yyyy-MM-dd”
     * @param timestamp 毫秒
     * @return
     */
    public static String getTimeString(String format, long timestamp) {
        final Date date = new Date(timestamp < 0 ? System.currentTimeMillis() : timestamp);
        format = format == null ? "yyyyMMdd_HHmmss" : format;
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 得到当前时间的日期
     *
     * @param format
     * @return
     */
    public static String getCurrentTimeString(String format) {
        return getTimeString(format, -1);
    }

    /**
     * 比较两个时间的大小
     *
     * @param DATE1
     * @param DATE2
     * @return 1   DATE1 大，2  DATE2 大,0相等
     */
    public static int compare2date(String DATE1, String DATE2) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return 2;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }


    /**
     * 判断是否是同一天
     *
     * @return
     */
    public static boolean isTheSameDay(final long timeA, final long timeB) {
        final Calendar calendarA = Calendar.getInstance();
        calendarA.setTimeInMillis(timeA);
        final Calendar calendarB = Calendar.getInstance();
        calendarB.setTimeInMillis(timeB);

        return calendarA.get(Calendar.YEAR) == calendarB.get(Calendar.YEAR)
                && calendarA.get(Calendar.DAY_OF_YEAR) == calendarB.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 判断是否是同一天
     *
     * @return
     */
    private static boolean isSameDay(final Calendar calendar1, final Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 判断是否是同一年
     *
     * @return
     */
    private static boolean isSameYear(final Calendar calendar1, final Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
    }

    /**
     * 判断是否是同一年
     *
     * @param timeA 毫秒
     * @param timeB 毫秒
     * @return
     */
    public static boolean isSameYear(final long timeA, final long timeB) {
        final Calendar calendarA = Calendar.getInstance();
        calendarA.setTimeInMillis(timeA);
        final Calendar calendarB = Calendar.getInstance();
        calendarB.setTimeInMillis(timeB);
        return isSameYear(calendarA, calendarB);
    }

    /**
     * 判断是否是同一年
     *
     * @param dateA
     * @param dateB
     * @return
     */
    public static boolean isSameYear(final String dateA, final String dateB) {
        long timeA = transForYearAndMonthAndDayToMillions(dateA);
        long timeB = transForYearAndMonthAndDayToMillions(dateB);
        return isSameYear(timeA, timeB);
    }

    /**
     * 判断是否同一周
     *
     * @param nowCalendar
     * @param calendar
     * @return
     */
    public static boolean isSameWeek(final Calendar nowCalendar, final Calendar calendar) {
        if (nowCalendar.get(Calendar.WEEK_OF_YEAR) == calendar.get(Calendar.WEEK_OF_YEAR)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否昨天
     *
     * @param nowCalendar
     * @param calendar
     * @return
     */
    private static boolean isYesterday(final Calendar nowCalendar, final Calendar calendar) {
        nowCalendar.add(Calendar.DAY_OF_YEAR, -1); // 转为昨天
        final boolean b = isSameDay(nowCalendar, calendar);
        nowCalendar.add(Calendar.DAY_OF_YEAR, 1); // 转回今天
        return b;
    }

    /**
     * 时间对象转为字符串
     *
     * @param date Date对象
     * @param type 格式0=yyyy-MM-dd,1=yyyy/MM/dd,2=yyyyMMdd,3=MM/dd/yy,4=yyyy-MM-dd
     *             HH:mm:ss
     * @return 时间字符串
     */
    public static String DateToString(Date date, int type) {
        String format = "yyyy-MM-dd";
        if (type == 0) {
            format = "yyyy-MM-dd";
        } else if (type == 1) {
            format = "yyyy/MM/dd";
        } else if (type == 2) {
            format = "yyyyMMdd";
        } else if (type == 3) {
            format = "MM/dd/yy";
        } else if (type == 4) {
            format = "yyyy-MM-dd HH:mm:ss";
        } else if (type == 5) {
            format = "yyyy/MM/dd HH:mm:ss";
        } else if (type == 6) {
            format = "yyyyMMddHHmmss";
        }else if (type==7){
            format="yyyy年MM月dd日 HH时";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        if (date != null) {
            return formatter.format(date);
        } else {
            return "";
        }
    }

    /**
     * 时间戳转日期格式
     *
     * @param timeSamp 毫秒
     * @param format   如 "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String transForDate(long timeSamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Date temp = new Date(timeSamp);
        try {
            return sdf.format(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * "yyyy-MM-dd HH:mm:ss" 转 时间戳
     *
     * @param data
     * @return
     */
    public static long transForDateToTimeSamp(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(data).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }


    public static String timeStamp2DateForTeacher(long timestamp) {
        updateDate();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        String format;
        if (isSameDay(mNowCalendar, calendar)) { // 今天
            format = "HH:mm";
        } else if (isYesterday(mNowCalendar, calendar)) { // 昨天
            format = "HH:mm";
            mDateFormat.applyPattern(format);
            return "昨天 " + mDateFormat.format(calendar.getTime());
        } else if (isSameYear(mNowCalendar, calendar)) { // 同一年
            //显示日期
            format = "MM-dd HH:mm";
        } else {// 不同年，显示年份
            format = "yyyy-MM-dd HH:mm";
        }
        mDateFormat.applyPattern(format);
        return mDateFormat.format(calendar.getTime());
    }

    public static String timeStamp2Date(long timestamp) {
        updateDate();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        String format;
        if (isSameDay(mNowCalendar, calendar)) { // 今天
            format = "HH:mm";
        } else if (isYesterday(mNowCalendar, calendar)) { // 昨天
            return "昨天";
        } else if (isSameYear(mNowCalendar, calendar)) { // 同一年
            //显示日期
            format = "MM-dd";
        } else {// 不同年，显示年份
            format = "yyyy-MM-dd";
        }
        mDateFormat.applyPattern(format);
        return mDateFormat.format(calendar.getTime());
    }

    /**
     * 日期转年月日时的时间戳，分秒都是0
     * @param date
     * @return
     */
    public static long transForDateToTime(Date date){
        mDateFormat.applyPattern("yyyy-MM-dd HH:00:00");
        String time = mDateFormat.format(date);
        try {
            return mDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 日期转时间戳
     *
     * @param date
     * @return
     */
    public static long transForYearAndMonthAndDayToMillions(String date) {
        if (TextUtils.isEmpty(date)) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 返回相隔时间 多少天
     *
     * @param timeStamp
     * @return 单位天
     */
    public static int convertTimeToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis();
        long time = curTime - timeStamp;
        int days = (int) (time / (1000 * 24 * 3600));
        return days;
    }


    /**
     * @param mss
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     */
    public static String formatDuring(long mss) {

        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + " days " + hours + " hours " + minutes + " minutes "
                + seconds + " seconds ";
    }

    /**
     * 距离当前 多久
     *
     * @param time
     * @return
     */
    public static String getBarrageTime(long time) {
        long current = System.currentTimeMillis();
        time = time * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String now = sdf.format(new Date(current));
        String before = sdf.format(new Date(time));

        int yearNow = Integer.valueOf(now.substring(0, 4));
        int yearBefor = Integer.valueOf(before.substring(0, 4));
        if (yearBefor != yearNow) {

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
            return sdf1.format(time);
        }
        long diff = (current - time) / 1000;
        if (diff < 60 * 60) {
            long l = diff / 60;
            if (l <= 0) {
                return "刚刚";
            }
            return l + "分钟前";
        } else if (diff >= 60 * 60 && diff < 24 * 60 * 60) {
            return diff / (60 * 60) + "小时前";
        } else if (diff >= 24 * 60 * 60) {
            return before.substring(4, 6) + "月" + before.substring(6, 8) + "日";
        }

        return "";
    }

    /**
     * 间隔时间是否大于 给定值
     *
     * @param last    上次
     * @param now     本次
     * @param diffMin 间隔时间是否大于 diffMin 分钟
     * @return true  间隔时间大于等于给定值
     */
    public static boolean differTime(final long last, final long now, long diffMin) {
        long diff = (now - last) / (1000 * 60);
        return diff >= diffMin;
    }

    /**
     * 间隔时间是否大于 给定值
     *
     * @param last     上次
     * @param now      本次
     * @param diffMill 间隔时间是否大于 diffMill 毫秒
     * @return true  间隔时间大于等于给定值
     */
    public static boolean differTimeMill(final long last, final long now, long diffMill) {
        long diff = now - last;
        return diff >= diffMill;
    }

    public static int getLastMonthInt(){
        SimpleDateFormat format = new SimpleDateFormat("MM");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) -1); // 设置为上一个月
        date = calendar.getTime();
        String accDate = format.format(date);
        int lastMonth;
        try {
            lastMonth = Integer.valueOf(accDate);
        }catch (Exception e){
            e.printStackTrace();
            lastMonth=1;
        }
        return lastMonth;
    }
}
