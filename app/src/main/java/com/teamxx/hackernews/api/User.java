package com.teamxx.hackernews.api;

import android.text.Html;

/**
 * A "User".
 * Analogous to the "User" in the HN API: https://github.com/HackerNews/API#users
 */
public class User {
    private String mId;
    private int mDelay;
    private long mCreated;
    private int mKarma;
    private Html mAbout;
    private int[] mSubmitted;

    private User(Builder builder) {
        mId = builder.mId;
        mDelay = builder.mDelay;
        mCreated = builder.mCreated;
        mKarma = builder.mKarma;
        mAbout = builder.mAbout;
        mSubmitted = builder.mSubmitted;
    }

    public String getId() {
        return mId;
    }

    public int getDelay() {
        return mDelay;
    }

    public long getCreated() {
        return mCreated;
    }

    public int getKarma() {
        return mKarma;
    }

    public Html getAbout() {
        return mAbout;
    }

    public int[] getSubmitted() {
        return mSubmitted;
    }

    public static final class Builder {
        private String mId;
        private int mDelay;
        private long mCreated;
        private int mKarma;
        private Html mAbout;
        private int[] mSubmitted;

        public Builder() {
        }

        public Builder id(String id) {
            this.mId = id;
            return this;
        }

        public Builder delay(int delay) {
            this.mDelay = delay;
            return this;
        }

        public Builder created(long created) {
            this.mCreated = created;
            return this;
        }

        public Builder karma(int karma) {
            this.mKarma = karma;
            return this;
        }

        public Builder about(Html about) {
            this.mAbout = about;
            return this;
        }

        public Builder submitted(int[] submitted) {
            this.mSubmitted = submitted;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
