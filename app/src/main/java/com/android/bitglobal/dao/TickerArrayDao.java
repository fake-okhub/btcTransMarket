package com.android.bitglobal.dao;

import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.entity.AreaData;
import com.android.bitglobal.entity.AreaDataResult;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.MarketDatasNoTLineResult;
import com.android.bitglobal.entity.MarketDatasResult;
import com.android.bitglobal.entity.TickerArrayResult;
import com.android.bitglobal.entity.TickerArrayResultRealm;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by bitbank on 17/2/28.
 */
public class TickerArrayDao {
    private static TickerArrayDao tickerArrayDao;
    public static synchronized TickerArrayDao getInstance() {
        if (tickerArrayDao == null) {
            tickerArrayDao = new TickerArrayDao();
        }
        return tickerArrayDao;
    }
    private Realm realm= AppContext.getRealm();
    public TickerArrayResultRealm getIfon(){
        TickerArrayResultRealm tickerArrayResult=realm.where(TickerArrayResultRealm.class)
                .findFirst();
        return tickerArrayResult;
    }

    public MarketDatasNoTLineResult getIfon(String coinName){
        MarketDatasNoTLineResult marketDatasResult=realm.where(MarketDatasNoTLineResult.class).equalTo("coinName",coinName)
                .findFirst();
        return marketDatasResult;
    }
    public void add(TickerArrayResultRealm tickerArrayResult){
        RealmResults<TickerArrayResultRealm> areaDataResults = realm.where(TickerArrayResultRealm.class).findAll();
        tickerArrayResult.setVersion(new Date().getTime());
        realm.beginTransaction();
        areaDataResults.deleteAllFromRealm();
        realm.copyToRealmOrUpdate(tickerArrayResult);//提交操作
        realm.commitTransaction();
    }
}
