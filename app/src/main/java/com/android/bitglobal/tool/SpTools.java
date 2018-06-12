package com.android.bitglobal.tool;

import com.android.bitglobal.entity.TradeOverviewResult;
import com.google.gson.Gson;

/**
 * Created by joyson on 2017/9/4.
 */

public class SpTools {
    public static String getlegalTender(){
        return SharedPreferences.getInstance().getString(SharedPreferences.APP_CURRENCY,"USD");
    }
    public static void setlegalTender(String legalTender){
        SharedPreferences.getInstance().putString(SharedPreferences.APP_CURRENCY, legalTender);
    }
    /*splash页面设置过ip的标记（只在清空缓存或第一次加载该app的标记）*/
    public static boolean getIpLegalTender(){
        return SharedPreferences.getInstance().getBoolean(SharedPreferences.IS_FIRST_TO_APP,true);
    }
    public static void setIplegalTender(boolean legalTender){
        SharedPreferences.getInstance().putBoolean(SharedPreferences.IS_FIRST_TO_APP, legalTender);
    }
    /*资金总览页 缓存*/
    public static TradeOverviewResult getTradeOverview(){
        String tradeString = SharedPreferences.getInstance().getString(SharedPreferences.CACHE_TRADE_OVERVIEW, "");
        TradeOverviewResult tradeOverviewResult = new Gson().fromJson(tradeString, TradeOverviewResult.class);
        return tradeOverviewResult;
    }

    public static void setTradeOverView(TradeOverviewResult result){
        String tradeOverView = new Gson().toJson(result);
        SharedPreferences.getInstance().putString(SharedPreferences.CACHE_TRADE_OVERVIEW, tradeOverView);
    }


}
