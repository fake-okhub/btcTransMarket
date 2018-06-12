package com.android.bitglobal.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * xiezuofei
 * 2016-11-18 13:20
 * 793169940@qq.com
 * 文章
 */
public class Article extends RealmObject {
    @PrimaryKey
    private String id;
    private String content;
    private String summary;
    private String publishTime;
    private String title;
    private String link;
    private String type;
    private String isVisible;//是否显示
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible;
    }

    @Override
    public String toString() {
        return " id=" + id
                + " summary=" + summary
                + " publishTime=" + publishTime
                + " title=" + title
                + " link=" + link
                + " isVisible=" + isVisible
                +" | ";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
