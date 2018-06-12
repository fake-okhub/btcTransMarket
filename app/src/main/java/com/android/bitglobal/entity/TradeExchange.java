package com.android.bitglobal.entity;

/**
 * Created by bitbank on 17/2/28.
 */
public class TradeExchange {
    private String currencyType;
    private String exchangeType;

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    @Override
    public String toString() {
        return "TradeExchange{" +
                "currencyType='" + currencyType + '\'' +
                ", exchangeType='" + exchangeType + '\'' +
                '}';
    }
}
