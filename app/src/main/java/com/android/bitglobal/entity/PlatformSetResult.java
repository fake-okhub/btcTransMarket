package com.android.bitglobal.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-08-22 13:20
 * 793169940@qq.com
 *
 */
public class PlatformSetResult extends RealmObject {
    @PrimaryKey
    private Long version;
    private RealmList<PlatformSet> platformSets;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public RealmList<PlatformSet> getPlatformSets() {
        return platformSets;
    }

    public void setPlatformSets(RealmList<PlatformSet> platformSets) {
        this.platformSets = platformSets;
    }

    @Override
    public String toString() {
        return "version=" + version
                + " platformSets=" + platformSets
                +" | ";
    }
}
