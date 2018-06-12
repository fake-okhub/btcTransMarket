package com.android.bitglobal.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bitbank on 17/2/28.
 */
public class TickerArrayResultRealm extends RealmObject {
    @PrimaryKey
    private Long version;//版本号

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    private RealmList<MarketDatasNoTLineResult> marketNoTLineDatas;
    public RealmList<MarketDatasNoTLineResult> getMarketNoTLineDatas() {
        return marketNoTLineDatas;
    }

    public void setMarketNoTLineDatas(RealmList<MarketDatasNoTLineResult> marketDatas) {
        this.marketNoTLineDatas = marketDatas;
    }


    @Override
    public String toString() {
        return "marketDatas" + marketNoTLineDatas
                +" | ";
    }
}
