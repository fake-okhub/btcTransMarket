package com.android.bitglobal.entity;

import com.android.bitglobal.R;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/7/6.
 */

public class CoinPriceFromOther implements Serializable {

    private String from; //来源网站

    private String last_price;//当前价格

    private String last_price_legal_tender;//当前法币价格

    private String vol;//24小时交易量

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getLast_price() {
        return last_price;
    }

    public void setLast_price(String last_price) {
        this.last_price = last_price;
    }

    public String getLast_price_legal_tender() {
        return last_price_legal_tender;
    }

    public void setLast_price_legal_tender(String last_price_legal_tender) {
        this.last_price_legal_tender = last_price_legal_tender;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

}
