package com.android.bitglobal.entity;

/**
 * xiezuofei
 * 2016-11-28 10:20
 * 793169940@qq.com
 * 界面元素配置
 */
public class ElementSetting{
    private String element;//元素，界面显示的功能模块、输入框或提示信息等等（见全局元素说明）
    private String enabled;//是否开启 1 是 0 否
    private String tips;//提示
    private String showTips;//是否显示提示 1 是 0 否
    private String tipsDetailUrl;

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getShowTips() {
        return showTips;
    }

    public void setShowTips(String showTips) {
        this.showTips = showTips;
    }

    public String getTipsDetailUrl() {
        return tipsDetailUrl;
    }

    public void setTipsDetailUrl(String tipsDetailUrl) {
        this.tipsDetailUrl = tipsDetailUrl;
    }

    @Override
    public String toString() {
        return " element=" + element
                + " enabled=" + enabled
                + " tips=" + tips
                + " showTips=" + showTips
                + " tipsDetailUrl=" + tipsDetailUrl
                +" | ";
    }
}
