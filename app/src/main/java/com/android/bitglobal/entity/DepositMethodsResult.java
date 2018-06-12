package com.android.bitglobal.entity;


import java.util.List;

/**
 * xiezuofei
 * 2016-09-08 13:20
 * 793169940@qq.com
 * 获取人民币充值方式
 */
public class DepositMethodsResult {
    private Long version;//版本号
    private List<DepositMethod> depositMethods;//货币配置数组

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<DepositMethod> getDepositMethods() {
        return depositMethods;
    }

    public void setDepositMethods(List<DepositMethod> depositMethods) {
        this.depositMethods = depositMethods;
    }

    @Override
    public String toString() {
        return "version=" + version
                + " depositMethods=" + depositMethods
                +" | ";
    }
}
