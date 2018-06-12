package com.android.bitglobal.entity;
/**
 * xiezuofei
 * 2016-09-19 17:50
 * 793169940@qq.com
 * 价格警报
 */
public class MarketRemindResult {

    //提醒ID
    private String id;
    //当前币种
    private String currency;
    //交易币种
    private String exchange;
    //设置的交易价格
    private String price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    //    private String status;
//    private String high;
//    private String low;
//    private String currencyType;
//    private String currentPrice;
//
//    public String getStatus() {
//        if(status==null||status.equals("")){
//            status="0";
//        }
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getHigh() {
//        if(high==null){
//            high="";
//        }
//        return high;
//    }
//
//    public void setHigh(String high) {
//        this.high = high;
//    }
//
//    public String getLow() {
//        if(low==null){
//            low="";
//        }
//        return low;
//    }
//
//    public void setLow(String low) {
//        this.low = low;
//    }
//
//    public String getCurrencyType() {
//        return currencyType;
//    }
//
//    public void setCurrencyType(String currencyType) {
//        this.currencyType = currencyType;
//    }
//
//    public String getCurrentPrice() {
//        return currentPrice;
//    }
//
//    public void setCurrentPrice(String currentPrice) {
//        this.currentPrice = currentPrice;
//    }
//
//    @Override
//    public String toString() {
//        return "status=" + status
//                + " high=" + high
//                + " low=" + low
//                + " currencyType=" + currencyType
//                + " currentPrice=" + currentPrice
//                +" | ";
//    }
}
