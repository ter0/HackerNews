package com.team11.hackernews.api;

import java.net.URL;
import java.util.List;

public class Story {

    private Long mId;
    private String mBy;
    private long mTime;
    private String mText;
    private List<Long> mKids;
    private URL mURL;
    private Long mScore;
    private String mTitle;

    private Story(Builder builder) {
        mId = builder.mId;
        mBy = builder.mBy;
        mTime = builder.mTime;
        mText = builder.mText;
        mKids = builder.mKids;
        mURL = builder.mURL;
        mScore = builder.mScore;
        mTitle = builder.mTitle;
    }

    public Long getId() {
        return mId;
    }

    public String getBy() {
        return mBy;
    }

    public long getTime() {
        return mTime;
    }

    public String getText() {
        return mText;
    }

    public List<Long> getKids() {
        return mKids;
    }

    public URL getURL() {
        return mURL;
    }

    public Long getScore() {
        return mScore;
    }

    public String getTitle() {
        return mTitle;
    }

    public static final class Builder {
        private Long mId;
        private String mBy;
        private long mTime;
        private String mText;
        private List<Long> mKids;
        private URL mURL;
        private Long mScore;
        private String mTitle;

        public Builder() {
        }

        public Builder id(Long id) {
            this.mId = id;
            return this;
        }

        public Builder by(String by) {
            this.mBy = by;
            return this;
        }

        public Builder time(long time) {
            this.mTime = time;
            return this;
        }

        public Builder text(String text) {
            this.mText = text;
            return this;
        }

        public Builder kids(List<Long> kids) {
            this.mKids = kids;
            return this;
        }

        public Builder URL(URL URL) {
            this.mURL = URL;
            return this;
        }

        public Builder score(Long score) {
            this.mScore = score;
            return this;
        }

        public Builder title(String title) {
            this.mTitle = title;
            return this;
        }

        public Story build() {
            return new Story(this);
        }
    }
}
