package com.team11.hackernews.api;

import android.os.Build;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Comment {
    private Long mId;
    private String mBy;
    private long mTime;
    private String mText;
    private long mParent;
    private List<Long> mKids;
    private int mDepth;

    protected Comment(Builder builder) {
        mId = builder.mId;
        mBy = builder.mBy;
        mTime = builder.mTime;
        mText = builder.mText;
        mParent = builder.mParent;
        mKids = builder.mKids;
        mDepth = builder.mDepth;
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

    public List<Long> getKids() {
        return mKids;
    }

    public int getDepth() {
        return mDepth;
    }

    public static class Builder {
        private Long mId;
        private String mBy;
        private long mTime;
        private String mText;
        private long mParent;
        private List<Long> mKids;
        private int mDepth;

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

        public Builder kids(List<Long> kids) {
            this.mKids = kids;
            return this;
        }

        public Builder depth(int depth) {
            this.mDepth = depth;
            return this;
        }

        public Comment build() {
            return new Comment(this);
        }
    }
}