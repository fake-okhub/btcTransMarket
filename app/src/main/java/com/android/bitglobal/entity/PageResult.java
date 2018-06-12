package com.android.bitglobal.entity;


import java.util.ArrayList;
import java.util.List;

/**
 * xiezuofei
 * 2016-08-29 10:20
 * 793169940@qq.com
 * 数组返回
 */
public class PageResult {
    private String pageIndex;//页码
    private String pageSize;//每页显示数量
    private String totalPage;//总页数

    private List<EntrustTrade> entrustTrades=new ArrayList<EntrustTrade>();
    private List<InterestFreeTicket> tickets=new ArrayList<InterestFreeTicket>();
    private List<LoadRecord> loanRecords=new ArrayList<LoadRecord>();
    private List<RepayRecord> repayRecords=new ArrayList<RepayRecord>();
    private List<RechargeBank> rechargeBanks=new ArrayList<RechargeBank>();
    private List<HistoryBank> historyBanks=new ArrayList<HistoryBank>();
    private List<BillDetail> billDetails=new ArrayList<BillDetail>();
    private List<BankTrade> bankTrades=new ArrayList<BankTrade>();

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

    public List<InterestFreeTicket> getTickets() {
        return tickets;
    }

    public void setTickets(List<InterestFreeTicket> tickets) {
        this.tickets = tickets;
    }

    public List<LoadRecord> getLoanRecords() {
        return loanRecords;
    }

    public void setLoanRecords(List<LoadRecord> loanRecords) {
        this.loanRecords = loanRecords;
    }

    public List<RepayRecord> getRepayRecords() {
        return repayRecords;
    }

    public void setRepayRecords(List<RepayRecord> repayRecords) {
        this.repayRecords = repayRecords;
    }

    public List<RechargeBank> getRechargeBanks() {
        return rechargeBanks;
    }

    public void setRechargeBanks(List<RechargeBank> rechargeBanks) {
        this.rechargeBanks = rechargeBanks;
    }

    public List<HistoryBank> getHistoryBanks() {
        return historyBanks;
    }

    public void setHistoryBanks(List<HistoryBank> historyBanks) {
        this.historyBanks = historyBanks;
    }

    public List<BillDetail> getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(List<BillDetail> billDetails) {
        this.billDetails = billDetails;
    }

    public List<BankTrade> getBankTrades() {
        return bankTrades;
    }

    public void setBankTrades(List<BankTrade> bankTrades) {
        this.bankTrades = bankTrades;
    }

    @Override
    public String toString() {
        return  "pageIndex=" + pageIndex
                + " pageSize=" + pageSize
                + " totalPage=" + totalPage
                + " entrustTrades=" + entrustTrades
                + " tickets=" + tickets
                + " loanRecords=" + loanRecords
                + " repayRecords=" + repayRecords
                + " rechargeBanks=" + rechargeBanks
                + " historyBanks=" + historyBanks
                + " billDetails=" + billDetails
                +" | ";
    }
}
