package com.android.bitglobal.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bitbank on 17/2/28.
 */
public class MarketDatasNoTLineResult extends RealmObject {
    private String time;
    private TickerData ticker;
    private String symbol;
    @PrimaryKey
    private String coinName;
    private String name;
    private String cName;
    private String exeByRate;
    private String type;
    private String moneyType;

    public TickerData getTicker() {
        return ticker;
    }

    public void setTicker(TickerData ticker) {
        this.ticker = ticker;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TickerData getTickerData() {
        return ticker;
    }

    public void setTickerData(TickerData tickerData) {
        this.ticker = tickerData;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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

    @Override
    public String toString() {
        return "tline"
                +" | ";
    }

}
