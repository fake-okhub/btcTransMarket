package com.android.bitglobal.entity;

import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2017/3/8.
 */

public class VipLevelRule {

    //主键
    private String id;
    //主键
    private String myId;
    //规则
    private String rule;
    //类型
    private String type;
    //说明
    private String memo;
    //序号
    private int seqNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

}
