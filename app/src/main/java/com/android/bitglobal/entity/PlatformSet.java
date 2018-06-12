package com.android.bitglobal.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-08-04 13:20
 * 793169940@qq.com
 *
 */
public class PlatformSet extends RealmObject {
    @PrimaryKey
    private String symbol;//androidbtccny
    private String name;//平台名称
    private String englishName;//平台英文名称
    private String currencyType;//类型：BTC,ETH
    private String isVisible;//是否显示
    private String isxs;//是否显示
    private int seq;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible;
    }

    public String getIsxs() {
        return isxs;
    }

    public void setIsxs(String isxs) {
        this.isxs = isxs;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "symbol=" + symbol
                + " name=" + name
                + " englishName=" + englishName
                + " currencyType=" + currencyType
                + " isVisible=" + isVisible
                + " isxs=" + isxs
                + " seq=" + seq
                +" | ";
    }
}
