package com.android.bitglobal.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-09-07 16:00
 * 793169940@qq.com
 */
public class RechargeBankResult extends RealmObject {
    @PrimaryKey
    private Long version;
    private RealmList<RechargeBank> rechargeBanks;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public RealmList<RechargeBank> getRechargeBanks() {
        return rechargeBanks;
    }

    public void setRechargeBanks(RealmList<RechargeBank> rechargeBanks) {
        this.rechargeBanks = rechargeBanks;
    }

    @Override
    public String toString() {
        return "version=" + version
                + " rechargeBanks=" + rechargeBanks
                +" | ";
    }
}
