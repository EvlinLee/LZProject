package com.by.lizhiyoupin.app.io.bean;

/**
 * data:2019/11/22
 * author:jyx
 * function:
 */
public class InformationBean {

        /**
         * createTime : 11/15 15:42
         * readStatus : 1
         * title : 测试的系统消息
         * type : 1
         */

        private String createTime;
        private int readStatus;
        private String title;
        private int type;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(int readStatus) {
            this.readStatus = readStatus;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
}
