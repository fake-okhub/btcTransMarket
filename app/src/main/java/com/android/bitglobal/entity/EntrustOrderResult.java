package com.android.bitglobal.entity;


import java.util.ArrayList;
import java.util.List;

/**
 * xiezuofei
 * 2016-08-25 10:20
 * 793169940@qq.com
 * 委托详情数组返回
 */
public class EntrustOrderResult {
    private List<EntrustOrder> entrustOrders=new ArrayList<EntrustOrder>();

    public List<EntrustOrder> getEntrustOrders() {
        return entrustOrders;
    }

    public void setEntrustOrders(List<EntrustOrder> entrustOrders) {
        this.entrustOrders = entrustOrders;
    }

    @Override
    public String toString() {
        return  "entrustOrders=" + entrustOrders
                +" | ";
    }
}
