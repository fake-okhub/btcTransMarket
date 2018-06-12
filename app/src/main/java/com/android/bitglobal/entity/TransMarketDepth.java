package com.android.bitglobal.entity;


/**
 * xiezuofei
 * 2016-08-02 18:20
 * 793169940@qq.com
 */
public class TransMarketDepth {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id=" + id);
        sb.append(" name=" + name);
        return sb.toString();
    }
}
