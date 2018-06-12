package com.android.bitglobal.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-08-02 13:20
 * 793169940@qq.com
 * 用户财富数据返回
 */
public class UserAcountResult extends RealmObject {
    @PrimaryKey
    private String userId;
    private String token;
    private String totalAmount;
    private String convertFund;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userid) {
        this.userId = userid;
    }
    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    private  UserAcountsResult userAccount;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public UserAcountsResult getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAcountsResult userAccount) {
        this.userAccount = userAccount;
    }

    public String getConvertFund() {
        return convertFund;
    }

    public void setConvertFund(String convertFund) {
        this.convertFund = convertFund;
    }

    @Override
    public String toString() {
        return "UserAcountResult="+userAccount +"userId="+userId+",token="+token+",totalAmount="+totalAmount;
    }
}
