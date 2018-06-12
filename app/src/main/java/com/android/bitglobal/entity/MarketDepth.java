package com.android.bitglobal.entity;
/**
 * xiezuofei
 * 2016-08-02 18:20
 * 793169940@qq.com
 */
public class MarketDepth {
    //当前价格
    private String currentPrice;
    //卖方深度
    private String asks[][];
    //买方深度
    private String bids[][];

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String[][] getAsks() {
        return asks;
    }

    public void setAsks(String[][] asks) {
        this.asks = asks;
    }

    public String[][] getBids() {
        return bids;
    }

    public void setBids(String[][] bids) {
        this.bids = bids;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("currentPrice=" + currentPrice);
        if (null != asks) {
            sb.append(" asks:" + asks.toString());
        }
        if (null != bids) {
            sb.append(" bids:" + bids.toString());
        }
        return sb.toString();
    }
}
