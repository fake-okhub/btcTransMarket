package com.android.bitglobal.entity;

/**
 * xiezuofei
 * 2016-08-30 16:20
 * 793169940@qq.com
 * 还款记录
 */
public class RepayRecord {
    private String id;
    private String currencyType;
    private String principalAndInterest;
    private String status;
    private String shouldRepayTime;
    private String repayTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getPrincipalAndInterest() {
        return principalAndInterest;
    }

    public void setPrincipalAndInterest(String principalAndInterest) {
        this.principalAndInterest = principalAndInterest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShouldRepayTime() {
        return shouldRepayTime;
    }

    public void setShouldRepayTime(String shouldRepayTime) {
        this.shouldRepayTime = shouldRepayTime;
    }

    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime;
    }



}
