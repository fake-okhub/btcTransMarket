package com.android.bitglobal.entity;

/**
 * xiezuofei
 * 2016-09-08 09:20
 * 793169940@qq.com
 *
 */
public class DepositMethod{
    private String name;//充值方式名称
    private String type;//1汇款 2 支付宝

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "name=" + name
                +" type=" + type
                +" | ";
    }
}
