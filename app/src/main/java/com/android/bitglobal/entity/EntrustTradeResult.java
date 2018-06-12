package com.android.bitglobal.entity;


import java.util.ArrayList;
import java.util.List;

/**
 * xiezuofei
 * 2016-08-17 15:20
 * 793169940@qq.com
 * 委托交易数组返回
 */
public class EntrustTradeResult {
    private List<EntrustTrade> entrustTrades=new ArrayList<EntrustTrade>();
    private String pageIndex;//页码
    private String pageSize;//每页显示数量
    private String totalPage;//总页数

    public List<EntrustTrade> getEntrustTrades() {
        return entrustTrades;
    }

    public void setEntrustTrades(List<EntrustTrade> entrustTrades) {
        this.entrustTrades = entrustTrades;
    }

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return  "pageIndex=" + pageIndex
                + " pageSize=" + pageSize
                + " totalPage=" + totalPage
                + " entrustTrades=" + entrustTrades
                +" | ";
    }
}
