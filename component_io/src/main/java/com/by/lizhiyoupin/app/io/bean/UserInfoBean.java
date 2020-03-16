package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/14 14:42
 * Summary: 用户信息
 */
public class UserInfoBean {


    /**
     * id :
     * name :
     * phone :
     * avatar :
     * gender : 0
     * apiToken :
     * roleLevel : 0
     * levelStartTime : 2019-10-14 14:21:05
     * levelEndTime : 2019-10-15 14:21:05
     * code :
     * inviteCount : 12
     * tuanCount : 15
     * superiorId :
     * operatorId :
     * coFounderId :
     * relationId :
     * parentCode :
     * wechat :
     * isBindWx : 0
     * addressId :
     * forbidden : 0
     * createdTime : 2019-10-14 14:21:05
     * updatedTime : 2019-10-14 14:21:05
     * invitedTime : 2019-10-14 14:21:05
     * upgradeDegradationMainId :
     * plusVipMissionStatus : 0
     * plusOperatorMissionStatus : 1
     * firstLogin: 是否第一次登录： 0  不是  1  是
     * couponGiftMoneyUrl: 优惠券跳转链接
     * registerType:注册来源（注册来源  1  APP  2  拼团小程序  3  A版迁移  4  魔力星球  5  后台系统  6  优惠券）
     */

    private long id=-1;//主键id ,userId
    private String name;//会员名称
    private String phone;//手机号
    private String avatar;//头像
    private int gender;//性别: 0-未知 1-男 2-女
    private String apiToken;//鉴权token
    private int roleLevel;//角色：1-普通 2-超级 3-Plus超级 4-运营商 5-plus运营商
    private String levelStartTime;//会员等级开始时间
    private String levelEndTime;//会员等级结束时间
    private String roleLevelEndTime;//会员当前等级结束时间
    private String code;//邀请码
    private int inviteCount;//邀请下级数量
    private int tuanCount;//团队数量
    private String parentCode;//上级的邀请码
    private String superiorId;//上级id
    private String operatorId;//运营商i
    private String coFounderId;//联创id
    private String relationId;//渠道ID
    private String wechat;//微信号
    private int isBindWx;//是否绑定微信：1-绑定 0-未绑定
    private String addressId;//默认地址id
    private int forbidden;//禁止登陆  0可以登录   1不可以登录,
    private String createdTime;//:创建时间
    private String updatedTime;//更新时间
    private String invitedTime;//被邀请时间
    private String upgradeDegradationMainId;//主升降级记录id
    private int plusVipMissionStatus;//plus超级会员任务状态  0未开启  1开启  2成功
    private int plusOperatorMissionStatus;//plus运营商任务状态   1保级成功  2升级任务  3成功  4任务失败
    private String accessToken;//微信accessToken
    private String openId;//微信用户唯一识别码
    private String userBirthday;//会员生日
    private String bankAccount;//银行账号（只有会员是运营商以上级别才会有值）
    private String alipayAccount;//支付宝账号
    private String fullName;//支付宝姓名
    private int firstTimeLogin;//是否是首次注册： 0不是    1是
    private int firstLogin;// 是否第一次登录(注册可能是其他来源的)： 0  不是  1  是
    private int registerType;//注册来源（注册来源  1  APP  2  拼团小程序  3  A版迁移  4  魔力星球  5  后台系统  6  优惠券）
    private String couponGiftMoneyUrl;//优惠券跳转链接

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public int getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(int roleLevel) {
        this.roleLevel = roleLevel;
    }

    public String getLevelStartTime() {
        return levelStartTime;
    }

    public void setLevelStartTime(String levelStartTime) {
        this.levelStartTime = levelStartTime;
    }

    public String getRoleLevelEndTime() {
        return roleLevelEndTime;
    }

    public void setRoleLevelEndTime(String roleLevelEndTime) {
        this.roleLevelEndTime = roleLevelEndTime;
    }

    public String getLevelEndTime() {
        return levelEndTime;
    }

