package com.android.bitglobal.entity;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bitbank on 16/12/19.
 */
public class TickerArrayResult   {

  public List<MarketDatasResult> getMarketDatas() {
        return marketDatas;
    }

    public void setMarketDatas(List<MarketDatasResult> marketDatas) {
        this.marketDatas = marketDatas;
    }
    private List<MarketDatasResult> marketDatas;

    @Override
    public String toString() {
        return "marketDatas" + marketDatas
                +" | ";
    }
}
