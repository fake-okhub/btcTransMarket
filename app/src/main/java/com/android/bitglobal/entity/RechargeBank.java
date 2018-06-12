package com.android.bitglobal.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-09-07 16:00
 * 793169940@qq.com
 */
public class RechargeBank extends RealmObject {
    @PrimaryKey
    private String id;//ID
    private String name;//名称
    private String tag;//标签
    private String img;//图标路径

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "id=" + id
                + " name=" + name
                + " tag=" + tag
                + " img=" + img
                +" | ";
    }
}
