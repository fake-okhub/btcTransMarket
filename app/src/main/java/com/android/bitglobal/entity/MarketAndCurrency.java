package com.android.bitglobal.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * xiezuofei
 * 2016-08-04 13:20
 * 793169940@qq.com
 * 首页数据
 */
public class MarketAndCurrency {
    private String is_visible;
    private TickerData tickerData;
    private CurrencySetRealm currencySetRealm;
    private String coinFullNameEn;
    private String symbol;
    private String coinName;
    private String name;

    public MarketAndCurrency() {
    }

    protected MarketAndCurrency(Parcel in) {
        is_visible = in.readString();
        coinFullNameEn = in.readString();
        symbol = in.readString();
        coinName = in.readString();
        name = in.readString();
        cName = in.readString();
        exeByRate = in.readString();
        type = in.readString();
        moneyType = in.readString();
        tickerData = in.readParcelable(TickerData.class.getClassLoader());
        currencySetRealm = in.readParcelable(CurrencySetRealm.class.getClassLoader());
    }

    public CurrencySetRealm getCurrencySetRealm() {
        return currencySetRealm;
    }

    public void setCurrencySetRealm(CurrencySetRealm currencySetRealm) {
        this.currencySetRealm = currencySetRealm;
    }

    public String getCoinFullNameEn() {
        return coinFullNameEn;
    }

    public void setCoinFullNameEn(String coinFullNameEn) {
        this.coinFullNameEn = coinFullNameEn;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getExeByRate() {
        return exeByRate;
    }

    public void setExeByRate(String exeByRate) {
        this.exeByRate = exeByRate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    private String cName;
    private String exeByRate;
    private String type;
    private String moneyType;

    private String[][] tline;

    public String[][] getTline() {
        return tline;
    }

    public void setTline(String[][] tline) {
        this.tline = tline;
    }
    public CurrencySetRealm getCurrencySet() {
        return currencySetRealm;
    }

    public void setCurrencySet(CurrencySetRealm currencySet) {
        this.currencySetRealm = currencySet;
    }

    public String getIs_visible() {
        if(is_visible==null||is_visible.equals("")){
            is_visible="0";
        }
        return is_visible;
    }

    public void setIs_visible(String is_visible) {

        this.is_visible = is_visible;
    }

    public TickerData getTickerData() {
        return tickerData;
    }

    public void setTickerData(TickerData tickerData) {
        this.tickerData = tickerData;
    }
    @Override
    public String toString() {
        return "tickerData=" + tickerData
                + " currencySetRealm=" + currencySetRealm
                +" | ";
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

}
