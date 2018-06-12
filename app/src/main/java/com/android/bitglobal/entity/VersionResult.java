package com.android.bitglobal.entity;


import java.util.List;

/**
 * xiezuofei
 * 2016-08-02 13:20
 * 793169940@qq.com
 * 版本信息
 */
public class VersionResult {
    private Long version;//版本号
    private List<CurrencySet> currencySets;//货币配置数组
    private List<Country> countries;//货币配置数组
    private List<MarketRemindResult> marketReminds;
    private List<CounterFee> feeInfos;
    private List<Article> articles;
    private List<ElementSetting> elementSettings;

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

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public List<MarketRemindResult> getMarketReminds() {
        return marketReminds;
    }

    public void setMarketReminds(List<MarketRemindResult> marketReminds) {
        this.marketReminds = marketReminds;
    }

    public List<CounterFee> getFeeInfos() {
        return feeInfos;
    }

    public void setFeeInfos(List<CounterFee> feeInfos) {
        this.feeInfos = feeInfos;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<ElementSetting> getElementSettings() {
        return elementSettings;
    }

    public void setElementSettings(List<ElementSetting> elementSettings) {
        this.elementSettings = elementSettings;
    }

    @Override
    public String toString() {
        return "version=" + version
                + " currencySets=" + currencySets
                + " countries=" + countries
                + " marketReminds=" + marketReminds
                + " feeInfos=" + feeInfos
                + " articles=" + articles
                + " elementSettings=" + elementSettings
                + " | ";
    }
}
