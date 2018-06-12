package com.android.bitglobal.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-09-07 16:00
 * 793169940@qq.com
 */
public class Country  extends RealmObject {
    @PrimaryKey
    private String id;//
    private String des;//国家名
    private String localName;//国家名
    private String code;//电话区位码

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    @Override
    public String toString() {
        return "id=" + id
                + " des=" + des
                + " localName=" + localName
                + " code=" + code
                +" | ";
    }
}
