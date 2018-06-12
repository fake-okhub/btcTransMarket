package com.android.bitglobal.manager;

import android.text.TextUtils;

import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.TradeOverviewResult;
import com.android.bitglobal.tool.PriceUtils;
import com.android.bitglobal.tool.SpTools;
import com.android.bitglobal.tool.Utils;

import java.util.List;

/**
 * Created by joyson on 2017/9/29.
 */

public class ExchangeNewSubManager {
    //虚拟币名称
    public static final String coinBTC="BTC";
    public static final String coinETH="ETH";
    public static final String coinLTC="LTC";
    public static final String coinGBC="GBC";
    //虚拟币持有数拦截小数位数
    public static final int coinBTCNum=5;
    public static final int coinETHNum=4;
    public static final int coinLTCNum=3;
    public static final int coinGBCNum=2;

    /**
     * 显示汇率小数位数精度
     */
    public static final int EXCHANGE_RATE_PRECISION=4;
    /**
     * btc市场下，关于btc相关的小数位精确度
     */
    public static final int EXCHANGE_BTC_PRECISION=6;





    public static String exchangeDef="btc";

    public static String emptypecialsTag="---";

    private static ExchangeNewSubManager manager;
    public static ExchangeNewSubManager getInstance(){
        if (manager==null){
            manager=new ExchangeNewSubManager();
        }
        return manager;
    }

    /**
     * 判断用户是否登录
     * @return
     */
    public boolean isNotLogin(){
        return TextUtils.isEmpty(UserDao.getInstance().getUserId()) && TextUtils.isEmpty(UserDao.getInstance().getToken());
    }

    /**
     * 根据缓存法币，返回法币符号
     * @return
     */
    public String getCoinSymbol(){
        String currentCurrency = SpTools.getlegalTender();
        switch (currentCurrency){
            case SystemConfig.AUD:
                return "A$";
            case SystemConfig.USD:
                return "$";
            case SystemConfig.CNY:
                return "¥";
            case SystemConfig.EUR:
                return "€";
            case SystemConfig.GBP:
                return "£";
            default:
                return "$";
        }
    }

    /**
     * 根据缓存获取当前法币汇率
     * @return
     */
    public String getCurrentExchangeRate(TradeOverviewResult.CoinBean usdc){
        TradeOverviewResult.CoinBean.ExchangeRateBeanX exchangeRate = usdc.getExchangeRate();
        String currentLegalTender=SpTools.getlegalTender();
        switch (currentLegalTender){
            case SystemConfig.AUD:
                return exchangeRate.getAUD();
            case SystemConfig.USD:
                return exchangeRate.getUSD();
            case SystemConfig.CNY:
                return exchangeRate.getCNY();
            case SystemConfig.EUR:
                return exchangeRate.getEUR();
            case SystemConfig.GBP:
                return exchangeRate.getGBP();
            default:
                return exchangeRate.getCNY();
        }
    }

    /**
     * 获取不同市场下 小数点精度
     * @param market
     * @return
     */
    public int getCoinPricePrecision(String market){
        switch (market){
            case SystemConfig.USDC:
                return Utils.getPrecisionAmount(market);
            case SystemConfig.BTC://btc市场下 各币的价格精度
                return EXCHANGE_BTC_PRECISION;
            default:
                return Utils.getPrecisionAmount(market);
        }
    }

    /**
     * 顶部持有text & recycItem text
     * 根据持有量和币种名称，返回最终带小数精度的持有量,精度不补零
     * @param coinName
     * @param holdNum
     * @param isFillZero 是否需要补零
     * @param isTextOrItem 是顶部true text or false Item
     * @return
     */
    public String getCoinHoldNum(String coinName, String holdNum,boolean isFillZero,boolean isTextOrItem){
        if (isTextOrItem){
            return PriceUtils.format(holdNum, getCoinPricePrecision(coinName), true);
        }else {
            int num = Utils.getPrecisionAmount(coinName);

            //无小数情况，不添加小数点，默认整数返回
            String holdNumU=PriceUtils.format(holdNum,num,false);
            //若是0这种情况，改变为0.00
            if (Double.valueOf(holdNumU)==0){
                holdNumU = "0.00";
            }
            return holdNumU;
        }
    }


