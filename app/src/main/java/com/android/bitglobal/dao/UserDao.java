package com.android.bitglobal.dao;


import android.util.Log;

import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.entity.UserInfo;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.internal.log.Logger;

/**
 * xiezuofei
 * 2016-07-13 13:20
 * 793169940@qq.com
 * 用户dao
 */
public class UserDao {
    private static UserDao userDao;
    public static synchronized UserDao getInstance() {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao;
    }
    private Realm realm= AppContext.getRealm();
    public void login(UserInfo userInfo){
        int if_cz = realm.where(UserInfo.class)
                .equalTo("userId", userInfo.getUserId())
                .findAll().size();
        realm.beginTransaction();
        if(if_cz<1){
            realm.copyToRealm(userInfo);//提交操作
        }else{
            RealmResults<UserInfo> realmUserInfo =realm.where(UserInfo.class).equalTo("is_online","1").findAll();
            for (UserInfo ui:realmUserInfo){
                ui.setIs_online("0");
                realm.copyToRealmOrUpdate(ui);//修改操作
            }
            realm.copyToRealmOrUpdate(userInfo);//修改操作
        }
        realm.commitTransaction();
    }
    public UserInfo getIfon(String userId){
        UserInfo userInfo=realm.where(UserInfo.class)
                .equalTo("userId", userId)
                .findFirst();
        return userInfo;
    }
    public UserInfo getIfon(){
        UserInfo userInfo=realm.where(UserInfo.class)
                .equalTo("is_online", "1")
                .findFirst();
        return userInfo;
    }
    public RealmResults<UserInfo> getUsers(){
        RealmResults<UserInfo> userInfos=realm.where(UserInfo.class)
                .equalTo("is_online", "0")
                .notEqualTo("token","")
                .notEqualTo("token","null")
                .findAll();
        return userInfos;
    }
    public String getToken(){
        UserInfo userInfo=realm.where(UserInfo.class)
                .equalTo("is_online", "1")
                .findFirst();
        String token="";
        if(userInfo==null||userInfo.getToken()==null||userInfo.getToken().equals("null")){

        }else{
            token=userInfo.getToken();
        }
        return token;
    }
    public String getUserId(){
        UserInfo userInfo=realm.where(UserInfo.class)
                .equalTo("is_online", "1")
                .findFirst();
        String userId="";
        if(userInfo==null||userInfo.getToken()==null||userInfo.getToken().equals("null")){

        }else{
            userId=userInfo.getUserId();
        }
        return userId;
    }
    public void setIfon(UserInfo userInfo){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(userInfo);//修改操作
        realm.commitTransaction();
    }
    public void exit(){
        realm.beginTransaction();
        RealmResults<UserInfo> realmUserInfo =realm.where(UserInfo.class).equalTo("is_online","1").findAll();
        for (UserInfo ui:realmUserInfo){
            ui.setIs_online("0");
            ui.setToken("");
            realm.copyToRealmOrUpdate(ui);//修改操作
        }
        realm.commitTransaction();
    }
    public void delect(String userId){
        RealmResults<UserInfo> userInfos=realm.where(UserInfo.class)
                .equalTo("userId", userId)
                .findAll();
        realm.beginTransaction();
        userInfos.deleteAllFromRealm();
        realm.commitTransaction();
    }
    public void change(String userId){
        UserInfo userInfo = realm.where(UserInfo.class)
                .equalTo("userId", userId)
                .findFirst();
        realm.beginTransaction();
        RealmResults<UserInfo> realmUserInfo =realm.where(UserInfo.class).equalTo("is_online","1").findAll();
        for (UserInfo ui:realmUserInfo){
            ui.setIs_online("0");
            realm.copyToRealmOrUpdate(ui);//修改操作
        }
        userInfo.setIs_online("1");
        realm.copyToRealmOrUpdate(userInfo);//修改操作
        realm.commitTransaction();
    }



}
