package com.android.bitglobal.dao;

import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.entity.RechargeBank;
import com.android.bitglobal.entity.RechargeBankResult;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * xiezuofei
 * 2016-09-07 19:20
 * 793169940@qq.com
 */
public class RechargeBankDao {
    private static RechargeBankDao rechargeBankDao;
    public static synchronized RechargeBankDao getInstance() {
        if (rechargeBankDao == null) {
            rechargeBankDao = new RechargeBankDao();
        }
        return rechargeBankDao;
    }
    private Realm realm= AppContext.getRealm();
    public RechargeBankResult getIfon(){
        RechargeBankResult rechargeBankResult=realm.where(RechargeBankResult.class)
                .findFirst();
        return rechargeBankResult;
    }
    public RechargeBank getIfon(String id){
        RechargeBank rechargeBank=realm.where(RechargeBank.class).equalTo("id",id)
                .findFirst();
        return rechargeBank;
    }
    public void add(RechargeBankResult rechargeBankResult){
        RealmResults<RechargeBankResult> rechargeBankResults = realm.where(RechargeBankResult.class).findAll();
        rechargeBankResult.setVersion(new Date().getTime());
        realm.beginTransaction();
        rechargeBankResults.deleteAllFromRealm();
        realm.copyToRealmOrUpdate(rechargeBankResult);//提交操作
        realm.commitTransaction();
    }


}
