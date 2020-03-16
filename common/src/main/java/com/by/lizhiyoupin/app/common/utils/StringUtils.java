package com.by.lizhiyoupin.app.common.utils;

import java.text.NumberFormat;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/9 10:41
 * Summary:
 */
public class StringUtils {
    /**
     * 取得浮点数保留小数点后两位的字符串
     */
    public static String getFormatedFloat(final double value) {
        if (Double.isNaN(value)) {
            return "--";
        }
        return getFormatedFloat((float) value, 2);
    }

    /**
     * 取得浮点数保留小数点后两位的字符串
     *
     * @param value
     * @param precision 保留小数位数
     * @return
     */
    public static String getFormatedFloat(final float value, int precision) {
        if (Float.isNaN(value)) {
            return "--";
        }
        return getFormattedFloat(value, precision);
    }

    /**
     * 取得浮点数四舍五入保留小数点后两位的字符串
     */
    public static String getRoundedFloat(final float value) {
        return getFormatedFloat(value);
    }

    /**
     * @param value
     * @return 所有数据都保留2位小数
     */
    public static String getFormattedDouble(final double value) {
        return getFormatedFloat((float) value, 2);
    }

    /**
     * 返回 ¥1.22
     *
     * @param value
     * @return
     */
    public static String getMoneyFormattedDouble(final double value) {
        if (Double.isNaN(value)) {
            return "--";
        }
        return "¥" + getFormattedFloat((float) value, 2);
    }

    /**
     * @param value
     * @return 如果数据=0则返回0 不保留小数
     */
    public static String getFormattedFloat0(final float value) {
        if (Double.isNaN(value)) {
            return "--";
        }
        if (value == 0) {
            return "0";
        }
        return getFormattedFloat(value, 2);
    }

    /**
     * @param value
     * @return 如果数据=0则返回0 不保留小数
     */
    public static String getFormattedDouble0(final double value) {
        if (Double.isNaN(value)) {
            return "--";
        }
        if (value == 0) {
            return "0";
        }
        return getFormattedDouble(value, 2);
    }

    /**
     *
     * @param value
     * @return 如果返回数据 等于整数则不要小数,否则传什么返会什么
     */
    public static String getFormattedDoubleOrInt(final double value) {
        if (Double.isNaN(value)) {
            return "--";
        }
        int temp = (int) value;
        if (value == temp) {
            return String.valueOf(temp);
        }
        return String.valueOf(value);
    }

    public static String getFormattedDouble(final double value, int precision) {
        return getFormatedFloat((float) value, precision);
    }

    public static String getFormattedFloat(final float value, int precision) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        switch (precision) {
            case 0:
                nf.setMaximumFractionDigits(0);
                nf.setMinimumFractionDigits(0);
                break;
            case 1:
                nf.setMaximumFractionDigits(1);
                nf.setMinimumFractionDigits(1);
                break;
            case 2:
                nf.setMaximumFractionDigits(2);
                nf.setMinimumFractionDigits(2);
                break;
            case 3:
                nf.setMaximumFractionDigits(3);
                nf.setMinimumFractionDigits(3);
                break;
            case 4:
                nf.setMaximumFractionDigits(4);
                nf.setMinimumFractionDigits(4);
                break;
            case 5:
                nf.setMaximumFractionDigits(5);
                nf.setMinimumFractionDigits(5);
                break;
            case 6:
                nf.setMaximumFractionDigits(6);
                nf.setMinimumFractionDigits(6);
                break;
            default:
                nf.setMaximumFractionDigits(2);
                nf.setMinimumFractionDigits(2);
                break;
        }
        return nf.format(value);
    }

    public static String getIVolume(long volume) {
        if (volume >= 10000) {
            return volume / 10000 + "万+";
        } else {
            return String.valueOf(volume);
        }
    }
    public static String getThumbsCount(long count) {
        if (count >= 10000) {
            return StringUtils.getFormattedDouble((double)count / 10000) + "万";
        } else {
            return String.valueOf(count);
        }
    }
}
