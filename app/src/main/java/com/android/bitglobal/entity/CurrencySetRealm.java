package com.android.bitglobal.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-08-02 13:20
 * 793169940@qq.com
 * 配置货币信息
 */
public class CurrencySetRealm extends RealmObject {
    public CurrencySetRealm (){

    }
    public CurrencySetRealm (CurrencySet cs){
        currency=cs.getCurrency();
        symbol=cs.getSymbol();
        name=cs.getName();
        englishName=cs.getEnglishName();
        coinUrl=cs.getCoinUrl();
        financeCoinUrl=cs.getFinanceCoinUrl();
        prizeRange=cs.getPrizeRange();
        dayFreetrial = cs.getDayFreetrial();
        dayCash=cs.getDayCash();
        timesCash=cs.getTimesCash();
        minFees=cs.getMinFees();
        inConfirmTimes=cs.getInConfirmTimes();
        outConfirmTimes=cs.getOutConfirmTimes();
        minCash=cs.getMinCash();
       /* for(CurrencyMarketDepth cmd: cs.getMarketDepth()){
            CurrencyMarketDepthRealm smdr=new CurrencyMarketDepthRealm();
            smdr.setCurrency(cmd.getCurrency());
            RealmList<EntityString>  estr_l=new RealmList<EntityString>();
            if(cmd.getOptional()!=null&&cmd.getOptional().length>0){
                for (String str:cmd.getOptional()){
                    EntityString es=new EntityString();
                    es.setStr(str);
                    estr_l.add(es);
                }
            }
            smdr.setOptional(estr_l);
            marketDepth.add(smdr);
        }*/
        for(CurrencyMarketDepth cmd: cs.getMarketLength()){
            CurrencyMarketDepthRealm smdr=new CurrencyMarketDepthRealm();
            smdr.setCurrency(cmd.getCurrency());
            RealmList<EntityString>  estr_l=new RealmList<EntityString>();
            if(cmd.getOptional()!=null&&cmd.getOptional().length>0){
                for (String str:cmd.getOptional()){
                    EntityString es=new EntityString();
                    es.setStr(str);
                    estr_l.add(es);
                }
            }
            smdr.setOptional(estr_l);
            marketLength.add(smdr);
        }
    }
    @PrimaryKey
    private String currency;//当前货币类型
    private String symbol;//货币符号
    private String name;//货币名称
    private String englishName;//货币名称
    private String coinUrl;// 货币图标链接
    private String financeCoinUrl;// 货币图标链接
    private String prizeRange;
    private RealmList<CurrencyMarketDepthRealm> marketDepth=new RealmList<CurrencyMarketDepthRealm>();//行情深度
    private RealmList<CurrencyMarketDepthRealm> marketLength=new RealmList<CurrencyMarketDepthRealm>();//行情深度


    private String dayFreetrial ;// 日免审额度 |
    private String dayCash;// 每日允许提现的额度 |

    public String getDayFreetrial() {
        return dayFreetrial;
    }

    public void setDayFreetrial(String dayFreetrial) {
        this.dayFreetrial = dayFreetrial;
    }

    public String getDayCash() {
        return dayCash;
    }

    public void setDayCash(String dayCash) {
        this.dayCash = dayCash;
    }

    public String getTimesCash() {
        return timesCash;
    }

    public void setTimesCash(String timesCash) {
        this.timesCash = timesCash;
    }

    public String getMinFees() {
        return minFees;
    }

    public void setMinFees(String minFees) {
        this.minFees = minFees;
    }

    public String getInConfirmTimes() {
        return inConfirmTimes;
    }

    public void setInConfirmTimes(String inConfirmTimes) {
        this.inConfirmTimes = inConfirmTimes;
    }

    public String getOutConfirmTimes() {
        return outConfirmTimes;
    }

    public void setOutConfirmTimes(String outConfirmTimes) {
        this.outConfirmTimes = outConfirmTimes;
    }

    public String getMinCash() {
        return minCash;
    }

    public void setMinCash(String minCash) {
        this.minCash = minCash;
    }

    private String timesCash ;// 次提现额度 |
    private String  minFees ;// 交易手续费 |
    private String  inConfirmTimes ;// 充值到账的确认次数 |
    private String outConfirmTimes ;//允许提现的确认次数 |
    private String  minCash ;// 最小提现额度 |

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getCoinUrl() {
        return coinUrl;
    }

    public void setCoinUrl(String coinUrl) {
        this.coinUrl = coinUrl;
    }

    public String getFinanceCoinUrl() {
        return financeCoinUrl;
    }

    public void setFinanceCoinUrl(String financeCoinUrl) {
        this.financeCoinUrl = financeCoinUrl;
    }

    public String getPrizeRange() {
        return prizeRange;
    }

    public void setPrizeRange(String prizeRange) {
        this.prizeRange = prizeRange;
    }

    public RealmList<CurrencyMarketDepthRealm> getMarketDepth() {
        return marketDepth;
    }

    public void setMarketDepth(RealmList<CurrencyMarketDepthRealm> marketDepth) {
        this.marketDepth = marketDepth;
    }

    public RealmList<CurrencyMarketDepthRealm> getMarketLength() {
        return marketLength;
    }

    public void setMarketLength(RealmList<CurrencyMarketDepthRealm> marketLength) {
        this.marketLength = marketLength;
    }

    @Override
    public String toString() {
        return "currency=" + currency
                + " symbol=" + symbol
                + " name=" + name
                + " englishName=" + englishName
                + " coinUrl=" + coinUrl
                + " financeCoinUrl=" + financeCoinUrl
                + " prizeRange=" + prizeRange
                + " marketDepth=" + marketDepth
                + " marketLength=" + marketLength
                +" | ";
    }

}
