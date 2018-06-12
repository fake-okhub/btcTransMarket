package com.android.bitglobal.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-08-04 13:20
 * 793169940@qq.com
 *
 */
public class CurrencyAddressResult extends RealmObject {
    @PrimaryKey
    private Long version;//版本号

    private String userId;

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    private String currencyType;
    private RealmList<CurrencyAddress> rechargeAddrs;//地址

    public RealmList<CurrencyAddress> getRechargeAddrs() {
        return rechargeAddrs;
    }

    public void setRechargeAddrs(RealmList<CurrencyAddress> rechargeAddrs) {
        this.rechargeAddrs = rechargeAddrs;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "rechargeAddrs=" + rechargeAddrs
                + "  currencyType="+currencyType
        +" version=" + version
                +" | ";
    }
}
