package com.android.bitglobal.entity;


/**
 * xiezuofei
 * 2016-08-30 16:20
 * 793169940@qq.com
 *  借款记录
 */
public class LoadRecord {
    //借款id
    private String id;
    //货币类型
    private String currencyType;
    //借入金额
    private String debtAmount;
    //借款利率
    private String rate;
    //已还金额
    private String hasRepay;
    //借款时间
    private String submitTime;
    //是否使用免息劵
    private String isUsedTicket;
    //总免息天数
    private String daysWithNoRate;
    //剩余免息天数
    private String restDaysWithNoRate;
    //免息额度
    private String amountWithNoRate;
    //已计息天数
    private String daysWithRate;
    //总计利息金额
    private String interestAmount;
    //应还总额
    private String amountShouldBeRepay;
    private String status;
    //剩余本金
    private String remainingPrincipal;
    //已还本金
    private String principal;

    private String isPart;
    private String select_status;
    private String setect_amount;
    private String select_remaining;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(String debtAmount) {
        this.debtAmount = debtAmount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getHasRepay() {
        return hasRepay;
    }

    public void setHasRepay(String hasRepay) {
        this.hasRepay = hasRepay;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getIsUsedTicket() {
        return isUsedTicket;
    }

    public void setIsUsedTicket(String isUsedTicket) {
        this.isUsedTicket = isUsedTicket;
    }

    public String getDaysWithNoRate() {
        return daysWithNoRate;
    }

    public void setDaysWithNoRate(String daysWithNoRate) {
        this.daysWithNoRate = daysWithNoRate;
    }

    public String getRestDaysWithNoRate() {
        return restDaysWithNoRate;
    }

    public void setRestDaysWithNoRate(String restDaysWithNoRate) {
        this.restDaysWithNoRate = restDaysWithNoRate;
    }

    public String getAmountWithNoRate() {
        return amountWithNoRate;
    }

    public void setAmountWithNoRate(String amountWithNoRate) {
        this.amountWithNoRate = amountWithNoRate;
    }

    public String getDaysWithRate() {
        return daysWithRate;
    }

    public void setDaysWithRate(String daysWithRate) {
        this.daysWithRate = daysWithRate;
    }

    public String getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(String interestAmount) {
        this.interestAmount = interestAmount;
    }

    public String getAmountShouldBeRepay() {
        return amountShouldBeRepay;
    }

    public void setAmountShouldBeRepay(String amountShouldBeRepay) {
        this.amountShouldBeRepay = amountShouldBeRepay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemainingPrincipal() {
        return remainingPrincipal;
    }

    public void setRemainingPrincipal(String remainingPrincipal) {
        this.remainingPrincipal = remainingPrincipal;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getIsPart() {
        return isPart;
    }

    public void setIsPart(String isPart) {
        this.isPart = isPart;
    }

    public String getSelect_status() {
        if(select_status==null){
            select_status="0";
        }
        return select_status;
    }

    public void setSelect_status(String select_status) {
        this.select_status = select_status;
    }

    public String getSetect_amount() {
        return setect_amount;
    }

    public void setSetect_amount(String setect_amount) {
        this.setect_amount = setect_amount;
    }

    public String getSelect_remaining() {
        return select_remaining;
    }

    public void setSelect_remaining(String select_remaining) {
        this.select_remaining = select_remaining;
    }
}
