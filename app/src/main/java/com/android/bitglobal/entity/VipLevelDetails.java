package com.android.bitglobal.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/3/8.
 */

public class VipLevelDetails {

    //时间
    private Time addTime;
    //加减分
    private int ioType;
    //积分
    private BigDecimal jifen;
    //描述
    private String memo;
    //类型
    private String typeValue;

    public Time getAddTime() {
        return addTime;
    }

    public void setAddTime(Time addTime) {
        this.addTime = addTime;
    }

    public int getIoType() {
        return ioType;
    }

    public void setIoType(int ioType) {
        this.ioType = ioType;
    }

    public BigDecimal getJifen() {
        return jifen;
    }

    public void setJifen(BigDecimal jifen) {
        this.jifen = jifen;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

}
