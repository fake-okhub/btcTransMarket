package com.android.bitglobal.entity;

/**
 * xiezuofei
 * 2016-08-30 12:20
 * 793169940@qq.com
 * 一键还款返回消息
 */
public class FastRepayResult {
    //private String failedRecords;
    private String available;



    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("available=" + available);
        return sb.toString();
    }
}
