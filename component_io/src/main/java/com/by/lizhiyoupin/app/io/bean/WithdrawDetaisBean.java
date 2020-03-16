package com.by.lizhiyoupin.app.io.bean;

/**
 * data:2019/11/20
 * author:jyx
 * function:
 */
public class WithdrawDetaisBean {

        /**
         * account : 17611112292
         * accountType : 0
         * amount : 300
         * bankAccount :
         * bankName :
         * createdTime : 1574042649000
         * fullName : 测试数据2
         * remark : 转账成功
         * status : 2
         * type : 1
         * commission : 0
         * updatedTime : 1574042652000
         */

        private String account;
        private int accountType;
        private double amount;
        private String bankAccount;
        private String bankName;
        private String createdTime;
        private String fullName;
        private String remark;
        private int status;
        private int type;
        private double commission;
        private String updatedTime;

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

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
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

        public double getCommission() {
            return commission;
        }

        public void setCommission(double commission) {
            this.commission = commission;
        }

        public String getUpdatedTime() {
            return updatedTime;
        }

        public void setUpdatedTime(String updatedTime) {
            this.updatedTime = updatedTime;

    }
}
