package com.android.bitglobal.entity;


import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * xiezuofei
 * 2016-08-02 18:20
 * 793169940@qq.com
 */
public class CurrencyMarketDepthRealm extends RealmObject {
    private String currency;
    private RealmList<EntityString> optional;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public RealmList<EntityString> getOptional() {
        return optional;
    }

    public void setOptional(RealmList<EntityString> optional) {
        this.optional = optional;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("currency=" + currency);
        sb.append(" optional=" + optional);
        return sb.toString();
    }
}
