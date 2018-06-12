package com.android.bitglobal.entity;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * xiezuofei
 * 2016-08-19 09:20
 * 793169940@qq.com
 *
 */
public class EntityString extends RealmObject {
    private String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "str=" + str
                +" | ";
    }
}
