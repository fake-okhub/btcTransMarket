package com.android.bitglobal.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mindaset on 2017/7/22.
 */

public class GetCurrencyResult {


    /**
     * default : EUR
     * legal_tender : ["CNY","USD","EUR","GBP","AUD"]
     */

    @SerializedName("default")
    private String defaultX;
    private List<String> legal_tender;

    public String getDefaultX() {
        return defaultX;
    }

    public void setDefaultX(String defaultX) {
        this.defaultX = defaultX;
    }

    public List<String> getLegal_tender() {
        return legal_tender;
    }

    public void setLegal_tender(List<String> legal_tender) {
        this.legal_tender = legal_tender;
    }

    @Override
    public String toString() {
        return "GetCurrencyResult{" +
                "defaultX='" + defaultX + '\'' +
                ", legal_tender=" + legal_tender +
                '}';
    }
}
