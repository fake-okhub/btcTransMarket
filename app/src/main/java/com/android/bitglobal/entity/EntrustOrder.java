package com.android.bitglobal.entity;

/**
 * xiezuofei
 * 2016-08-5 10:20
 * 793169940@qq.com
 * 委托详情
 */
public class EntrustOrder {
    private String entrustId;//记录id
    private String transactionPrice;//成交价格
    private String transactionTotalMoney;//成交额
    private String transactionNumber;//委托数量
    private String transactionTime;//成交时间(时间戳)

    public String getEntrustId() {
        return entrustId;
    }

    public void setEntrustId(String entrustId) {
        this.entrustId = entrustId;
    }

    public String getTransactionPrice() {
        return transactionPrice;
    }

    public void setTransactionPrice(String transactionPrice) {
        this.transactionPrice = transactionPrice;
    }

    public String getTransactionTotalMoney() {
        return transactionTotalMoney;
    }

    public void setTransactionTotalMoney(String transactionTotalMoney) {
        this.transactionTotalMoney = transactionTotalMoney;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    @Override
    public String toString() {
        return "entrustId=" + entrustId
                + " transactionPrice=" + transactionPrice
                + " transactionTotalMoney=" + transactionTotalMoney
                + " transactionNumber=" + transactionNumber
                + " transactionTime=" + transactionTime
                +" | ";
    }
}
