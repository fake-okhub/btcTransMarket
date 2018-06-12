package com.android.bitglobal.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-08-02 13:20
 * 793169940@qq.com
 * 用户财富数据
 */
public class UserAcount extends RealmObject {
    @PrimaryKey
    private String userId;//用户ID
    private String totalAssets;//按人民币换算 总资产
    private String netAssets;//按人民币换算 净资产
    private String totalJifen;//总积分
    private RealmList<AssetsBalance> balances;//资产余额

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(String totalAssets) {
        this.totalAssets = totalAssets;
    }

    public String getNetAssets() {
        return netAssets;
    }

    public void setNetAssets(String netAssets) {
        this.netAssets = netAssets;
    }

    public String getTotalJifen() {
        return totalJifen;
    }

    public void setTotalJifen(String totalJifen) {
        this.totalJifen = totalJifen;
    }

    public RealmList<AssetsBalance> getBalances() {
        return balances;
    }

    public void setBalances(RealmList<AssetsBalance> balances) {
        this.balances = balances;
    }

    @Override
    public String toString() {
        return "userId=" + userId
                + " totalAssets=" + totalAssets
                + " netAssets=" + netAssets
                + " balances=" + balances
                +" | ";
    }
}
