package com.android.bitglobal.entity;

/**
 * Created by bitbank on 16/9/18.
 */
public class ActivityResult {

    private String url;//	String	是		活动地址
    private String title;	//String	是		活动标题
    private String isInvolved;	//String	是		是否参与了 0：未参与 1：已参与
    private String topUrl;	//String	是		活动主图URL
    private String activityId;	//Integer	是		活动ID
    private String shareUrl;	//String	是		活动分享URL
    private String shareImg;	//String	是		活动分享图片
    private String shareDes;	//String	是		活动分享描述
    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsInvolved(String isInvolved) {
        this.isInvolved = isInvolved;
    }

    public void setTopUrl(String topUrl) {
        this.topUrl = topUrl;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public void setShareImg(String shareImg) {
        this.shareImg = shareImg;
    }

    public void setShareDes(String shareDes) {
        this.shareDes = shareDes;
    }

    public String getUrl() {

        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getIsInvolved() {
        return isInvolved;
    }

    public String getTopUrl() {
        return topUrl;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public String getShareImg() {
        return shareImg;
    }

    public String getShareDes() {
        return shareDes;
    }
    @Override
    public String toString() {
        return "url=" + url
                + " title=" + title
                + " isInvolved=" + isInvolved
                +" topUrl=" + topUrl
                +" activityId=" + activityId
                +" shareUrl=" + shareUrl
                +" shareImg=" + shareImg
                +" shareDes=" + shareDes
                + " | ";
    }

}
