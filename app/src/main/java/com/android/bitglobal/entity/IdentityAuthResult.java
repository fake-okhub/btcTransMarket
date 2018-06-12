package com.android.bitglobal.entity;
/**
 * xiezuofei
 * 2016-09-07 16:00
 * 793169940@qq.com
 */
public class IdentityAuthResult {
    private String status;//0:高级认证-未提交 1:高级认证-待审核 2:高级认证-通过 3:高级认证-不通过 4:初级认证-未提交 5: 初级认证-待审核 6:初级认证-通过 7:初级认证-不通过
    private String statusShow;
    private String[] detailStatus;//[0,1,0,1]	实名认证详细状态数组,-1：未设置 0:不通过 1通过，下标：0基本资料 1证件照片 2银行卡信息 3住址证明
    private String failReason;//实名认证驳回说明
    private String fileBasePath;//文件基础路径，将图片名称拼接到此路径后面组成完成的显示路径
    private String frontalImg;//身份证正面照
    private String backImg;//身份证背面照
    private String loadImg;//手持身份证照
    private String proofAddressImg;//住址证明照
    private String bankCardId;//银行卡号
    private String bankTel;//银行预留手机号
    private String bankId;//银行id

    public String getStatusShow() {
        return statusShow;
    }

    public void setStatusShow(String statusShow) {
        this.statusShow = statusShow;
    }

    private String realName;//真实姓名
    private String cardId;//证件号
    private String country;//证件地区

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(String[] detailStatus) {
        this.detailStatus = detailStatus;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getFileBasePath() {
        return fileBasePath;
    }

    public void setFileBasePath(String fileBasePath) {
        this.fileBasePath = fileBasePath;
    }

    public String getFrontalImg() {
        return frontalImg;
    }

    public void setFrontalImg(String frontalImg) {
        this.frontalImg = frontalImg;
    }

    public String getBackImg() {
        return backImg;
    }

    public void setBackImg(String backImg) {
        this.backImg = backImg;
    }

    public String getLoadImg() {
        return loadImg;
    }

    public void setLoadImg(String loadImg) {
        this.loadImg = loadImg;
    }

    public String getProofAddressImg() {
        return proofAddressImg;
    }

    public void setProofAddressImg(String proofAddressImg) {
        this.proofAddressImg = proofAddressImg;
    }

    public String getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(String bankCardId) {
        this.bankCardId = bankCardId;
    }

    public String getBankTel() {
        return bankTel;
    }

    public void setBankTel(String bankTel) {
        this.bankTel = bankTel;
    }

    public String getBankId() {
        if(bankId==null){
            bankId="";
        }
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "status=" + status
                + " detailStatus=" + detailStatus
                + " failReason=" + failReason
                + " fileBasePath=" + fileBasePath
                + " frontalImg=" + frontalImg
                + " backImg=" + backImg
                + " loadImg=" + loadImg
                + " proofAddressImg=" + proofAddressImg
                + " bankCardId=" + bankCardId
                + " bankTel=" + bankTel
                + " bankId=" + bankId
                + " realName=" + realName
                + " cardId=" + cardId
                + " country=" + country
                + " statusShow=" + statusShow
                +" | ";
    }
}
