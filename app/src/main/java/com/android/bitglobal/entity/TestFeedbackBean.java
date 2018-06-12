package com.android.bitglobal.entity;

import java.util.List;

/**
 * Created by joyson on 2017/8/10.
 */

public class TestFeedbackBean {
    List<FeedBackBean> feedBackBeens;

    public List<FeedBackBean> getFeedBackBeens() {
        return feedBackBeens;
    }

    public void setFeedBackBeens(List<FeedBackBean> feedBackBeens) {
        this.feedBackBeens = feedBackBeens;
    }

    public static  class FeedBackBean{
        private String time;
        private String content;
        private String customResponse;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCustomResponse() {
            return customResponse;
        }

        public void setCustomResponse(String customResponse) {
            this.customResponse = customResponse;
        }

        @Override
        public String toString() {
            return "FeedBackBean{" +
                    "time='" + time + '\'' +
                    ", content='" + content + '\'' +
                    ", customResponse='" + customResponse + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TestFeedbackBean{" +
                "feedBackBeens=" + feedBackBeens +
                '}';
    }
}
