package com.android.bitglobal.entity;


import java.util.ArrayList;
import java.util.List;

/**
 * xiezuofei
 * 2016-08-27 13:20
 * 793169940@qq.com
 * 账单数组
 */
public class WithdrawDetailResult {
    private String pageIndex;
    private String pageSize;
    private String totalPage;
    private List<WithdrawDetail> withdrawDetails=new ArrayList<WithdrawDetail>();//账单数组

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

    public List<WithdrawDetail> getWithdrawDetails() {
        return withdrawDetails;
    }

    public void setWithdrawDetails(List<WithdrawDetail> withdrawDetails) {
        this.withdrawDetails = withdrawDetails;
    }

    @Override
    public String toString() {
        return "pageIndex=" + pageIndex
                + " pageSize=" + pageSize
                + " totalPage=" + totalPage
                + " withdrawDetails=" + withdrawDetails
                +" | ";
    }
}
