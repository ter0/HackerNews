package com.teamxx.hackernews.api;

import android.text.Html;

import java.net.URL;

/**
 * An "Item". This can be a story, comment, job, Ask HN and even a poll.
 * Analogous to the "Item" in the HN API: https://github.com/HackerNews/API#items
 */
public class Item {
    private int mId;
    private boolean mDeleted;
    private Type mType;
    private String mBy;
    private long mTime;
    private Html mText;
    private boolean mDead;
    private int mParent;
    private int[] mKids;
    private URL mURL;
    private int mScore;
    private String mTitle;
    private int[] mParts;

    private Item(Builder builder) {
        mId = builder.mId;
        mDeleted = builder.mDeleted;
        mType = builder.mType;
        mBy = builder.mBy;
        mTime = builder.mTime;
        mText = builder.mText;
        mDead = builder.mDead;
        mParent = builder.mParent;
        mKids = builder.mKids;
        mURL = builder.mURL;
        mScore = builder.mScore;
        mTitle = builder.mTitle;
        mParts = builder.mParts;
    }

    public int getId() {
        return mId;
    }

    public boolean isDeleted() {
        return mDeleted;
    }

    public Type getType() {
        return mType;
    }

    public String getBy() {
        return mBy;
    }

    public long getTime() {
        return mTime;
    }

    public Html getText() {
        return mText;
    }

    public boolean isDead() {
        return mDead;
    }

    public int getParent() {
        return mParent;
    }

    public int[] getKids() {
        return mKids;
    }

    public URL getURL() {
        return mURL;
    }

    public int getScore() {
        return mScore;
    }

    public String getTitle() {
        return mTitle;
    }

    public int[] getParts() {
        return mParts;
    }

    public enum Type {
        Job,
        Story,
        Comment,
        Poll,
        Pollopt
    }

    public static final class Builder {
        private int mId;
        private boolean mDeleted;
        private Type mType;
        private String mBy;
        private long mTime;
        private Html mText;
        private boolean mDead;
        private int mParent;
        private int[] mKids;
        private URL mURL;
        private int mScore;
        private String mTitle;
        private int[] mParts;

        public Builder() {
        }

        public Builder mId(int mId) {
            this.mId = mId;
            return this;
        }

        public Builder mDeleted(boolean mDeleted) {
            this.mDeleted = mDeleted;
            return this;
        }

        public Builder mType(Type mType) {
            this.mType = mType;
            return this;
        }

        public Builder mBy(String mBy) {
            this.mBy = mBy;
            return this;
        }

        public Builder mTime(long mTime) {
            this.mTime = mTime;
            return this;
        }

        public Builder mText(Html mText) {
            this.mText = mText;
            return this;
        }

        public Builder mDead(boolean mDead) {
            this.mDead = mDead;
            return this;
        }

        public Builder mParent(int mParent) {
            this.mParent = mParent;
            return this;
        }

        public Builder mKids(int[] mKids) {
            this.mKids = mKids;
            return this;
        }

        public Builder mURL(URL mURL) {
            this.mURL = mURL;
            return this;
        }

        public Builder mScore(int mScore) {
            this.mScore = mScore;
            return this;
        }

        public Builder mTitle(String mTitle) {
            this.mTitle = mTitle;
            return this;
        }

        public Builder mParts(int[] mParts) {
            this.mParts = mParts;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
    }
}
