package com.android.bitglobal.entity;

import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2017/3/8.
 */

public class UserVipInfo {

    //当前用户等级
    private int currentRate;
    //当前用户共有积分
    private double currentPoints;
    //下一级起始积分
    private double nextRateBeginPoint;
    //当前级起始积分
    private double currentRateBeginPoint;
    //下一等级
    private int nextRate;
    //是否满级
    private boolean isFull;

    public int getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(int currentRate) {
        this.currentRate = currentRate;
    }

    public double getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(double currentPoints) {
        this.currentPoints = currentPoints;
    }

    public double getNextRateBeginPoint() {
        return nextRateBeginPoint;
    }

    public void setNextRateBeginPoint(double nextRateBeginPoint) {
        this.nextRateBeginPoint = nextRateBeginPoint;
    }

    public double getCurrentRateBeginPoint() {
        return currentRateBeginPoint;
    }

    public void setCurrentRateBeginPoint(double currentRateBeginPoint) {
        this.currentRateBeginPoint = currentRateBeginPoint;
    }

    public int getNextRate() {
        return nextRate;
    }

    public void setNextRate(int nextRate) {
        this.nextRate = nextRate;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

}
