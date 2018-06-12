package com.android.bitglobal.entity;

import java.util.List;

/**
 * Created by joyson on 2017/9/26.
 */

public class TradeOverviewResult {

    /**
     * BTC : {"usableFund":"100.123","freezeFund":"23.00","convertFund":"123.123","symbolList":[{"propTag":"ETH","totalFund":"148.11336","holdCount":"123.12","lastPrice":"1.203"},{"propTag":"LTC","totalFund":"148.11336","holdCount":"123.12","lastPrice":"1.203"}],"exchangeRate":{"AUD":"3","EUR":"4","GBP":"5","USD":"1","CNY":"2"}}
     * USDC : {"usableFund":"100.123","freezeFund":"23.00","convertFund":"123.123","symbolList":[{"propTag":"BTC","totalFund":"148.11336","holdCount":"123.12","lastPrice":"1.203"},{"propTag":"LTC","totalFund":"148.11336","holdCount":"123.12","lastPrice":"1.203"}],"exchangeRate":{"AUD":"3","EUR":"4","GBP":"5","USD":"1","CNY":"2"}}
     */

    private CoinBean BTC;
    private CoinBean USDC;

    public CoinBean getBTC() {
        return BTC;
    }

    public void setBTC(CoinBean BTC) {
        this.BTC = BTC;
    }

    public CoinBean getUSDC() {
        return USDC;
    }

    public void setUSDC(CoinBean USDC) {
        this.USDC = USDC;
    }

    @Override
    public String toString() {
        return "TradeOverviewResult{" +
                "BTC=" + BTC +
                ", USDC=" + USDC +
                '}';
    }

    public static class CoinBean {
        /**
         * usableFund : 100.123
         * freezeFund : 23.00
         * convertFund : 123.123
         * symbolList : [{"propTag":"BTC","totalFund":"148.11336","holdCount":"123.12","lastPrice":"1.203"},{"propTag":"LTC","totalFund":"148.11336","holdCount":"123.12","lastPrice":"1.203"}]
         * exchangeRate : {"AUD":"3","EUR":"4","GBP":"5","USD":"1","CNY":"2"}
         */

        private String usableFund;
        private String freezeFund;
        private String convertFund;
        private ExchangeRateBeanX exchangeRate;
        private List<SymbolListBeanX> symbolList;

        public String getUsableFund() {
            return usableFund;
        }

        public void setUsableFund(String usableFund) {
            this.usableFund = usableFund;
        }

        public String getFreezeFund() {
            return freezeFund;
        }

        public void setFreezeFund(String freezeFund) {
            this.freezeFund = freezeFund;
        }

        public String getConvertFund() {
            return convertFund;
        }

        public void setConvertFund(String convertFund) {
            this.convertFund = convertFund;
        }

        public ExchangeRateBeanX getExchangeRate() {
            return exchangeRate;
        }

        public void setExchangeRate(ExchangeRateBeanX exchangeRate) {
            this.exchangeRate = exchangeRate;
        }

        public List<SymbolListBeanX> getSymbolList() {
            return symbolList;
        }

        public void setSymbolList(List<SymbolListBeanX> symbolList) {
            this.symbolList = symbolList;
        }

        @Override
        public String toString() {
            return "CoinBean{" +
                    "usableFund='" + usableFund + '\'' +
                    ", freezeFund='" + freezeFund + '\'' +
                    ", convertFund='" + convertFund + '\'' +
                    ", exchangeRate=" + exchangeRate +
                    ", symbolList=" + symbolList +
                    '}';
        }

        public static class ExchangeRateBeanX {
            /**
             * AUD : 3
             * EUR : 4
             * GBP : 5
             * USD : 1
             * CNY : 2
             */

            public String AUD;
            public String EUR;
            public String GBP;
            public String USD;
            public String CNY;

            public String getAUD() {
                return AUD;
            }

            public void setAUD(String AUD) {
                this.AUD = AUD;
            }

            public String getEUR() {
                return EUR;
            }

            public void setEUR(String EUR) {
                this.EUR = EUR;
            }

            public String getGBP() {
                return GBP;
            }

            public void setGBP(String GBP) {
                this.GBP = GBP;
            }

            public String getUSD() {
                return USD;
            }

            public void setUSD(String USD) {
                this.USD = USD;
            }

            public String getCNY() {
                return CNY;
            }

            public void setCNY(String CNY) {
                this.CNY = CNY;
            }

            @Override
            public String toString() {
                return "ExchangeRateBeanX{" +
                        "AUD='" + AUD + '\'' +
                        ", EUR='" + EUR + '\'' +
                        ", GBP='" + GBP + '\'' +
                        ", USD='" + USD + '\'' +
                        ", CNY='" + CNY + '\'' +
                        '}';
            }
        }

        public static class SymbolListBeanX {
            /**
             * propTag : BTC
             * totalFund : 148.11336
             * holdCount : 123.12
             * lastPrice : 1.203
             */

            private String propTag;
            private String totalFund;
            private String holdCount;
            private String lastPrice;

            public String getPropTag() {
                return propTag;
            }

            public void setPropTag(String propTag) {
                this.propTag = propTag;
            }

            public String getTotalFund() {
                return totalFund;
            }

            public void setTotalFund(String totalFund) {
                this.totalFund = totalFund;
            }

            public String getHoldCount() {
                return holdCount;
            }

            public void setHoldCount(String holdCount) {
                this.holdCount = holdCount;
            }

            public String getLastPrice() {
                return lastPrice;
            }

            public void setLastPrice(String lastPrice) {
                this.lastPrice = lastPrice;
            }

            @Override
            public String toString() {
                return "SymbolListBeanX{" +
                        "propTag='" + propTag + '\'' +
                        ", totalFund='" + totalFund + '\'' +
                        ", holdCount='" + holdCount + '\'' +
                        ", lastPrice='" + lastPrice + '\'' +
                        '}';
            }
        }
    }
}
