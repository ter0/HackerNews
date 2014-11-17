package com.teamxx.hackernews.api;

import android.text.Html;

import java.net.URL;
import java.util.ArrayList;

/**
 * An "Item". This can be a story, comment, job, Ask HN and even a poll.
 * Analogous to the "Item" in the HN API: https://github.com/HackerNews/API#items
 */
public class Item {
    private String mId;
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

    public String getId() {
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
        private String mId;
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

        public Builder id(String id) {
            this.mId = id;
            return this;
        }

        public Builder deleted(boolean deleted) {
            this.mDeleted = deleted;
            return this;
        }

        public Builder type(Type type) {
            this.mType = type;
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

        public Builder text(Html text) {
            this.mText = text;
            return this;
        }

        public Builder dead(boolean dead) {
            this.mDead = dead;
            return this;
        }

        public Builder parent(int parent) {
            this.mParent = parent;
            return this;
        }

        public Builder kids(int[] kids) {
            this.mKids = kids;
            return this;
        }

        public Builder URL(URL URL) {
            this.mURL = URL;
            return this;
        }

        public Builder score(int score) {
            this.mScore = score;
            return this;
        }

        public Builder title(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder parts(int[] parts) {
            this.mParts = parts;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
    }
}
