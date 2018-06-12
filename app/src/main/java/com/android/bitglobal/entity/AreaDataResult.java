package com.android.bitglobal.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-09-25 16:20
 * 793169940@qq.com
 * 地区信息返回数据
 */
public class AreaDataResult extends RealmObject {
    @PrimaryKey
    private Long version;
    private RealmList<AreaData> areas=new RealmList<AreaData>();

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }


    public RealmList<AreaData> getAreas() {
        return areas;
    }

    public void setAreas(RealmList<AreaData> areas) {
        this.areas = areas;
    }

    @Override
    public String toString() {
        return "AreaDataResult.version=" + version
                + " AreaDataResult.areas=" + areas
                +" | ";
    }
}
