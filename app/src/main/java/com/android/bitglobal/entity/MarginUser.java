package com.android.bitglobal.entity;


/**
 * xiezuofei
 * 2016-08-29 18:20
 * 793169940@qq.com
 * 融资融币信息
 */
public class MarginUser {
    private String currencyType;//货币类型: BTC:比特币,LTC:莱特币,RMB:人民币, ETH:Ethereum,ETC:Ethereum Classic
    private String netAssets;//净资产
    private String hasBorrowed;//已借入金额
    private String canBorrow;//可以借的金额
    private String liquidatePrice;//预估平仓价
    private String p2pOutRate;//当前最优日费率

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getNetAssets() {
        return netAssets;
    }

    public void setNetAssets(String netAssets) {
        this.netAssets = netAssets;
    }

    public String getHasBorrowed() {
        return hasBorrowed;
    }

    public void setHasBorrowed(String hasBorrowed) {
        this.hasBorrowed = hasBorrowed;
    }

    public String getCanBorrow() {
        return canBorrow;
    }

    public void setCanBorrow(String canBorrow) {
        this.canBorrow = canBorrow;
    }

    public String getLiquidatePrice() {
        return liquidatePrice;
    }

    public void setLiquidatePrice(String liquidatePrice) {
        this.liquidatePrice = liquidatePrice;
    }

    public String getP2pOutRate() {
        return p2pOutRate;
    }

    public void setP2pOutRate(String p2pOutRate) {
        this.p2pOutRate = p2pOutRate;
    }

    @Override
    public String toString() {

        return "currencyType=" + currencyType
                + " netAssets=" + netAssets
                + " hasBorrowed=" + hasBorrowed
                + " canBorrow=" + canBorrow
                + " liquidatePrice=" + liquidatePrice
                + " p2pOutRate=" + p2pOutRate
                +" | ";
    }
}
