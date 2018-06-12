package com.android.bitglobal.dao;

import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.entity.CurrencySetRealm;
import com.android.bitglobal.entity.CurrencySetResult;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * xiezuofei
 * 2016-08-18 13:20
 * 793169940@qq.com
 * 货币配置dao
 */
public class CurrencySetDao {
    private static CurrencySetDao currencySetDao;
    public static synchronized CurrencySetDao getInstance() {
        if (currencySetDao == null) {
            currencySetDao = new CurrencySetDao();
        }
        return currencySetDao;
    }
    private Realm realm= AppContext.getRealm();
    public CurrencySetResult getIfon(){
        CurrencySetResult currencySetResult=realm.where(CurrencySetResult.class)
                .findFirst();
        return currencySetResult;
    }
    public CurrencySetRealm getIfon(String currency){
        CurrencySetRealm currencySetRealm=realm.where(CurrencySetRealm.class).equalTo("currency",currency)
                .findFirst();
        return currencySetRealm;
    }
    public void add(CurrencySetResult currencySetResult){
        RealmResults<CurrencySetResult> currencySetResults = realm.where(CurrencySetResult.class).findAll();
        currencySetResult.setVersion(new Date().getTime());
        realm.beginTransaction();
        currencySetResults.deleteAllFromRealm();
        realm.copyToRealmOrUpdate(currencySetResult);//提交操作
        realm.commitTransaction();
    }


}
