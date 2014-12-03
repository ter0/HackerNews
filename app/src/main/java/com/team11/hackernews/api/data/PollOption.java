package com.team11.hackernews.api.data;

public class PollOption {
    private Long mId;
    private String mBy;
    private long mTime;
    private String mText;
    private long mParent;
    private Long mScore;

    private PollOption(Builder builder) {
        mId = builder.mId;
        mBy = builder.mBy;
        mTime = builder.mTime;
        mText = builder.mText;
        mParent = builder.mParent;
        mScore = builder.mScore;
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

    public long getParent() {
        return mParent;
    }

    public Long getScore() {
        return mScore;
    }

    public static final class Builder {
        private Long mId;
        private String mBy;
        private long mTime;
        private String mText;
        private long mParent;
        private Long mScore;

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

        public Builder parent(long parent) {
            this.mParent = parent;
            return this;
        }

        public Builder score(Long score) {
            this.mScore = score;
            return this;
        }

        public PollOption build() {
            return new PollOption(this);
        }
    }

}
