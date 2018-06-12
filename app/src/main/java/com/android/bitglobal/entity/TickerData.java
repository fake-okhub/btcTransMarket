package com.android.bitglobal.entity;

import android.text.TextUtils;

import com.android.bitglobal.tool.StringUtils;

import io.realm.RealmObject;

public class TickerData extends RealmObject{

    private String buy = "";                   // 买一价（人民币）
    private String buydollar = "";               // 买一价（美元）
    private String date = "";                    // 时间戳
    private String dollar = "";                  // 最新成交价（美元）
    private String high = "";                  // 最高价（人民币）
    private String highdollar = "";              // 最高价（美元）
    private String last = "";                // 最新成交价（人民币）
    private String low = "";                     // 最低价（人民币）
    private String lowdollar = "";             // 最低价（美元）
    private String sell = "";                   // 卖一价（人民币）
    private String selldollar = "";              // 卖一价（美元）
    private String vol = "";                     // 成交量
    private String riseRate = "";
    private String totalBtcNum;
    private String legal_tender;

    public String getLegal_tender() {
        return legal_tender;
    }

    public void setLegal_tender(String legal_tender) {
        this.legal_tender = legal_tender;
    }

    public String getTotalBtcNum() {
        return totalBtcNum;
    }

    public void setTotalBtcNum(String totalBtcNum) {
        this.totalBtcNum = totalBtcNum;
    }

    public String getWeekRiseRate() {
        return weekRiseRate;
    }

    public void setWeekRiseRate(String weekRiseRate) {
        this.weekRiseRate = weekRiseRate;
    }

    public String getMonthRiseRate() {
        return monthRiseRate;
    }

    public void setMonthRiseRate(String monthRiseRate) {
        this.monthRiseRate = monthRiseRate;
    }

    private String weekRiseRate;//周涨跌幅
    private String monthRiseRate;//月涨跌幅
    private String hour6RiseRate;//月涨跌幅
    private String month3RiseRate;//月涨跌幅
    private String month6RiseRate;//月涨跌幅

    public String getHour6RiseRate() {
        return hour6RiseRate;
    }

    public void setHour6RiseRate(String hour6RiseRate) {
        this.hour6RiseRate = hour6RiseRate;
    }

    public String getMonth3RiseRate() {
        return month3RiseRate;
    }

    public void setMonth3RiseRate(String month3RiseRate) {
        this.month3RiseRate = month3RiseRate;
    }

    public String getMonth6RiseRate() {
        return month6RiseRate;
    }

    public void setMonth6RiseRate(String month6RiseRate) {
        this.month6RiseRate = month6RiseRate;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getBuydollar() {
        return buydollar;
    }

    public void setBuydollar(String buydollar) {
        this.buydollar = buydollar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDollar() {
        return dollar;
    }

    public void setDollar(String dollar) {
        this.dollar = dollar;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getHighdollar() {
        return highdollar;
    }

    public void setHighdollar(String highdollar) {
        this.highdollar = highdollar;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getLowdollar() {
        return lowdollar;
    }

    public void setLowdollar(String lowdollar) {
        this.lowdollar = lowdollar;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getSelldollar() {
        return selldollar;
    }

    public void setSelldollar(String selldollar) {
        this.selldollar = selldollar;
    }

    public String getVol() {
        if (StringUtils.isEmpty(vol)) {
            vol = "0";
        }
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getRiseRate() {
//        if(riseRate.equals("")){
//            riseRate="0.00";
//        }
        return riseRate;
    }

    public void setRiseRate(String riseRate) {
        this.riseRate = riseRate;
    }

    @Override
    public String toString() {
        return " buy=" + buy
                + " buydollar=" + buydollar
                + " date=" + date
                + " dollar=" + dollar
                + " high=" + high
                + " highdollar=" + highdollar
                + " last=" + last
                + " low=" + low
                + " lowdollar=" + lowdollar
                + " sell=" + sell
                + " selldollar=" + selldollar
                + " vol=" + vol
                + " riseRate=" + riseRate
                +" | ";
    }

    public void format(){
        if(!TextUtils.isEmpty(riseRate)){
            riseRate= StringUtils.formatDouble(Double.parseDouble(riseRate),2);
        }
        if(!TextUtils.isEmpty(monthRiseRate)){
            monthRiseRate= StringUtils.formatDouble(Double.parseDouble(monthRiseRate),2);
        }
        if(!TextUtils.isEmpty(weekRiseRate)){
            weekRiseRate= StringUtils.formatDouble(Double.parseDouble(weekRiseRate),2);
        }
    }

}
