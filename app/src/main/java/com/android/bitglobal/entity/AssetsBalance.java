package com.android.bitglobal.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-08-04 13:20
 * 793169940@qq.com
 * 资产余额数据
 */
public class AssetsBalance extends RealmObject {
    @PrimaryKey
    private String currencyType;//货币类型：BTC：比特币，LTC：莱特币，RMB：人民币
    private String total;//总额
    private String available;//可用
    private String frozen;//冻结
    private String loan;//借入/借出
    private String isIn;//是否借入 0:否 1:是

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getFrozen() {
        return frozen;
    }

    public void setFrozen(String frozen) {
        this.frozen = frozen;
    }

    public String getLoan() {
        return loan;
    }

    public void setLoan(String loan) {
        this.loan = loan;
    }

    public String getIsIn() {
        return isIn;
    }

    public void setIsIn(String isIn) {
        this.isIn = isIn;
    }

    @Override
    public String toString() {
        return " currencyType=" + currencyType
                + " total=" + total
                + " available=" + available
                + " frozen=" + frozen
                + " loan=" + loan
                + " isIn=" + isIn
                +" | ";
    }
}