    public void setLevelEndTime(String levelEndTime) {
        this.levelEndTime = levelEndTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(int inviteCount) {
        this.inviteCount = inviteCount;
    }

    public int getTuanCount() {
        return tuanCount;
    }

    public void setTuanCount(int tuanCount) {
        this.tuanCount = tuanCount;
    }

    public String getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(String superiorId) {
        this.superiorId = superiorId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getCoFounderId() {
        return coFounderId;
    }

    public void setCoFounderId(String coFounderId) {
        this.coFounderId = coFounderId;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public int getIsBindWx() {
        return isBindWx;
    }

    public void setIsBindWx(int isBindWx) {
        this.isBindWx = isBindWx;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public int getForbidden() {
        return forbidden;
    }

    public void setForbidden(int forbidden) {
        this.forbidden = forbidden;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getInvitedTime() {
        return invitedTime;
    }

    public void setInvitedTime(String invitedTime) {
        this.invitedTime = invitedTime;
    }

    public String getUpgradeDegradationMainId() {
        return upgradeDegradationMainId;
    }

    public void setUpgradeDegradationMainId(String upgradeDegradationMainId) {
        this.upgradeDegradationMainId = upgradeDegradationMainId;
    }

    public int getPlusVipMissionStatus() {
        return plusVipMissionStatus;
    }

    public void setPlusVipMissionStatus(int plusVipMissionStatus) {
        this.plusVipMissionStatus = plusVipMissionStatus;
    }

    public int getPlusOperatorMissionStatus() {
        return plusOperatorMissionStatus;
    }

    public void setPlusOperatorMissionStatus(int plusOperatorMissionStatus) {
        this.plusOperatorMissionStatus = plusOperatorMissionStatus;
    }

    public int getFirstTimeLogin() {
        return firstTimeLogin;
    }

    public void setFirstTimeLogin(int firstTimeLogin) {
        this.firstTimeLogin = firstTimeLogin;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public int getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(int firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getCouponGiftMoneyUrl() {
        return couponGiftMoneyUrl;
    }

    public void setCouponGiftMoneyUrl(String couponGiftMoneyUrl) {
        this.couponGiftMoneyUrl = couponGiftMoneyUrl;
    }

    public int getRegisterType() {
        return registerType;
    }

    public void setRegisterType(int registerType) {
        this.registerType = registerType;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender=" + gender +
                ", apiToken='" + apiToken + '\'' +
                ", roleLevel=" + roleLevel +
                ", levelStartTime='" + levelStartTime + '\'' +
                ", levelEndTime='" + levelEndTime + '\'' +
                ", roleLevelEndTime='" + roleLevelEndTime + '\'' +
                ", code='" + code + '\'' +
                ", inviteCount=" + inviteCount +
                ", tuanCount=" + tuanCount +
                ", parentCode='" + parentCode + '\'' +
                ", superiorId='" + superiorId + '\'' +
                ", operatorId='" + operatorId + '\'' +
                ", coFounderId='" + coFounderId + '\'' +
                ", relationId='" + relationId + '\'' +
                ", wechat='" + wechat + '\'' +
                ", isBindWx=" + isBindWx +
                ", addressId='" + addressId + '\'' +
                ", forbidden=" + forbidden +
                ", createdTime='" + createdTime + '\'' +
                ", updatedTime='" + updatedTime + '\'' +
                ", invitedTime='" + invitedTime + '\'' +
                ", upgradeDegradationMainId='" + upgradeDegradationMainId + '\'' +
                ", plusVipMissionStatus=" + plusVipMissionStatus +
                ", plusOperatorMissionStatus=" + plusOperatorMissionStatus +
                ", firstTimeLogin=" + firstTimeLogin +
                ", accessToken='" + accessToken + '\'' +
                ", openId='" + openId + '\'' +
                ", userBirthday='" + userBirthday + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", alipayAccount='" + alipayAccount + '\'' +
                ", fullName='" + fullName + '\'' +
                ", firstLogin=" + firstLogin +
                ", couponGiftMoneyUrl='" + couponGiftMoneyUrl + '\'' +
                ", registerType=" + registerType +
                '}';
    }
}
