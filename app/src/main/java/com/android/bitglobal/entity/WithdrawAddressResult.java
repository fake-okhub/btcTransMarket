package com.android.bitglobal.entity;

import java.util.ArrayList;
import java.util.List;


/**
 * xiezuofei
 * 2016-08-25 18:20
 * 793169940@qq.com
 *
 */
public class WithdrawAddressResult {
    private List<WithdrawAddress> withdrawAddrs=new ArrayList<WithdrawAddress>();

    public List<WithdrawAddress> getWithdrawAddrs() {
        return withdrawAddrs;
    }

    public void setWithdrawAddrs(List<WithdrawAddress> withdrawAddrs) {
        this.withdrawAddrs = withdrawAddrs;
    }

    @Override
    public String toString() {
        return "withdrawAddrs=" + withdrawAddrs
                +" | ";
    }
}
