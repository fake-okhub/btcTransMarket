package com.android.bitglobal.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-09-02 16:20
 * 793169940@qq.com
 */
public class Login2Info extends RealmObject {
    @PrimaryKey
    public String userId;//用户ID
    private String needGoogleCode;
    private String needDynamicCode;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNeedGoogleCode() {
        return needGoogleCode;
    }

    public void setNeedGoogleCode(String needGoogleCode) {
        this.needGoogleCode = needGoogleCode;
    }

    public String getNeedDynamicCode() {
        return needDynamicCode;
    }

    public void setNeedDynamicCode(String needDynamicCode) {
        this.needDynamicCode = needDynamicCode;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" userId=" + userId);
        sb.append(" needGoogleCode=" + needGoogleCode);
        sb.append(" needDynamicCode=" + needDynamicCode);
        return sb.toString();
    }
}
