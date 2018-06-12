package com.android.bitglobal.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-08-02 13:20
 * 793169940@qq.com
 * 用户基本信息
 */
public class UserInfo extends RealmObject {
    @PrimaryKey
    private String userId;//用户ID
    private String token;
    private String is_online;
    private String userName;// 用户名
    private String mobileNumber;// 手机号
    private String email;// 邮箱
    private String totalJifen;// 总积分
    private String isHadSecurePassword;// 是否设置有资金密码: 0:否,1:是
    private String safePwdPeriod;// 关闭周期 1:始终开启 0:永久关 闭 2:关闭6个小时
    private String juaUserId;//为空时表示未绑定JUA
    private String bwUserId;//为空时表示未绑定bw
    private String realName;//为空时表示未认证
    private String googleAuth;//1已通过谷歌认证0未通 过谷歌认证
    private String vipLevel;//vip等级0­6
    private String loginSmsCheck;//异地登录短信验证 1开 启0关闭
    private String loginGoogleAuth;//登录谷歌验证 1开启0关 闭
    private String paySmsAuth;//支付短信验证 1开启0关闭
    private String payGoogleAuth;//支付谷歌验证 1开启0关 闭
    private String identityAuthStatus;//实名认证状态
    private String userOpenId;// 网页授权ID
    private String countryCode;// 手机号码对应国家码


    private String loginAuthenType;//登录验证策略：0.未选择1.密码2.密码+Google验证码3.密码+异地登录验证（短信/邮件)4. 密码+Google验证码+异地登录验证（短信/邮件）
    private String tradeAuthenType;//交易验证策略：0.未选择1.永不输入资金密码2.6小时内免输资金密码3.每次交易均验证资金密码
    private String withdrawAuthenType;//提现验证策略：0.未选择1.资金密码+短信/邮件验证码2.资金密码+Google验证码3.资金密码+短信/邮件验证码+Google验证码

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTotalJifen() {
        return totalJifen;
    }

    public void setTotalJifen(String totalJifen) {
        this.totalJifen = totalJifen;
    }

    public String getIsHadSecurePassword() {
        return isHadSecurePassword;
    }

    public void setIsHadSecurePassword(String isHadSecurePassword) {
        this.isHadSecurePassword = isHadSecurePassword;
    }

    public String getSafePwdPeriod() {
        return safePwdPeriod;
    }

    public void setSafePwdPeriod(String safePwdPeriod) {
        this.safePwdPeriod = safePwdPeriod;
    }

    public String getJuaUserId() {
        return juaUserId;
    }

    public void setJuaUserId(String juaUserId) {
        this.juaUserId = juaUserId;
    }

    public String getBwUserId() {
        return bwUserId;
    }

    public void setBwUserId(String bwUserId) {
        this.bwUserId = bwUserId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getGoogleAuth() {
        return googleAuth;
    }

    public void setGoogleAuth(String googleAuth) {
        this.googleAuth = googleAuth;
    }

    public String getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(String vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getLoginSmsCheck() {
        return loginSmsCheck;
    }

    public void setLoginSmsCheck(String loginSmsCheck) {
        this.loginSmsCheck = loginSmsCheck;
    }

    public String getLoginGoogleAuth() {
        return loginGoogleAuth;
    }

    public void setLoginGoogleAuth(String loginGoogleAuth) {
        this.loginGoogleAuth = loginGoogleAuth;
    }

    public String getPayGoogleAuth() {
        return payGoogleAuth;
    }

    public void setPayGoogleAuth(String payGoogleAuth) {
        this.payGoogleAuth = payGoogleAuth;
    }

    public String getIdentityAuthStatus() {
        return identityAuthStatus;
    }

    public void setIdentityAuthStatus(String identityAuthStatus) {
        this.identityAuthStatus = identityAuthStatus;
    }

    public String getUserOpenId() {
        return userOpenId;
    }

    public void setUserOpenId(String userOpenId) {
        this.userOpenId = userOpenId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPaySmsAuth() {
        return paySmsAuth;
    }

    public void setPaySmsAuth(String paySmsAuth) {
        this.paySmsAuth = paySmsAuth;
    }

    public String getLoginAuthenType() {
        return loginAuthenType;
    }

    public void setLoginAuthenType(String loginAuthenType) {
        this.loginAuthenType = loginAuthenType;
    }

    public String getTradeAuthenType() {
        return tradeAuthenType;
    }

    public void setTradeAuthenType(String tradeAuthenType) {
        this.tradeAuthenType = tradeAuthenType;
    }

    public String getWithdrawAuthenType() {
        return withdrawAuthenType;
    }

    public void setWithdrawAuthenType(String withdrawAuthenType) {
        this.withdrawAuthenType = withdrawAuthenType;
    }

    @Override
    public String toString() {
        return "userId=" + userId
                + " token=" + token
                + " is_online=" + is_online
                + " userName=" + userName
                + " mobileNumber=" + mobileNumber
                + " email=" + email
                + " totalJifen=" + totalJifen
                + " isHadSecurePassword=" + isHadSecurePassword
                + " safePwdPeriod=" + safePwdPeriod
                + " juaUserId=" + juaUserId
                + " bwUserId=" + bwUserId
                + " realName=" + realName
                + " googleAuth=" + googleAuth
                + " vipLevel=" + vipLevel
                + " loginSmsCheck=" + loginSmsCheck
                + " loginGoogleAuth=" + loginGoogleAuth
                + " paySmsAuth=" + paySmsAuth
                + " payGoogleAuth=" + payGoogleAuth
                + " identityAuthStatus=" + identityAuthStatus
                + " userOpenId=" + userOpenId
                + " countryCode=" + countryCode
                + " loginAuthenType=" + loginAuthenType
                + " tradeAuthenType=" + tradeAuthenType
                + " withdrawAuthenType=" + withdrawAuthenType
                +" | ";
    }
}
