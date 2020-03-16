package com.by.lizhiyoupin.app.io.bean;

/**
 * data:2019/11/21
 * author:jyx
 * function:
 */
public class PlusTimeBean {

        /**
         * endTime : 2119-11-21 00:00:00
         * missionType : 1
         * startTime : 2019-11-21 14:57:48
         * status : 0
         */

        private String endTime;
        private int missionType;
        private String startTime;
        private int status;

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getMissionType() {
            return missionType;
        }

        public void setMissionType(int missionType) {
            this.missionType = missionType;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;

    }
}
