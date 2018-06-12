package com.android.bitglobal.entity;

import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-08-25 18:20
 * 793169940@qq.com
 *
 */
public class WithdrawAddress{
    @PrimaryKey
    private String id;
    private String address;//地址
    private String memo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "id=" + id
                + " address=" + address
                + " memo=" + memo
                +" | ";
    }
}
