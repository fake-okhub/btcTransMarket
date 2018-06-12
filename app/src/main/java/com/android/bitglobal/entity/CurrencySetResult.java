package com.android.bitglobal.entity;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-08-02 13:20
 * 793169940@qq.com
 * 用户基本信息
 * 当前货币信息
 */
public class CurrencySetResult extends RealmObject {
    @PrimaryKey
    private Long version;//版本号
    private RealmList<CurrencySetRealm> currencySets;//货币配置数组

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public RealmList<CurrencySetRealm> getCurrencySets() {
        return currencySets;
    }

    public void setCurrencySets(RealmList<CurrencySetRealm> currencySets) {
        this.currencySets = currencySets;
    }

    @Override
    public String toString() {
        return "version=" + version
                + " currencySets=" + currencySets
                +" | ";
    }
}
