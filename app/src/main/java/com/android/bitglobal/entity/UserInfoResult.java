package com.android.bitglobal.entity;

/**
 * xiezuofei
 * 2016-08-02 13:20
 * 793169940@qq.com
 * 用户基本信息
 */
public class UserInfoResult{
    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "userInfo=" + userInfo;
    }
}
