package com.by.lizhiyoupin.app.io.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/11 14:40
 * Summary:
 */
public class FansDataBean {

    /**
     * fansList : [{"allProfit":0,"avatar":"","createTime":"2019-11-01 15:47:26","fansCount":0,"lastMonthEstimate":0,"name":"plus运营商(普通)","nowMonthEstimate":0,"uid":103},{"allProfit":0,"avatar":"","createTime":"2019-11-01 15:54:07","fansCount":0,"lastMonthEstimate":0,"name":"运营商plus(运营商)","nowMonthEstimate":0,"uid":104},{"allProfit":0,"avatar":"https://lzyp-static.oss-cn-hangzhou.aliyuncs.com/image/default_head_img.png","createTime":"2019-11-08 11:44:48","fansCount":0,"lastMonthEstimate":0,"name":"186****0130","nowMonthEstimate":0,"uid":138}]
     * nowFansCount : 0
     */

    private int nowFansCount; //当天粉丝数量
    private List<FansListBean> fansList;

    public int getNowFansCount() {
        return nowFansCount;
    }

    public void setNowFansCount(int nowFansCount) {
        this.nowFansCount = nowFansCount;
    }

    public List<FansListBean> getFansList() {
        return fansList;
    }

    public void setFansList(List<FansListBean> fansList) {
        this.fansList = fansList;
    }

    public static class FansListBean implements Parcelable, Comparable<FansListBean> {
        /**
         * allProfit : 0
         * avatar :
         * createTime : 2019-11-01 15:47:26
         * fansCount : 0
         * lastMonthEstimate : 0
         * name : plus运营商(普通)
         * nowMonthEstimate : 0
         * uid : 103
         * level: 会员等级 1普通会员 2超级会员 3超级会员Plus 4运营商 5运营商Plus
         */

        private double allProfit;//累计收益
        private String avatar;//头像
        private String createTime;
        private int fansCount;//粉丝数量
        private double lastMonthEstimate;//上月预估收益
        private String name;//昵称
        private double nowMonthEstimate;//本月预估收益
        private Long uid; //会员ID
        private int level; //会员等级  1普通会员 2超级会员 3超级会员Plus 4运营商 5运营商Plus

        protected FansListBean(Parcel in) {
            allProfit = in.readDouble();
            avatar = in.readString();
            createTime = in.readString();
            fansCount = in.readInt();
            lastMonthEstimate = in.readDouble();
            name = in.readString();
            nowMonthEstimate = in.readDouble();
            if (in.readByte() == 0) {
                uid = null;
            } else {
                uid = in.readLong();
            }
            level = in.readInt();
        }

        public static final Creator<FansListBean> CREATOR = new Creator<FansListBean>() {
            @Override
            public FansListBean createFromParcel(Parcel in) {
                return new FansListBean(in);
            }

            @Override
            public FansListBean[] newArray(int size) {
                return new FansListBean[size];
            }
        };

        public double getAllProfit() {
            return allProfit;
        }

        public void setAllProfit(double allProfit) {
            this.allProfit = allProfit;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getFansCount() {
            return fansCount;
        }

        public void setFansCount(int fansCount) {
            this.fansCount = fansCount;
        }

        public double getLastMonthEstimate() {
            return lastMonthEstimate;
        }

        public void setLastMonthEstimate(double lastMonthEstimate) {
            this.lastMonthEstimate = lastMonthEstimate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getNowMonthEstimate() {
            return nowMonthEstimate;
        }

        public void setNowMonthEstimate(double nowMonthEstimate) {
            this.nowMonthEstimate = nowMonthEstimate;
        }

        public Long getUid() {
            return uid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof FansListBean)) return false;
            //注意重写了 equals
            FansListBean listBean = (FansListBean) o;
            return getUid() != null ? getUid().equals(listBean.getUid()) : listBean.getUid() == null;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(getAllProfit());
            result = (int) (temp ^ (temp >>> 32));
            result = 31 * result + (getAvatar() != null ? getAvatar().hashCode() : 0);
            result = 31 * result + (getCreateTime() != null ? getCreateTime().hashCode() : 0);
            result = 31 * result + getFansCount();
            temp = Double.doubleToLongBits(getLastMonthEstimate());
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            result = 31 * result + (getName() != null ? getName().hashCode() : 0);
            temp = Double.doubleToLongBits(getNowMonthEstimate());
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            result = 31 * result + (getUid() != null ? getUid().hashCode() : 0);
            result = 31 * result + getLevel();
            return result;
        }

        @Override
        public String toString() {
            return "FansListBean{" +
                    "allProfit=" + allProfit +
                    ", avatar='" + avatar + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", fansCount=" + fansCount +
                    ", lastMonthEstimate=" + lastMonthEstimate +
                    ", name='" + name + '\'' +
                    ", nowMonthEstimate=" + nowMonthEstimate +
                    ", uid=" + uid +
                    ", level=" + level +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(allProfit);
            dest.writeString(avatar);
            dest.writeString(createTime);
            dest.writeInt(fansCount);
            dest.writeDouble(lastMonthEstimate);
            dest.writeString(name);
            dest.writeDouble(nowMonthEstimate);
            if (uid == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeLong(uid);
            }
            dest.writeInt(level);
        }

        @Override
        public int compareTo(FansListBean o) {
            if (uid != null && uid.equals(o.uid)) {
                return 0;
            }
            if (this.allProfit-o.allProfit>= 0) {
                return 1;//正序
            } else {
                return -1;//倒叙
            }
        }
    }

    @Override
    public String toString() {
        return "FansDataBean{" +
                "nowFansCount=" + nowFansCount +
                ", fansList=" + fansList +
                '}';
    }
}
