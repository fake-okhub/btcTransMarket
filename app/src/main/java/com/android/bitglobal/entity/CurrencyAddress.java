package com.android.bitglobal.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-08-04 13:20
 * 793169940@qq.com
 *
 */
public class CurrencyAddress extends RealmObject {
    @PrimaryKey
    private String id;
    private String currencyType;
    private String address;//地址

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "id=" + id
                + " currencyType=" + currencyType
                + " address=" + address
                +" | ";
    }
}
