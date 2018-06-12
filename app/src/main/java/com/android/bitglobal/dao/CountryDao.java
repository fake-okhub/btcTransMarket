package com.android.bitglobal.dao;

import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.entity.CountryResult;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * xiezuofei
 * 2016-09-07 19:20
 * 793169940@qq.com
 */
public class CountryDao {
    private static CountryDao countryDao;
    public static synchronized CountryDao getInstance() {
        if (countryDao == null) {
            countryDao = new CountryDao();
        }
        return countryDao;
    }
    private Realm realm= AppContext.getRealm();
    public CountryResult getIfon(){
        CountryResult countryResult=realm.where(CountryResult.class)
                .findFirst();
        return countryResult;
    }

    public void add(CountryResult countryResult){
        RealmResults<CountryResult> countryResults = realm.where(CountryResult.class).findAll();
        countryResult.setVersion(new Date().getTime());
        realm.beginTransaction();
        countryResults.deleteAllFromRealm();
        realm.copyToRealmOrUpdate(countryResult);//提交操作
        realm.commitTransaction();
    }


}
