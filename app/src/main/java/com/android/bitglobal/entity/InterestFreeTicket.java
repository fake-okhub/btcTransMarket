package com.android.bitglobal.entity;


/**
 * xiezuofei
 * 2016-08-29 11:20
 * 793169940@qq.com
 * 免息卷信息
 */
public class InterestFreeTicket {
    private String id;//免息卷id
    private String source;//免息卷来源
    private String type;//免息卷类型0 系统赠送 1 活动赠送
    private String totalAmount;//免息卷面值
    private String available;//可用金额
    private String freePeriod;//免息天数
    private String status;//免息卷状态 0 未激活 1已过期2已激活3禁止使用4 已使用
    private String expiryTime;//过期时间(时间戳);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getFreePeriod() {
        return freePeriod;
    }

    public void setFreePeriod(String freePeriod) {
        this.freePeriod = freePeriod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }

    @Override
    public String toString() {

        return "id=" + id
                + " source=" + source
                + " type=" + type
                + " totalAmount=" + totalAmount
                + " available=" + available
                + " freePeriod=" + freePeriod
                + " status=" + status
                + " expiryTime=" + expiryTime
                +" | ";
    }
}
