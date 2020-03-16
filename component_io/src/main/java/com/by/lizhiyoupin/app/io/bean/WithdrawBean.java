package com.by.lizhiyoupin.app.io.bean;

/**
 * data:2019/11/20
 * author:jyx
 * function:
 */
public class WithdrawBean {
        /**
         * account : 15600716026
         * accountType : 0
         * amount : 105.0
         * bankAccount : 15600716026
         * bankName : 农行
         * commission : 0.0
         * createdTime : 1574253720000
         * fullName : 15600716026
         * remark :
         * status : 1
         * type : 3
         * updatedTime : 1574253720000
         */

        private String account;
        private int accountType;
        private double amount;
        private String bankAccount;
        private String bankName;
        private double commission;
        private long createdTime;
        private String fullName;
        private String remark;
        private int status;
        private int type;
        private long updatedTime;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getAccountType() {
            return accountType;
        }

        public void setAccountType(int accountType) {
            this.accountType = accountType;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getBankAccount() {
            return bankAccount;
        }

        public void setBankAccount(String bankAccount) {
            this.bankAccount = bankAccount;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public double getCommission() {
            return commission;
        }

        public void setCommission(double commission) {
            this.commission = commission;
        }

        public long getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(long createdTime) {
            this.createdTime = createdTime;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public long getUpdatedTime() {
            return updatedTime;
        }

        public void setUpdatedTime(long updatedTime) {
            this.updatedTime = updatedTime;
        }
    }

