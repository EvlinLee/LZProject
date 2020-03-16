package com.by.lizhiyoupin.app.io.bean;

/**
 * data:2019/11/25
 * author:jyx
 * function:
 */
public class AlipayBean {


        /**
         * id : 1
         * userId : 123456789
         * account : 1234567899
         * accountType : 1
         * commonlyBalance : 1.1
         * giftBalance : 1.1
         * highBalance : 1.1
         * activityBalance : 1.1
         * idCard : 1234567899
         * cardFrontImg : 123456
         * cardBackImg : 123456
         * bindStatus : 1
         * fullName : fsd
         * bankName : fsd
         * bankAccount : fsd
         * bankNickName : fsd
         * surplusBalance : 1.1
         */

        private Long id;
        private Long userId;
        private String account;
        private int accountType;
        private double commonlyBalance;
        private double giftBalance;
        private double highBalance;
        private double activityBalance;
        private String idCard;
        private String cardFrontImg;
        private String cardBackImg;
        private int bindStatus;
        private String fullName;
        private String bankName;
        private String bankAccount;
        private String bankNickName;
        private double surplusBalance;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

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

        public void setHighBalance(double highBalance) {
            this.highBalance = highBalance;
        }

        public double getActivityBalance() {
            return activityBalance;
        }

        public void setActivityBalance(double activityBalance) {
            this.activityBalance = activityBalance;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getCardFrontImg() {
            return cardFrontImg;
        }

        public void setCardFrontImg(String cardFrontImg) {
            this.cardFrontImg = cardFrontImg;
        }

        public String getCardBackImg() {
            return cardBackImg;
        }

        public void setCardBackImg(String cardBackImg) {
            this.cardBackImg = cardBackImg;
        }

        public int getBindStatus() {
            return bindStatus;
        }

        public void setBindStatus(int bindStatus) {
            this.bindStatus = bindStatus;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
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

        public String getBankNickName() {
            return bankNickName;
        }

        public void setBankNickName(String bankNickName) {
            this.bankNickName = bankNickName;
        }

        public double getSurplusBalance() {
            return surplusBalance;
        }

        public void setSurplusBalance(double surplusBalance) {
            this.surplusBalance = surplusBalance;
        }
    }

