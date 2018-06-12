package com.android.bitglobal.entity;


/**
 * xiezuofei
 * 2016-08-27 16:20
 * 793169940@qq.com
 * 提现网络手续费
 */
public class CounterFee{
    private String id;
    private String currencyType;
    private String counterFee;
    private String feeRate;
    private String isDefault;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getCounterFee() {
        return counterFee;
    }

    public void setCounterFee(String counterFee) {
        this.counterFee = counterFee;
    }

    public String getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(String feeRate) {
        this.feeRate = feeRate;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "id=" + id
                + " currencyType=" + currencyType
                + " counterFee=" + counterFee
                + " feeRate=" + feeRate
                + " isDefault=" + isDefault
                +" | ";
    }
}
