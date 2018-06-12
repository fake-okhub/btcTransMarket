package com.android.bitglobal.entity;


import java.util.ArrayList;
import java.util.List;

/**
 * xiezuofei
 * 2016-08-29 18:20
 * 793169940@qq.com
 * 融资融币信息
 */
public class MarginUserResult {
    private List<MarginUser> marginUsers=new ArrayList<>();
    private String leverage;//融资融币信息
    private String leverageLink;//总资产/借入资产

    public List<MarginUser> getMarginUsers() {
        return marginUsers;
    }

    public void setMarginUsers(List<MarginUser> marginUsers) {
        this.marginUsers = marginUsers;
    }

    public String getLeverage() {
        return leverage;
    }

    public void setLeverage(String leverage) {
        this.leverage = leverage;
    }

    public String getLeverageLink() {
        return leverageLink;
    }

    public void setLeverageLink(String leverageLink) {
        this.leverageLink = leverageLink;
    }

    @Override
    public String toString() {
        return "leverage=" + leverage
                + " leverageLink=" + leverageLink
                + " marginUsers=" + marginUsers
                +" | ";
    }
}
