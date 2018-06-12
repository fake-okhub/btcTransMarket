package com.android.bitglobal.entity;

import java.util.List;

/**
 * Created by bitbank on 17/3/7.
 */
public class MarketChartDataResult {
    public List<MarketChartData> getMarketDatas() {
        return marketChartDatas;
    }

    public void setMarketDatas(List<MarketChartData> marketDatas) {
        this.marketChartDatas = marketDatas;
    }
    private List<MarketChartData> marketChartDatas;

    @Override
    public String toString() {
        return "marketDatas" + marketChartDatas
                +" | ";
    }
}
