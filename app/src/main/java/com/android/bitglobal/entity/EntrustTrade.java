package com.android.bitglobal.entity;

/**
 * xiezuofei
 * 2016-08-17 15:20
 * 793169940@qq.com
 * 委托交易数据
 */
public class EntrustTrade {
    private String entrustId;
    private String type;//类型 0:卖出 1:买入
    private String submitTime;//提交时间
    private String unitPrice;//单价
    private String number;//数量
    private String completeNumber;//已完成数量
    private double junjia;//成交均价
    private String completeTotalMoney;//成交金额
    private String status;//状态 0:正在进行 1:已取消 2:已成交 3:待 成交 ­1:计划中
    private double entrust_total;//委托总额
    private double complete_rate;//成交进度

    public double getEntrust_total() {
        return entrust_total;
    }

    public void setEntrust_total(double entrust_total) {
        this.entrust_total = entrust_total;
    }

    public double getComplete_rate() {
        return complete_rate;
    }

    public void setComplete_rate(double complete_rate) {
        this.complete_rate = complete_rate;
    }

    public String getEntrustId() {
        return entrustId;
    }

    public void setEntrustId(String entrustId) {
        this.entrustId = entrustId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCompleteNumber() {
        return completeNumber;
    }

    public void setCompleteNumber(String completeNumber) {
        this.completeNumber = completeNumber;
    }

    public double getJunjia() {
        return junjia;
    }

    public void setJunjia(double junjia) {
        this.junjia = junjia;
    }

    public String getCompleteTotalMoney() {
        return completeTotalMoney;
    }

    public void setCompleteTotalMoney(String completeTotalMoney) {
        this.completeTotalMoney = completeTotalMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "entrustId=" + entrustId
                + " type=" + type
                + " submitTime=" + submitTime
                + " unitPrice=" + unitPrice
                + " number=" + number
                + " completeNumber=" + completeNumber
                + " junjia=" + junjia
                + " completeTotalMoney=" + completeTotalMoney
                + " status=" + status
                +" | ";
    }
}
