package com.android.bitglobal.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/8.
 */

public class VipInfoResult {

    private List<VipLevelInfo> userVipLevelList = new ArrayList<>();

    private List<VipLevelRule> integralRuleList = new ArrayList<>();

    public List<VipLevelInfo> getUserVipLevelList() {
        return userVipLevelList;
    }

    public void setUserVipLevelList(List<VipLevelInfo> userVipLevelList) {
        this.userVipLevelList = userVipLevelList;
    }

    public List<VipLevelRule> getIntegralRuleList() {
        return integralRuleList;
    }

    public void setIntegralRuleList(List<VipLevelRule> integralRuleList) {
        this.integralRuleList = integralRuleList;
    }

}
