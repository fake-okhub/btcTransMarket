package com.android.bitglobal.entity;

/**
 * xiezuofei
 * 2016-09-22 16:20
 * 793169940@qq.com
 * 银行交易信息
 */
public class BankTrade {
    private String serialNumber;//流水号
    private String bankId;//类型 1:支付宝 >1:其他银行卡
    private String receiveAccount;//收款账号
    private String payAccount;//付款账号
    private String receiveBankName;//银行名称(支付宝为空)
    private String payBankName;//付款银行名称/支付宝
    private String type;//1:充值0:提现
    private String date;//提交时间(时间戳)
    private String description;//描述
    private String status;//状态值
    private String statusShow;
    private String fees;//手续费
    private String withdrawType;//提现类型 1 24小时内到账(免费) 2 15分钟内极速到账
    private String withdrawTypeShow;//提现类型 1 24小时内到账(免费) 2 15分钟内极速到账
    private String amount;//充值/提现金额
    private String actualAmount;//实际金额
    private String payer;//付款方
    private String receiver;//收款方
    private String rechargeMethod;//充值方式 0 易宝支付 1 线下银行汇款 2 支付宝转账充 值 3 艺粹交易 4 贝宝转账充值 5 系统赠送 6 系统充值 7 系统扣除 8 财付通转账充值 9/10 网银自动充值 12 充值卡充值 其他显示”–”
    private String payBankLink;//充值时为相应银行链接
    private String receiveBankTailNumber;//收款银行尾号
    private String payBankTailNumber;//付款银行/付款支付宝账户尾号
    private String tips;//温馨提示
    private String yue;//
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getReceiveAccount() {
        return receiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        this.receiveAccount = receiveAccount;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getReceiveBankName() {
        return receiveBankName;
    }

    public void setReceiveBankName(String receiveBankName) {
        this.receiveBankName = receiveBankName;
    }

    public String getPayBankName() {
        return payBankName;
    }

    public void setPayBankName(String payBankName) {
        this.payBankName = payBankName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getWithdrawType() {
        return withdrawType;
    }

    public void setWithdrawType(String withdrawType) {
        this.withdrawType = withdrawType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getRechargeMethod() {
        return rechargeMethod;
    }

    public void setRechargeMethod(String rechargeMethod) {
        this.rechargeMethod = rechargeMethod;
    }

    public String getPayBankLink() {
        return payBankLink;
    }

    public void setPayBankLink(String payBankLink) {
        this.payBankLink = payBankLink;
    }

    public String getReceiveBankTailNumber() {
        return receiveBankTailNumber;
    }

    public void setReceiveBankTailNumber(String receiveBankTailNumber) {
        this.receiveBankTailNumber = receiveBankTailNumber;
    }

    public String getPayBankTailNumber() {
        return payBankTailNumber;
    }

    public void setPayBankTailNumber(String payBankTailNumber) {
        this.payBankTailNumber = payBankTailNumber;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getStatusShow() {
        return statusShow;
    }

    public void setStatusShow(String statusShow) {
        this.statusShow = statusShow;
    }

    public String getYue() {
        return yue;
    }

    public void setYue(String yue) {
        this.yue = yue;
    }

    public String getWithdrawTypeShow() {
        return withdrawTypeShow;
    }

    public void setWithdrawTypeShow(String withdrawTypeShow) {
        this.withdrawTypeShow = withdrawTypeShow;
    }
}
