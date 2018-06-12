package com.android.bitglobal.dao;

import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.entity.PlatformSet;
import com.android.bitglobal.entity.PlatformSetResult;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * xiezuofei
 * 2016-08-18 13:20
 * 793169940@qq.com
 * 获取各个平台配置信息dao
 */
public class PlatformSetDao {
    private static PlatformSetDao platformSetDao;
    public static synchronized PlatformSetDao getInstance() {
        if (platformSetDao == null) {
            platformSetDao = new PlatformSetDao();
        }
        return platformSetDao;
    }
    private Realm realm= AppContext.getRealm();
    public PlatformSetResult getIfon(){
        PlatformSetResult platformSetResult=realm.where(PlatformSetResult.class)
                .findFirst();
        return platformSetResult;
    }

    public void add(PlatformSetResult platformSetResult){
        RealmResults<PlatformSetResult> platformSetResults = realm.where(PlatformSetResult.class).findAll();
        platformSetResult.setVersion(new Date().getTime());
        realm.beginTransaction();
        platformSetResults.deleteAllFromRealm();
        realm.copyToRealmOrUpdate(platformSetResult);//提交操作
        realm.commitTransaction();
    }
    public void setPlatform(String symbol){
        PlatformSet platformSet=realm.where(PlatformSet.class).equalTo("symbol", symbol).findFirst();
        realm.beginTransaction();
        if("0".equals(platformSet.getIsxs())){
            platformSet.setIsxs("1");
        }else{
            platformSet.setIsxs("0");
        }
        realm.copyToRealmOrUpdate(platformSet);//修改操作
        realm.commitTransaction();
    }
    public void sort(List<PlatformSet> ps){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(ps);
        realm.commitTransaction();

    }

}
