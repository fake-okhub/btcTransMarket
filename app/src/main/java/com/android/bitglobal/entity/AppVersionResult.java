package com.android.bitglobal.entity;
/**
 * xiezuofei
 * 2016-08-30 16:00
 * 793169940@qq.com
 */
public class AppVersionResult {

    private String url;//下载地址
    private String version;//版本名称
    private boolean released;//是否发布
    private String content;//公告内容
    private String title;//功能标题
    private String size;//包大小
    private boolean isEnforceUpdate;//是否强制更新

    public boolean isEnforceUpdate() {
        return isEnforceUpdate;
    }

    public void setEnforceUpdate(boolean enforceUpdate) {
        isEnforceUpdate = enforceUpdate;
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
