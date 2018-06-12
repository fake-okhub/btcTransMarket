package com.android.bitglobal.entity;


import java.util.List;

/**
 * xiezuofei
 * 2016-08-02 13:20
 * 793169940@qq.com
 * 用户基本信息
 */
public class CurrencyResult{
    private Long version;//版本号
    private List<CurrencySet> currencySets;//货币配置数组

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<CurrencySet> getCurrencySets() {
        return currencySets;
    }

    public void setCurrencySets(List<CurrencySet> currencySets) {
        this.currencySets = currencySets;
    }

    @Override
    public String toString() {
        return "version=" + version
                + " currencySets=" + currencySets
                +" | ";
    }
}
