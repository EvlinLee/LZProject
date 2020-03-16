package com.by.lizhiyoupin.app.io.bean;

/**
 * data:2019/11/19
 * author:jyx
 * function:
 */
public class WithdrawaccountBean {

        private int accountType;
        private String fullName;
        private String account;
        private String bankName;
        private String bankAccount;
        private double commonlyBalance;
        private double giftBalance;
        private double highBalance;
        private double surplusBalance;
        private double activityBalance;
        private int bindStatus;

    public int getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(int bindStatus) {
        this.bindStatus = bindStatus;
    }

    public void setHighBalance(double highBalance) {
        this.highBalance = highBalance;
    }

    public double getActivityBalance() {
        return activityBalance;
    }

    public void setActivityBalance(double activityBalance) {
        this.activityBalance = activityBalance;
    }

    public double getSurplusBalance() {
        return surplusBalance;
    }

    public void setSurplusBalance(double surplusBalance) {
        this.surplusBalance = surplusBalance;
    }

    public int getAccountType() {
            return accountType;
        }

        public void setAccountType(int accountType) {
            this.accountType = accountType;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBankAccount() {
            return bankAccount;
        }

        public void setBankAccount(String bankAccount) {
            this.bankAccount = bankAccount;
        }

        public double getCommonlyBalance() {
            return commonlyBalance;
        }

        public void setCommonlyBalance(double commonlyBalance) {
            this.commonlyBalance = commonlyBalance;
        }

        public double getGiftBalance() {
            return giftBalance;
        }

        public void setGiftBalance(double giftBalance) {
            this.giftBalance = giftBalance;
        }

        public double getHighBalance() {
            return highBalance;
        }

        public void setHighBalance(int highBalance) {
            this.highBalance = highBalance;
        }
    }

