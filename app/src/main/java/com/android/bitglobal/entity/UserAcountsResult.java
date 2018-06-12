package com.android.bitglobal.entity;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by bitbank on 16/12/23.
 */
public class UserAcountsResult extends RealmObject {
    private RealmList<BalanceResult> balances;

    public RealmList<BalanceResult> getBalances() {
        return balances;
    }

    public void setBalances(RealmList<BalanceResult> balances) {
        this.balances = balances;
    }

    @Override
    public String toString() {
        return "balances="+balances;
    }
}
