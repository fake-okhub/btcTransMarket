package com.android.bitglobal.entity;

import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2017/3/8.
 */

public class VipLevelInfo {

    //主键
    private String id;
    //主键
    private String myId;
    //等级
    private int vipRate;
    //等级对应积分
    private int jifen;
    //手续费折扣
    private double discount;
    //备注
    private String memo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public int getVipRate() {
        return vipRate;
    }

    public void setVipRate(int vipRate) {
        this.vipRate = vipRate;
    }

    public int getJifen() {
        return jifen;
    }

    public void setJifen(int jifen) {
        this.jifen = jifen;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}