    /**
     * 根据服务端返回数据 若为4 7 .. 给recyclerview 添加empty item，使得recyciew在最后一个画出边框
     * @return
     */
    public List<TradeOverviewResult.CoinBean.SymbolListBeanX> addEmptyBeans(List<TradeOverviewResult.CoinBean.SymbolListBeanX> listBeanXes){
//        List<TradeOverviewResult.CoinBean.SymbolListBeanX> newSymbolListBeanXes=new ArrayList<>();
        if (!listBeanXes.isEmpty()){
//            newSymbolListBeanXes=listBeanXes;
            //说明要添加empty item
            if (listBeanXes.size()%3==1){
                TradeOverviewResult.CoinBean.SymbolListBeanX bean=new TradeOverviewResult.CoinBean.SymbolListBeanX();
                bean.setLastPrice(emptypecialsTag);
                bean.setHoldCount(emptypecialsTag);
                bean.setTotalFund(emptypecialsTag);
                //特殊处理
                bean.setPropTag("");
                listBeanXes.add(bean);
                return listBeanXes;
            }else {
                return listBeanXes;
            }
        }else {
            return listBeanXes;
        }
    }

    /**
     * 此界面最上端 可用 & 锁定 text使用
     * 价格  返回币种的最终价格
     * @param market 币种名称
     * @param price  服务器返回币价
     * @return
     */
    public String getCoinPrice(String market,String price){
        return PriceUtils.format(price,getCoinPricePrecision(market),true);
    }

    /**
     * 此界面recyclerview 界面中item
     * 价格  返回币种的最终价格
     * @param currentMark 当前市场
     * @param coinName 币种名称
     * @param price  服务器返回币价
     * @return
     */
    public String getCoinPrice(String currentMark,String coinName,String price){
        int coinPrecision = Utils.getPrecisionExchange(coinName,currentMark);
        return PriceUtils.format(price,coinPrecision,true);
    }


    //USDC市场各币价格
    public int getCoinPricePrecisionInUSDC(String coin) {
        if ("USD".equals(coin) || "CNY".equals(coin) ||"AUD".equals(coin) || "EUR".equals(coin) || "GBP".equals(coin) || "ETH".equals(coin)) {
            return 2;
        }else if ("LTC".equals(coin)) {
            return 3;
        }else if ("BTC".equals(coin)) {
            return 1;
        }else if ("GBC".equals(coin)) {
            return 5;
        }else{
            return 2;
        }
    }

    //USDC市场持币数量
    public int getCoinNumPrecisionInUSDC(String coin){
        if ("USD".equals(coin) || "CNY".equals(coin) || "AUD".equals(coin) || "EUR".equals(coin) || "GBP".equals(coin) || "GBC".equals(coin) ){
            return 2;
        }else if ("LTC".equals(coin)) {
            return 3;
        }else if ("ETH".equals(coin)) {
            return 4;
        }else if ("BTC".equals(coin)) {
            return 5;
        }else{
            return 2;
        }
    }

    //比特币市场各币价格
    public int getCoinPricePrecisionInBTC(String coin){
        if ("USD".equals(coin) || "CNY".equals(coin) || "AUD".equals(coin) || "EUR".equals(coin) || "GBP".equals(coin) ) {
            return 2;
        }else if ("ETC".equals(coin) || "GBC".equals(coin)) {
            return 7;
        }else if ("ZEC".equals(coin) || "DASH".equals(coin) || "LTC".equals(coin) || "ETH".equals(coin)) {
            return 6;
        }else if ("BTC".equals(coin)) {
            return 6;
        }else{
            return 2;
        }
    }
    //比特币市场持有数量
    public int getCoinNumPrecisionInBTC(String coin) {
        if ("USD".equals(coin) || "CNY".equals(coin) || "AUD".equals(coin) || "EUR".equals(coin) || "GBP".equals(coin) || "GBC".equals(coin)) {
            return 2;
        }else if ("LTC".equals(coin) || "ETC".equals(coin)) {
            return 3;
        }else if ("ETH".equals(coin) || "ZEC".equals(coin) || "DASH".equals(coin)) {
            return 4;
        }else if ("BTC".equals(coin)) {
            return 6;
        }else{
            return 2;
        }
    }

    /**
     * 根据虚拟货币名字，持有量 保留小数位数 不补零
     * String coinName 币种名称
     * String holdNum 币持有量
     *
     */
    private String judgeCoinDecimalsDigit(String coinName,String holdNum){
        switch (coinName){
            case coinBTC:
                return PriceUtils.parsePrice(holdNum,coinBTCNum,false);
            case coinETH:
                return PriceUtils.parsePrice(holdNum,coinETHNum,false);
            case coinLTC:
                return PriceUtils.parsePrice(holdNum,coinLTCNum,false);
            case coinGBC:
                return PriceUtils.parsePrice(holdNum,coinGBCNum,false);
            default:
                return PriceUtils.parsePrice(holdNum,coinBTCNum,false);
        }
    }



}
