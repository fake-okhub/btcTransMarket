package com.android.bitglobal.entity;


/**
 * Elbert
 * 2017-03-20
 * 未读公告
 */
public class UnReadNoticeResult {

    /**
     * summary : <strong>cccc</strong>
     * id : 6
     * title : ccccccc
     * publishTime : 1489993380000
     */

    private String summary;
    private String id;
    private String title;
    private long publishTime;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }
}
