package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2020/1/10 11:17
 * Summary: 签到红包
 */
public class SignInRedPaperBean {
    /**
     * signBonusAmount : 2.2
     * bonusType : 1
     */

    private boolean showDialog=true;//是否弹出签到红包框（native 自己加的）
    private boolean share=false;// 是否分享（native 自己加的）
    private double signBonusAmount;//签到红包金额
    private int bonusType;//红包类型：0 周一到周六普通红包 1 周一到周六翻倍红包 2 周日红包

    public SignInRedPaperBean() {
    }

    public SignInRedPaperBean(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public boolean isShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public double getSignBonusAmount() {
        return signBonusAmount;
    }

    public void setSignBonusAmount(double signBonusAmount) {
        this.signBonusAmount = signBonusAmount;
    }

    public int getBonusType() {
        return bonusType;
    }

    public void setBonusType(int bonusType) {
        this.bonusType = bonusType;
    }
}
