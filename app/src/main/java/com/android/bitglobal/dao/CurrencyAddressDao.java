package com.android.bitglobal.dao;

import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.entity.CurrencyAddressResult;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * xiezuofei
 * 2016-08-18 13:20
 * 793169940@qq.com
 * 充值地址dao
 */
public class CurrencyAddressDao {
    private static CurrencyAddressDao currencyAddressDao;
    public static synchronized CurrencyAddressDao getInstance() {
        if (currencyAddressDao == null) {
            currencyAddressDao = new CurrencyAddressDao();
        }
        return currencyAddressDao;
    }
    private Realm realm= AppContext.getRealm();
    public CurrencyAddressResult getIfon(String type, String userId){
        CurrencyAddressResult currencyAddressResult=realm.where(CurrencyAddressResult.class).equalTo("currencyType", type).equalTo("userId", userId)
                .findFirst();
        return currencyAddressResult;
    }

    public void add(CurrencyAddressResult currencyAddressResult){
        RealmResults<CurrencyAddressResult> results = realm.where(CurrencyAddressResult.class).findAll();
        currencyAddressResult.setVersion(new Date().getTime());
        realm.beginTransaction();
        results.deleteAllFromRealm();
        realm.copyToRealmOrUpdate(currencyAddressResult);//提交操作
        realm.commitTransaction();
    }


}
