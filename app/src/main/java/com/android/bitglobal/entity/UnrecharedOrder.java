package com.android.bitglobal.entity;

/**
 * xiezuofei
 * 2016-09-07 16:00
 * 793169940@qq.com
 */
public class UnrecharedOrder{
    private String serialNumber;//流水号
    private String amount;//充值金额
    private String actualAmount;//实际金额
    private String status;//状态,此处=9，待充值
    private String total;//记录数

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "serialNumber=" + serialNumber
                + " amount=" + amount
                + " actualAmount=" + actualAmount
                + " status=" + status
                + " total=" + total
                +" | ";
    }
}
