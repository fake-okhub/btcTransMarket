package com.android.bitglobal.entity;

import java.util.List;

/**
 * xiezuofei
 * 2016-08-02 13:20
 * 793169940@qq.com
 * 用户基本信息
 */
public class CurrencySet{
    private String currency;//当前货币类型
    private String symbol;//货币符号
    private String name;//货币名称
    private String englishName;//货币名称
    private String coinUrl;// 货币图标链接
    private String financeCoinUrl;// 货币图标链接
    private String prizeRange;
    private List<CurrencyMarketDepth> marketDepth;//行情深度
    private List<CurrencyMarketDepth> marketLength;//行情深度


    private String dayFreetrial ;// 日免审额度 |
    private String dayCash;// 每日允许提现的额度 |

    public String getDayFreetrial() {
        return dayFreetrial;
    }

    public void setDayFreetrial(String dayFreetrial) {
        this.dayFreetrial = dayFreetrial;
    }

    public String getDayCash() {
        return dayCash;
    }

    public void setDayCash(String dayCash) {
        this.dayCash = dayCash;
    }

    public String getTimesCash() {
        return timesCash;
    }

    public void setTimesCash(String timesCash) {
        this.timesCash = timesCash;
    }

    public String getMinFees() {
        return minFees;
    }

    public void setMinFees(String minFees) {
        this.minFees = minFees;
    }

    public String getInConfirmTimes() {
        return inConfirmTimes;
    }

    public void setInConfirmTimes(String inConfirmTimes) {
        this.inConfirmTimes = inConfirmTimes;
    }

    public String getOutConfirmTimes() {
        return outConfirmTimes;
    }

    public void setOutConfirmTimes(String outConfirmTimes) {
        this.outConfirmTimes = outConfirmTimes;
    }

    public String getMinCash() {
        return minCash;
    }

    public void setMinCash(String minCash) {
        this.minCash = minCash;
    }

    private String timesCash ;// 次提现额度 |
    private String  minFees ;// 交易手续费 |
    private String  inConfirmTimes ;// 充值到账的确认次数 |
    private String outConfirmTimes ;//允许提现的确认次数 |
    private String  minCash ;// 最小提现额度 |


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

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

    public String getCoinUrl() {
        return coinUrl;
    }

    public void setCoinUrl(String coinUrl) {
        this.coinUrl = coinUrl;
    }

    public String getFinanceCoinUrl() {
        return financeCoinUrl;
    }

    public void setFinanceCoinUrl(String financeCoinUrl) {
        this.financeCoinUrl = financeCoinUrl;
    }

    public String getPrizeRange() {
        return prizeRange;
    }

    public void setPrizeRange(String prizeRange) {
        this.prizeRange = prizeRange;
    }

    public List<CurrencyMarketDepth> getMarketDepth() {
        return marketDepth;
    }

    public void setMarketDepth(List<CurrencyMarketDepth> marketDepth) {
        this.marketDepth = marketDepth;
    }

    public List<CurrencyMarketDepth> getMarketLength() {
        return marketLength;
    }

    public void setMarketLength(List<CurrencyMarketDepth> marketLength) {
        this.marketLength = marketLength;
    }

    @Override
    public String toString() {
        return "currency=" + currency
                + "symbol=" + symbol
                + " name=" + name
                + " englishName=" + englishName
                + " coinUrl=" + coinUrl
                + " financeCoinUrl=" + financeCoinUrl
                + " prizeRange=" + prizeRange
                + " marketDepth=" + marketDepth
                + " marketLength=" + marketLength
                +" | ";
    }
}
