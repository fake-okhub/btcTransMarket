package com.android.bitglobal.entity;

import java.util.List;

public class MarketDataListResult {
    private String des;
    private boolean isSuc;
    private List<BTC123MarketData> datas;

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public boolean isSuc() {
        return isSuc;
    }

    public void setSuc(boolean suc) {
        isSuc = suc;
    }

    public List<BTC123MarketData> getDatas() {
        return datas;
    }

    public void setDatas(List<BTC123MarketData> datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return " des=" + des
                + " isSuc=" + isSuc
                + " datas=" + datas.toString()
                +" | ";
    }
}
