package com.android.bitglobal.entity;


/**
 * xiezuofei
 * 2016-09-07 16:00
 * 793169940@qq.com
 */
public class HistoryBank{
    private String id;//账户ID
    private String bankStaticId;//静态ID(支付宝=1)
    private String owner;//账户所有人姓名
    private String cardNumber;//账户账号
    private String bankName;//银行名/“支付宝”
    private String province;//所属省编号(充值为0)
    private String city;//所属市编号(充值为0)
    private String branch;//支行名(充值为空)
    private String isDefault;//1为默认银行2为普通银行卡(充值为0)
    private String img;//银行图标
    private String xstb;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankStaticId() {
        return bankStaticId;
    }

    public void setBankStaticId(String bankStaticId) {
        this.bankStaticId = bankStaticId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getXstb() {
        return xstb;
    }

    public void setXstb(String xstb) {
        this.xstb = xstb;
    }

    @Override
    public String toString() {
        return "id=" + id
                + " bankStaticId=" + bankStaticId
                + " owner=" + owner
                + " cardNumber=" + cardNumber
                + " bankName=" + bankName
                + " province=" + province
                + " city=" + city
                + " branch=" + branch
                + " isDefault=" + isDefault
                + " img=" + img
                +" | ";
    }
}
