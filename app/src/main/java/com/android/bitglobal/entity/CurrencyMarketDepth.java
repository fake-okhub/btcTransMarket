package com.android.bitglobal.entity;


/**
 * xiezuofei
 * 2016-08-02 18:20
 * 793169940@qq.com
 */
public class CurrencyMarketDepth{
    private String currency;
    private String[] optional;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String[] getOptional() {
        return optional;
    }

    public void setOptional(String[] optional) {
        this.optional = optional;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("currency=" + currency);
        sb.append(" optional=" + optional);
        return sb.toString();
    }
}
