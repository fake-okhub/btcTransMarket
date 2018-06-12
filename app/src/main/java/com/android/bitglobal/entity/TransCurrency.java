package com.android.bitglobal.entity;


/**
 * xiezuofei
 * 2016-08-04 13:20
 * 793169940@qq.com
 *当前交易的币种
 */
public class TransCurrency {
    private String exchangeType;
    private String currencyType;

    private String exchangeType_symbol;
    private String currencyType_symbol;

    private String url;

    private String prizeRange;
    private String btcPrizeRange;
    private String usdcPrizeRange;
    private String[] marketDepth;//行情深度
    private String[] marketLength;//行情深度

    public String getBtcPrizeRange() {
        return btcPrizeRange;
    }

    public void setBtcPrizeRange(String btcPrizeRange) {
        this.btcPrizeRange = btcPrizeRange;
    }

    public String getUsdcPrizeRange() {
        return usdcPrizeRange;
    }

    public void setUsdcPrizeRange(String usdcPrizeRange) {
        this.usdcPrizeRange = usdcPrizeRange;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrizeRange() {
        return prizeRange;
    }

    public void setPrizeRange(String prizeRange) {
        this.prizeRange = prizeRange;
    }

    public String[] getMarketDepth() {
        return marketDepth;
    }

    public void setMarketDepth(String[] marketDepth) {
        this.marketDepth = marketDepth;
    }

    public String[] getMarketLength() {
        return marketLength;
    }

    public void setMarketLength(String[] marketLength) {
        this.marketLength = marketLength;
    }

    public String getExchangeType_symbol() {
        return exchangeType_symbol;
    }

    public void setExchangeType_symbol(String exchangeType_symbol) {
        this.exchangeType_symbol = exchangeType_symbol;
    }

    public String getCurrencyType_symbol() {
        return currencyType_symbol;
    }

    public void setCurrencyType_symbol(String currencyType_symbol) {
        this.currencyType_symbol = currencyType_symbol;
    }

    @Override
    public String toString() {
        return "exchangeType=" + exchangeType
                + " currencyType=" + currencyType
                + " exchangeType_symbol=" + exchangeType_symbol
                + " currencyType_symbol=" + currencyType_symbol
                + " url=" + url
                + " prizeRange=" + prizeRange
                + " marketDepth=" + marketDepth
                + " marketLength=" + marketLength
                +" | ";
    }
}
