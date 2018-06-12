package com.android.bitglobal.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-08-04 13:20
 * 793169940@qq.com
 * 地区信息
 */
public class AreaData extends RealmObject {
    @PrimaryKey
    private String id;//地区id
    private String name;//地名
    private String parentId	;//	所属地id

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "AreaData.id=" + id
                + " AreaData.name=" + name
                + " AreaData.parentId=" + parentId
                +" | ";
    }
}
