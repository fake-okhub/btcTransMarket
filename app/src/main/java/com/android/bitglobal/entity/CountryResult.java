package com.android.bitglobal.entity;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-08-02 13:20
 * 793169940@qq.com
 * 版本信息
 */
public class CountryResult  extends RealmObject {
    @PrimaryKey
    private Long version;//版本号
    private RealmList<Country> countries;
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public RealmList<Country> getCountries() {
        return countries;
    }

    public void setCountries(RealmList<Country> countries) {
        this.countries = countries;
    }
    @Override
    public String toString() {
        return "version=" + version
                + " countries=" + countries
                +" | ";
    }
}
