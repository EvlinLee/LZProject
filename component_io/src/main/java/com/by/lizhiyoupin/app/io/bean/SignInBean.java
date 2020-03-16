package com.by.lizhiyoupin.app.io.bean;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/9 19:30
 * Summary: 签到bean
 */
public class SignInBean {
    /**
     * signedDays : 2
     * signDescription : 周一至周六签满6天，周日额外瓜分5千万
     * signRemind : 1
     * userSignBonusVOList : [{"dateName":"昨日","dateNameType":0,"signStatus":1,"todayAllBonusAmount":1.11,"doubleStatus":1,"seconds":121}]
     * userSignWeekInfoVOList : [{"weekDays":1,"signStatus":1}]
     */
    private double allEarnings;//会员累计收益金额
    private int signedDays;//已签到天数
    private String signDescription;//签到说明
    private int signRemind;//签到提醒 0 未提醒 1 已提醒
    private List<UserSignBonusVOListBean> userSignBonusVOList;//签到三天（昨日、今天、明天（周几））信息集合
    private List<UserSignWeekInfoVOListBean> userSignWeekInfoVOList;//一周签到天数和状态

    public double getAllEarnings() {
        return allEarnings;
    }

    public void setAllEarnings(double allEarnings) {
        this.allEarnings = allEarnings;
    }

    public int getSignedDays() {
        return signedDays;
    }

    public void setSignedDays(int signedDays) {
        this.signedDays = signedDays;
    }

    public String getSignDescription() {
        return signDescription;
    }

    public void setSignDescription(String signDescription) {
        this.signDescription = signDescription;
    }

    public int getSignRemind() {
        return signRemind;
    }

    public void setSignRemind(int signRemind) {
        this.signRemind = signRemind;
    }

    public List<UserSignBonusVOListBean> getUserSignBonusVOList() {
        return userSignBonusVOList;
    }

    public void setUserSignBonusVOList(List<UserSignBonusVOListBean> userSignBonusVOList) {
        this.userSignBonusVOList = userSignBonusVOList;
    }

    public List<UserSignWeekInfoVOListBean> getUserSignWeekInfoVOList() {
        return userSignWeekInfoVOList;
    }

    public void setUserSignWeekInfoVOList(List<UserSignWeekInfoVOListBean> userSignWeekInfoVOList) {
        this.userSignWeekInfoVOList = userSignWeekInfoVOList;
    }

    public static class UserSignBonusVOListBean {
        /**
         * dateName : 昨日
         * dateNameType : 0
         * signStatus : 1
         * todayAllBonusAmount : 1.11
         * doubleStatus : 1
         * seconds : 121
         */

        private String dateName;//日期名称
        private int dateNameType;//日期数值： 0 昨日 1 今天 2 明天（周几）
        private int signStatus;//领取状态 0 未领取 1 已领取
        private double todayAllBonusAmount;//当日领到红包总金额
        private int doubleStatus;//是否翻倍 0 未翻倍 1 已翻倍
        private long seconds;//距离明天零点的时间差 秒

        public String getDateName() {
            return dateName;
        }

        public void setDateName(String dateName) {
            this.dateName = dateName;
        }

        public int getDateNameType() {
            return dateNameType;
        }

        public void setDateNameType(int dateNameType) {
            this.dateNameType = dateNameType;
        }

        public int getSignStatus() {
            return signStatus;
        }

        public void setSignStatus(int signStatus) {
            this.signStatus = signStatus;
        }

        public double getTodayAllBonusAmount() {
            return todayAllBonusAmount;
        }

        public void setTodayAllBonusAmount(double todayAllBonusAmount) {
            this.todayAllBonusAmount = todayAllBonusAmount;
        }

        public int getDoubleStatus() {
            return doubleStatus;
        }

        public void setDoubleStatus(int doubleStatus) {
            this.doubleStatus = doubleStatus;
        }

        public long getSeconds() {
            return seconds;
        }

        public void setSeconds(long seconds) {
            this.seconds = seconds;
        }
    }

    public static class UserSignWeekInfoVOListBean {
        /**
         * weekDays : 1
         * signStatus : 1
         */

        private int weekDays;//周几的对应数值 1 周一 2 周二 3 周三 4 周四 5 周五 6 周六 7 周日
        private int signStatus;//签到状态： 0 未签到 1 已签到 2断签

        public int getWeekDays() {
            return weekDays;
        }

        public void setWeekDays(int weekDays) {
            this.weekDays = weekDays;
        }

        public int getSignStatus() {
            return signStatus;
        }

        public void setSignStatus(int signStatus) {
            this.signStatus = signStatus;
        }
    }
}
