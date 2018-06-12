package com.android.bitglobal.entity;
/**
 * xiezuofei
 * 2016-09-02 13:20
 * 793169940@qq.com
 */
public class ObjectResult{


    public String userId;//用户ID
    public String userOpenId;//
//    private String dynamicCode;
    private String message;
    private String code;
    private MarketRemindResult marketRemind;

    private String recommendLink;
    private String recommendGuide;
    private String recommendTitle;
    private String recommendPathWithArgs;
    private String recommendContent;
    private String shareImg;
    private String recommendPath;


    private BankTrade bankTrade;
    private String fileName;
    private String rate;


    private String title;
    private String summary;
    private String hyperlink;

    public String getRecommendPath() {
        return recommendPath;
    }

    public void setRecommendPath(String recommendPath) {
        this.recommendPath = recommendPath;
    }

    public String getRecommendPathWithArgs() {
        return recommendPathWithArgs;
    }

    public void setRecommendPathWithArgs(String recommendPathWithArgs) {
        this.recommendPathWithArgs = recommendPathWithArgs;
    }
    public String getRecommendLink() {
        return recommendLink;
    }

    public void setRecommendLink(String recommendLink) {
        this.recommendLink = recommendLink;
    }

    public String getRecommendGuide() {
        return recommendGuide;
    }

    public void setRecommendGuide(String recommendGuide) {
        this.recommendGuide = recommendGuide;
    }

    public String getRecommendTitle() {
        return recommendTitle;
    }

    public void setRecommendTitle(String recommendTitle) {
        this.recommendTitle = recommendTitle;
    }

    public String getRecommendContent() {
        return recommendContent;
    }

    public void setRecommendContent(String recommendContent) {
        this.recommendContent = recommendContent;
    }

    public String getShareImg() {
        return shareImg;
    }

    public void setShareImg(String shareImg) {
        this.shareImg = shareImg;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public MarketRemindResult getMarketRemind() {
        return marketRemind;
    }

    public void setMarketRemind(MarketRemindResult marketRemind) {
        this.marketRemind = marketRemind;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserOpenId() {
        return userOpenId;
    }

    public void setUserOpenId(String userOpenId) {
        this.userOpenId = userOpenId;
    }

    private Login2Info login2;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Login2Info getLogin2() {
        return login2;
    }

    public void setLogin2(Login2Info login2) {
        this.login2 = login2;
    }

//    public String getDynamicCode() {
//        return dynamicCode;
//    }

//    public void setDynamicCode(String dynamicCode) {
//        this.dynamicCode = dynamicCode;
//    }

    public BankTrade getBankTrade() {
        return bankTrade;
    }

    public void setBankTrade(BankTrade bankTrade) {
        this.bankTrade = bankTrade;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getHyperlink() {
        return hyperlink;
    }

    public void setHyperlink(String hyperlink) {
        this.hyperlink = hyperlink;
    }

    @Override
    public String toString() {
        return " login2=" + login2
                + " userId=" + userId
                + " userOpenId=" + userOpenId
                + " message=" + message
                + " code=" + code
                + " recommendLink=" + recommendLink
                + " recommendGuide=" + recommendGuide
                + " recommendTitle=" + recommendTitle
                + " recommendContent=" + recommendContent
                + " shareImg=" + shareImg
                + " recommendPath=" + recommendPath
                + " bankTrade=" + bankTrade
                + " rate=" + rate
                + " recommendPathWithArgs=" + recommendPathWithArgs
                + " title=" + title
                + " summary=" + summary
                + " hyperlink=" + hyperlink
                +" | ";
    }
}
