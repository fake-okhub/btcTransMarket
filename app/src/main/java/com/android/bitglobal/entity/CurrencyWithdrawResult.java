package com.android.bitglobal.entity;


/**
 * xiezuofei
 * 2016-08-02 13:20
 * 793169940@qq.com
 *
 */
public class CurrencyWithdrawResult {
    private String needSafePwd;//是否需要资金密码 0:否 1:是
    private String needMobileCode;//是否需要短信验证码 0:否 1:是
    private String needEmailCode;//是否需要邮件验证码 0:否 1:是
    private String needGoogleCode;//是否需要Google验证码 0:否 1:是

    public String getNeedSafePwd() {
        return needSafePwd;
    }

    public void setNeedSafePwd(String needSafePwd) {
        this.needSafePwd = needSafePwd;
    }

    public String getNeedMobileCode() {
        return needMobileCode;
    }

    public void setNeedMobileCode(String needMobileCode) {
        this.needMobileCode = needMobileCode;
    }

    public String getNeedEmailCode() {
        return needEmailCode;
    }

    public void setNeedEmailCode(String needEmailCode) {
        this.needEmailCode = needEmailCode;
    }

    public String getNeedGoogleCode() {
        return needGoogleCode;
    }

    public void setNeedGoogleCode(String needGoogleCode) {
        this.needGoogleCode = needGoogleCode;
    }

    @Override
    public String toString() {
        return "needSafePwd=" + needSafePwd
                + " needMobileCode=" + needMobileCode
                + " needEmailCode=" + needEmailCode
                + " needGoogleCode=" + needGoogleCode
                +" | ";
    }
}
