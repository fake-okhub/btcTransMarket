package com.android.bitglobal.dao;


import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.entity.Login2Info;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * xiezuofei
 * 2016-07-14 16:20
 * 793169940@qq.com
 * 二次登录dao
 */
public class Login2LnfoDao {
    private static Login2LnfoDao login2LnfoDao;
    public static synchronized Login2LnfoDao getInstance() {
        if (login2LnfoDao == null) {
            login2LnfoDao = new Login2LnfoDao();
        }
        return login2LnfoDao;
    }
    private Realm realm=  AppContext.getRealm();

    public Login2Info getIfon(){
        Login2Info login2Lnfo=realm.where(Login2Info.class).findFirst();
        return login2Lnfo;
    }

    public void setIfon(Login2Info login2Lnfo){
        RealmResults<Login2Info> login2Lnfos = realm.where(Login2Info.class).findAll();
        realm.beginTransaction();
        if(login2Lnfos.size()>0){
            login2Lnfos.deleteAllFromRealm();
        }
        realm.copyToRealm(login2Lnfo);//修改操作
        realm.commitTransaction();
    }

}
