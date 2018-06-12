package com.android.bitglobal.entity;
/**
 * xiezuofei
 * 2016-08-02 13:20
 * 793169940@qq.com
 */
public class LoginResult {
    private String token;
    private String userId;
    private UserInfo userInfo;
    private Login2Info login2;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Login2Info getLogin2() {
        return login2;
    }

    public void setLogin2(Login2Info login2) {
        this.login2 = login2;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" token=" + token);
        sb.append(" userId=" + userId);
        if (null != userInfo) {
            sb.append(" userInfo:" + userInfo.toString());
        }
        if (null != login2) {
            sb.append(" login2:" + login2.toString());
        }
        return sb.toString();
    }
}
