package com.android.bitglobal.dao;


import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.entity.AreaData;
import com.android.bitglobal.entity.AreaDataResult;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * xiezuofei
 * 2016-09-25 19:20
 * 793169940@qq.com
 */
public class AreaDataDao {
    private static AreaDataDao areaDataDao;
    public static synchronized AreaDataDao getInstance() {
        if (areaDataDao == null) {
            areaDataDao = new AreaDataDao();
        }
        return areaDataDao;
    }
    private Realm realm= AppContext.getRealm();
    public AreaDataResult getIfon(){
        AreaDataResult areaDataResult=realm.where(AreaDataResult.class)
                .findFirst();
        return areaDataResult;
    }
    public List<AreaData> getIfon(String parentId){
        List<AreaData> areaDatas=realm.where(AreaData.class).equalTo("parentId",parentId)
                .findAll();
        return areaDatas;
    }
    public AreaData getAreaIfon(String id){
        AreaData areaData=realm.where(AreaData.class).equalTo("id",id)
                .findFirst();
        return areaData;
    }
    public void add(AreaDataResult areaDataResult){
        RealmResults<AreaDataResult> areaDataResults = realm.where(AreaDataResult.class).findAll();
        areaDataResult.setVersion(new Date().getTime());
        realm.beginTransaction();
        areaDataResults.deleteAllFromRealm();
        realm.copyToRealmOrUpdate(areaDataResult);//提交操作
        realm.commitTransaction();
    }


}
