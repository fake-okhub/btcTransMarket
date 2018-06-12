package com.android.bitglobal.dao;


import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.entity.UserAcount;
import com.android.bitglobal.entity.AssetsBalance;
import com.android.bitglobal.entity.UserAcountResult;
import com.android.bitglobal.entity.UserAcountsResult;
import com.android.bitglobal.entity.UserInfo;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * xiezuofei
 * 2016-07-13 13:20
 * 793169940@qq.com
 * 用户资产dao
 */
public class UserAcountDao {
    private static UserAcountDao userAcountDao;
    public static synchronized UserAcountDao getInstance() {
        if (userAcountDao == null) {
            userAcountDao = new UserAcountDao();
        }
        return userAcountDao;
    }
    private Realm realm= AppContext.getRealm();

   public UserAcountResult getIfon(String userId){
       UserAcountResult userAcount=realm.where(UserAcountResult.class)
               .equalTo("userId", userId)
               .findFirst();
       return userAcount;
   }
    public RealmResults<UserAcountResult> getIfon(){
        RealmResults<UserAcountResult> userAcount=realm.where(UserAcountResult.class)
                .findAll();
        return userAcount;
    }
    public void setIfon(UserAcountResult userAcount){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(userAcount);//修改操作
        realm.commitTransaction();
    }

}
