package com.android.bitglobal.entity;


/**
 * xiezuofei
 * 2016-08-27 13:20
 * 793169940@qq.com
 * 账单数组
 */
public class WithdrawDetail {
    private String id;//
    private String submitTimestamp;//提交时间(时间戳)
    private String toAddress;//提现地址
    private String status;//状态 0:打币中 1:失败 2:成功 3:已取消
    private String showStat;//状态(中文)
    private String commandId;//客服审核 0:未审核(此时如果status=0,则为待审 核) >0:已审核(此时如果status=0,则为打币 中)
    private String manageTimestamp;//处理时间(时间戳)
    private String doubleAmount;//提现金额
    private String afterAmount;//实际到账
    private String fee;//手续费
    private String remark;//备注

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubmitTimestamp() {
        return submitTimestamp;
    }

    public void setSubmitTimestamp(String submitTimestamp) {
        this.submitTimestamp = submitTimestamp;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShowStat() {
        return showStat;
    }

    public void setShowStat(String showStat) {
        this.showStat = showStat;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getManageTimestamp() {
        return manageTimestamp;
    }

    public void setManageTimestamp(String manageTimestamp) {
        this.manageTimestamp = manageTimestamp;
    }

    public String getDoubleAmount() {
        return doubleAmount;
    }

    public void setDoubleAmount(String doubleAmount) {
        this.doubleAmount = doubleAmount;
    }

    public String getAfterAmount() {
        return afterAmount;
    }

    public void setAfterAmount(String afterAmount) {
        this.afterAmount = afterAmount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "id=" + id
                + " submitTimestamp=" + submitTimestamp
                + " toAddress=" + toAddress
                + " status=" + status
                + " showStat=" + showStat
                + " commandId=" + commandId
                + " manageTimestamp=" + manageTimestamp
                + " doubleAmount=" + doubleAmount
                + " afterAmount=" + afterAmount
                + " fee=" + fee
                + " remark=" + remark
                +" | ";
    }
}
