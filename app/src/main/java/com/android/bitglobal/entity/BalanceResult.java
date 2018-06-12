package com.android.bitglobal.entity;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bitbank on 16/12/23.
 */
public class BalanceResult extends RealmObject {

    @PrimaryKey
    private String fundsType;
    private String unitTag;
    private String loanOutStatus;
    private String repayLevel;
    private String loanInStatus;
    private String  outWait ;
    private String interestOfDay;
    private String overdraft;
    private String inSuccess ;
    private String coinUrl;
    private String financeCoinUrl;
    private String withdrawFreeze;
    private String outSuccess;
    private String total;
    private String balance;
    private String propTag;
    private String coinFullNameEn;
    private String freeze;
    private String inWait;

    public String getUnitTag() {
        return unitTag;
    }

    public void setUnitTag(String unitTag) {
        this.unitTag = unitTag;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPropTag() {
        return propTag;
    }

    public void setPropTag(String propTag) {
        this.propTag = propTag;
    }

    public String getCoinFullNameEn() {
        return coinFullNameEn;
    }

    public void setCoinFullNameEn(String coinFullNameEn) {
        this.coinFullNameEn = coinFullNameEn;
    }

    public String getFreeze() {
        return freeze;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }

    public String getFundsType() {
        return fundsType;
    }

    public void setFundsType(String fundsType) {
        this.fundsType = fundsType;
    }

    public String getInWait() {
        return inWait;
    }

    public void setInWait(String inWait) {
        this.inWait = inWait;
    }

    public String getLoanOutStatus() {
        return loanOutStatus;
    }

    public void setLoanOutStatus(String loanOutStatus) {
        this.loanOutStatus = loanOutStatus;
    }

    public String getRepayLevel() {
        return repayLevel;
    }

    public void setRepayLevel(String repayLevel) {
        this.repayLevel = repayLevel;
    }

    public String getLoanInStatus() {
        return loanInStatus;
    }

    public void setLoanInStatus(String loanInStatus) {
        this.loanInStatus = loanInStatus;
    }

    public String getOutWait() {
        return outWait;
    }

    public void setOutWait(String outWait) {
        this.outWait = outWait;
    }

    public String getInterestOfDay() {
        return interestOfDay;
    }

    public void setInterestOfDay(String interestOfDay) {
        this.interestOfDay = interestOfDay;
    }

    public String getOverdraft() {
        return overdraft;
    }

    public void setOverdraft(String overdraft) {
        this.overdraft = overdraft;
    }

    public String getInSuccess() {
        return inSuccess;
    }

    public void setInSuccess(String inSuccess) {
        this.inSuccess = inSuccess;
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

    public String getWithdrawFreeze() {
        return withdrawFreeze;
    }

    public void setWithdrawFreeze(String withdrawFreeze) {
        this.withdrawFreeze = withdrawFreeze;
    }

    public String getOutSuccess() {
        return outSuccess;
    }

    public void setOutSuccess(String outSuccess) {
        this.outSuccess = outSuccess;
    }

    @Override
    public String toString() {
        return "fundsType="+fundsType+",unitTag"+unitTag+",loanOutStatus"+loanOutStatus+",repayLevel"+repayLevel+",loanInStatus"+loanInStatus+",outWait"+outWait+",interestOfDay"+interestOfDay+",overdraft"+overdraft+",inSuccess"+inSuccess+",coinUrl"+coinUrl+",financeCoinUrl"+financeCoinUrl
                +",withdrawFreeze"+withdrawFreeze+",outSuccess"+outSuccess+",total"+total+",balance"+balance+",propTag"+propTag+",freeze"+freeze+",inWait"+inWait;
    }
}
